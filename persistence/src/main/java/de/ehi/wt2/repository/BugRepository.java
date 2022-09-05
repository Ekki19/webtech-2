package de.ehi.wt2.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.ehi.wt2.entity.DBBug;

public interface BugRepository extends CrudRepository<DBBug, Long>{
	
	List<DBBug> findAll();
	DBBug findById(long id);

}
