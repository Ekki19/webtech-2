package de.ehi.wt2.rest;

import java.util.ArrayList;
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
import de.ehi.wt2.rest.integration.RestBug;

@Transactional
@RestController
@RequestMapping(path = "api/v1/rest/bug")
public class BugREST {
	
	@Autowired
	private EntityManager entityManager; 
	
	@GetMapping(path="{id}", 
				produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestBug> getBugById(@PathVariable("id")final long id) {
		final Subject subject = SecurityUtils.getSubject();
        if (subject == null || !subject.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
                        
		DBBug dbBug = entityManager.find(DBBug.class, id);
				
		RestBug restBug = null;
		if(dbBug != null) {		
			restBug = new RestBug(dbBug);
		}
		
		return ResponseEntity.ok(restBug);
	}
	
	@GetMapping(path = "/all/public", 
				produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RestBug>> getAllBugs() {
		final Subject subject = SecurityUtils.getSubject();
        if (subject == null || !subject.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DBBug> criteriaQuery = criteriaBuilder.createQuery(DBBug.class);
        Root<DBBug> from = criteriaQuery.from(DBBug.class);
        criteriaQuery.where(criteriaBuilder.isTrue(from.get("ispublic")));
        criteriaQuery.select(from);
        
		return ResponseEntity.ok(entityManager.createQuery(criteriaQuery).getResultList().stream().map(dbBug -> new RestBug(dbBug)).collect(Collectors.toList()));
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RestBug>> getBugsByUser(@RequestParam(required = true) String userid) {
		final Subject subject = SecurityUtils.getSubject();
        if (subject == null || !subject.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        @SuppressWarnings("unchecked")
		List<DBBug> list = entityManager.createQuery("SELECT e FROM DBBug e").getResultList();
        List<RestBug> newList = searchByUserId(list, userid);
        
        //List<DBBug> allBugs = entityManager.find
		DBUser user = entityManager.find(DBUser.class, Long.parseLong(userid));

		List<RestBug> userBugs = user.getBugList().stream().map(dbBug -> new RestBug(dbBug)).collect(Collectors.toList());
		newList.addAll(userBugs);
		return ResponseEntity.ok(newList);	
	}
	
	@PostMapping(path = "/create",
				consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestBug> createBug(@RequestBody final RestBug param) {		
		final Subject subject = SecurityUtils.getSubject();
        if (subject == null || !subject.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
		
		DBUser user = entityManager.find(DBUser.class, (long) param.getCreatorId());
		
		DBBug dbBug = new DBBug();
		dbBug.setCreator(user);
		dbBug.setHeadline(param.getHeadline());
		dbBug.setDescription(param.getDescription());
		dbBug.setCreated(param.getCreated());
		dbBug.setIspublic(param.isIspublic());
		
		if(param.getAffectedusername() != null) {
        	CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<DBUser> criteriaQuery = criteriaBuilder.createQuery(DBUser.class);
            Root<DBUser> from = criteriaQuery.from(DBUser.class);
            criteriaQuery.select(from);
            criteriaQuery.where(criteriaBuilder.like(from.get("username"), param.getAffectedusername()));
            List<DBUser> dbUserList = entityManager.createQuery(criteriaQuery).getResultList();
            
            if(dbUserList.size() != 0) {
            	dbBug.setAffecteduser(dbUserList.get(0));
            }
        }

		
		entityManager.persist(dbBug);
		
		return ResponseEntity.ok(new RestBug(dbBug));
	}
	
	@DeleteMapping(path = "/delete/{id}")
	@Transactional
	public ResponseEntity<RestBug> deleteBug(@PathVariable final long id) {
		final Subject subject = SecurityUtils.getSubject();
        if (subject == null || !subject.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        DBBug dbBug = entityManager.find(DBBug.class, id);
        dbBug.getCreator().getBugList().remove(dbBug);
        
        List<DBComment> dbCommentList = dbBug.getCommentList();
        for(int i=0; i<dbCommentList.size(); i++) {
        	DBComment dbComment = dbCommentList.get(i);
        	dbComment.setBug(null);
        }
        //dbCommentList.clear();
        
        entityManager.remove(dbBug);
        
        return ResponseEntity.ok(new RestBug(dbBug));
	}
	
	@PostMapping(path = "/update",
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseEntity<RestBug> updateBug(@RequestBody final RestBug param) {
		final Subject subject = SecurityUtils.getSubject();
        if (subject == null || !subject.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        DBBug dbBug = entityManager.find(DBBug.class, param.getId());
        if(dbBug == null) {		
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
        
        if(param.getAffectedusername() != null) {
        	CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<DBUser> criteriaQuery = criteriaBuilder.createQuery(DBUser.class);
            Root<DBUser> from = criteriaQuery.from(DBUser.class);
            criteriaQuery.select(from);
            criteriaQuery.where(criteriaBuilder.like(from.get("username"), param.getAffectedusername()));
            List<DBUser> dbUserList = entityManager.createQuery(criteriaQuery).getResultList();
            
            if(dbUserList.size() != 0) {
            	dbBug.setAffecteduser(dbUserList.get(0));
            }
        }
        
        dbBug.setHeadline(param.getHeadline());
        dbBug.setDescription(param.getDescription());
        dbBug.setIspublic(param.isIspublic());
        
        entityManager.merge(dbBug);
        
        return ResponseEntity.ok(new RestBug(dbBug));
	}
	
	public List<RestBug> searchByUserId(List<DBBug> list, String userid) {
		long userid_long = Long.parseLong(userid);
		List<RestBug> restBugList = new ArrayList<RestBug>();
		for(int i=list.size()-1; i >=0; i--) {
			DBBug dbBug = list.get(i);
			DBUser dbUser = dbBug.getAffecteduser();
		
			
			if(dbUser != null && dbUser.getId() == userid_long) {
				restBugList.add(new RestBug(dbBug));
			}
		}
		return restBugList;
	}
}
