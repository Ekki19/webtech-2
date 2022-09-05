package de.ehi.wt2.rest;

import java.util.Date;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.ehi.wt2.entity.DBUser;
import de.ehi.wt2.rest.integration.RestUser;

@Transactional
@RestController
@RequestMapping(path = "api/v1/rest/user")
public class UserREST {

	@Autowired
	EntityManager entityManager;
	
	@GetMapping(path="{id}", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestUser> getUserById(@PathVariable("id")final long id) {
		final Subject subject = SecurityUtils.getSubject();
        if (subject == null || !subject.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
		DBUser dbUser = entityManager.find(DBUser.class, id);
		
		RestUser restUser = null;
		if(dbUser!= null) {		
			restUser = new RestUser(dbUser);
		} else {
			//TODO: error handling 
			return null;
		}
		
		return ResponseEntity.ok(restUser);
	}
	
	@GetMapping(path = "/all",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RestUser>> getAllUser() {
		final Subject subject = SecurityUtils.getSubject();
        if (subject == null || !subject.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DBUser> criteriaQuery = criteriaBuilder.createQuery(DBUser.class);
        Root<DBUser> from = criteriaQuery.from(DBUser.class);
        criteriaQuery.select(from);
        
        List<RestUser> userList = entityManager.createQuery(criteriaQuery).getResultList().stream().map(dbUser -> new RestUser(dbUser)).collect(Collectors.toList());
        for(int i=userList.size()-1; i >= 0; i--){
        	RestUser user = userList.get(i);
        	if(user.getRole().equals("admin")){
        		userList.remove(i);
        	}
        }
        
		return ResponseEntity.ok(userList);
	}
		
	@PostMapping(path="/new", 
				produces = MediaType.APPLICATION_JSON_VALUE,
				consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestUser> createUser(@RequestBody final DBUser user) {
		DBUser dbUser = new DBUser();
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DBUser> criteriaQuery = criteriaBuilder.createQuery(DBUser.class);
        Root<DBUser> from = criteriaQuery.from(DBUser.class);
        criteriaQuery.select(from);
        criteriaQuery.where(criteriaBuilder.like(from.get("username"), user.getUsername()));
        List<DBUser> dbUserList = entityManager.createQuery(criteriaQuery).getResultList();
		
        if(dbUserList.size() == 0) {
			dbUser.setUsername(user.getUsername());
			dbUser.setPassword(user.getPassword());		
			dbUser.setFirstname(user.getFirstname());
			dbUser.setLastname(user.getLastname());
			dbUser.setEmail(user.getEmail());
			dbUser.setRegistered(new Date(System.currentTimeMillis()));
			dbUser.setRole("viewer");
			
			
			entityManager.persist(dbUser);
			
			return ResponseEntity.ok(new RestUser(dbUser));
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/loggedin",
			produces = MediaType.APPLICATION_JSON_VALUE) 
	public ResponseEntity<RestUser> isLoggedIn() {
		final Subject subject = SecurityUtils.getSubject();
        if (subject == null || !subject.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        String username = (String) subject.getPrincipal();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DBUser> criteriaQuery = criteriaBuilder.createQuery(DBUser.class);
        Root<DBUser> from = criteriaQuery.from(DBUser.class);
        criteriaQuery.select(from);
        criteriaQuery.where(criteriaBuilder.like(from.get("username"), username));
        List<DBUser> dbUserList = entityManager.createQuery(criteriaQuery).getResultList();
        
        DBUser dbUser = null;
        if(dbUserList.size() == 0) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
        	dbUser = dbUserList.get(0);
        }
		
		return ResponseEntity.ok(new RestUser(dbUser));
	}
	
	@PostMapping(path = "/admin/update",
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseEntity<RestUser> updateBug(@RequestBody final DBUser param) {
		final Subject subject = SecurityUtils.getSubject();
        if (subject == null || !subject.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
      
    	DBUser dbUser = entityManager.find(DBUser.class, param.getId());
    	if(dbUser == null) {		
             return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
 		}
    	 
        if(param.getUsername() != null) {
           
        	CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<DBUser> criteriaQuery = criteriaBuilder.createQuery(DBUser.class);
            Root<DBUser> from = criteriaQuery.from(DBUser.class);
            criteriaQuery.select(from);
            criteriaQuery.where(criteriaBuilder.like(from.get("username"), param.getUsername()));
            List<DBUser> dbUserList = entityManager.createQuery(criteriaQuery).getResultList();

            if(dbUserList.size() != 0) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        	
        	dbUser.setUsername(param.getUsername());
        }
        if(param.getFirstname() != null) {
            dbUser.setFirstname(param.getFirstname());
        }
		if(param.getLastname() != null) {
	        dbUser.setLastname(param.getLastname());
		}
		if(param.getEmail() != null) {
			dbUser.setEmail(param.getEmail());
		}
        if(param.getRole() != null) {
            dbUser.setRole(param.getRole());
        }
        if(param.getPassword() != null && param.getPassword().length() > 0) {
            dbUser.setPassword(param.getPassword());
        }
        entityManager.merge(dbUser);
        
        return ResponseEntity.ok(new RestUser(dbUser));
	}
}
