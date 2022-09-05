package de.ehi.wt2.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.ehi.wt2.entity.DBBug;
import de.ehi.wt2.entity.DBComment;
import de.ehi.wt2.entity.DBUser;
import de.ehi.wt2.rest.integration.RestComment;

@Transactional
@RestController
@RequestMapping(path = "api/v1/rest/comment")
public class CommentREST {
	
	@Autowired
	EntityManager entityManager;
	
	@GetMapping(path="{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestComment> getCommentById(@PathVariable("id")final long id) {
		final Subject subject = SecurityUtils.getSubject();
        if (subject == null || !subject.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
		
		DBComment dbComment = entityManager.find(DBComment.class, id);
		
		RestComment restComment = null;
		if(dbComment != null) {
			restComment = new RestComment(dbComment);
		} else {
			//TODO: error handling
			return null;
		}
		
		return ResponseEntity.ok(restComment);
	}
	
	@GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RestComment>> getAllComments() {
		final Subject subject = SecurityUtils.getSubject();
        if (subject == null || !subject.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DBComment> criteriaQuery = criteriaBuilder.createQuery(DBComment.class);
        Root<DBComment> from = criteriaQuery.from(DBComment.class);
        criteriaQuery.select(from);
        
		return ResponseEntity.ok(entityManager.createQuery(criteriaQuery).getResultList().stream().map(dbComment -> new RestComment(dbComment)).collect(Collectors.toList()));
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RestComment>> getCommentsByUser(@RequestParam(required = true) String userid) {
		final Subject subject = SecurityUtils.getSubject();
        if (subject == null || !subject.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
		DBUser user = entityManager.find(DBUser.class, Long.parseLong(userid));
		return ResponseEntity.ok(user.getCommentList().stream().map(dbComment -> new RestComment(dbComment)).collect(Collectors.toList()));
	}
	
	@GetMapping(path="/bug/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RestComment>> getCommentsByBug(@PathVariable("id")final long id){
		final Subject subject = SecurityUtils.getSubject();
        if (subject == null || !subject.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
		DBBug bug = entityManager.find(DBBug.class, id);
		return ResponseEntity.ok(bug.getCommentList().stream().map(dbComment -> new RestComment(dbComment)).collect(Collectors.toList()));
	}
	
	
	@PostMapping(path = "/create",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestComment> createComment(@RequestBody final RestComment param) {		
		final Subject subject = SecurityUtils.getSubject();
	    if (subject == null || !subject.isAuthenticated()) {
	        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	    }
	        
		DBUser creator = entityManager.find(DBUser.class, (long) param.getCreatorId());
		if(creator == null) {
			return null;
		}
		DBBug bug = entityManager.find(DBBug.class, (long) param.getBugId());
		if(bug == null) {
			return null;
		}
		
		DBComment comment = new DBComment();
		
		comment.setComment(param.getComment());
		comment.setPublished(param.getPublished());
		comment.setCreator(creator);
		comment.setBug(bug);
		
		entityManager.persist(comment);
		
		return ResponseEntity.ok(new RestComment(comment));
	}
	
	@DeleteMapping(path = "/delete/{id}")
	@Transactional
	public ResponseEntity<RestComment> deleteBug(@PathVariable final long id) {
		final Subject subject = SecurityUtils.getSubject();
		if (subject == null || !subject.isAuthenticated()) {
		    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		DBComment dbComment= entityManager.find(DBComment.class, id);
		dbComment.getCreator().getCommentList().remove(dbComment);
		entityManager.remove(dbComment);
		
		return ResponseEntity.ok(new RestComment(dbComment));
	}
}
