package de.ehi.wt2.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import de.ehi.wt2.entity.DBComment;

public interface CommentRepository extends CrudRepository<DBComment, Long>{
	
	List<DBComment> findAll();
	DBComment findById(long id);

}
