package de.ehi.wt2;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import de.ehi.wt2.entity.DBUser;

@Component
@Transactional
public class StartupBean implements ApplicationListener<ContextRefreshedEvent> {
    
	@Autowired
	EntityManager entityManager;
	
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
    	try {
    		
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<DBUser> criteriaQuery = criteriaBuilder.createQuery(DBUser.class);
            Root<DBUser> from = criteriaQuery.from(DBUser.class);
            criteriaQuery.select(from);
            criteriaQuery.where(criteriaBuilder.like(from.get("username"), "admin"));
            List<DBUser> dbUserList = entityManager.createQuery(criteriaQuery).getResultList();
    		
    		DBUser dbUser = null;
            if(dbUserList.size() == 0) {
            	dbUser = createUser("admin");
            	entityManager.persist(dbUser);
            } else {
            	//do nothing
            }
    	} catch(Exception e) {
    		System.out.println("ERROR WHILE STARTUPBEAN:");
    		System.err.println(e);
    	}
    }
    
    public DBUser createUser(String name) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.set(1995, 11, 14, 10, 00);
    	
    	DBUser dbUser = new DBUser();
    	dbUser.setUsername(name);
    	dbUser.setFirstname(name);
    	dbUser.setLastname(name);
    	dbUser.setPassword(name);
    	dbUser.setEmail(name+"@"+name+".de");
    	dbUser.setRegistered(new Date(calendar.getTimeInMillis()));
    	dbUser.setRole("admin");
    	
    	return dbUser;
    }
//    
//    public void createBugAndComment(DBUser creator, DBUser assignedUser) {
//
//    	List<DBUser> assignedUserList = new ArrayList<DBUser>();
//    	assignedUserList.add(assignedUser);
//    	
//    	DBBug dbBug = new DBBug();
//    	dbBug.setCreator(creator);
//    	dbBug.setDescription("Error with test.");
//    	dbBug.setHeadline("Test");	
//    	dbBug.setCreated(new Date(System.currentTimeMillis()));
//    	bugRepository.save(dbBug);
//    	
//    	DBComment dbComment = new DBComment();
//    	dbComment.setComment("Test Comment.");
//    	dbComment.setPublished(new Date(System.currentTimeMillis()));
//    	dbComment.setCreator(creator);
//    	dbComment.setBug(dbBug);
//    	commentRepository.save(dbComment);
//    }
    
}
