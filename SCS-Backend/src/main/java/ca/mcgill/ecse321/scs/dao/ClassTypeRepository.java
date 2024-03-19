package ca.mcgill.ecse321.scs.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.scs.model.ClassType;

/*
 * DAO (a database link) for crud operations for ClassType.
 */
public interface ClassTypeRepository extends CrudRepository<ClassType, String>{
    public ClassType findClassTypeByClassName(String className);
    
    @Query("SELECT c FROM ClassType c WHERE c.isApproved = true")
    public List<ClassType> findAllApprovedClassTypes();
    
    @Query("SELECT c FROM ClassType c WHERE c.isApproved = false")
    public List<ClassType> findAllNotApprovedClassTypes();
}