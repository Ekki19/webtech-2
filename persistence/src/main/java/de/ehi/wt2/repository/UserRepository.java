package de.ehi.wt2.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.ehi.wt2.entity.DBUser;

public interface UserRepository extends CrudRepository<DBUser, Long>{
	
	DBUser findById(long id);
	DBUser findByUsername(String username);
	DBUser findByUsernameAndPassword(String username, String password);
	List<DBUser> findAll();
}
