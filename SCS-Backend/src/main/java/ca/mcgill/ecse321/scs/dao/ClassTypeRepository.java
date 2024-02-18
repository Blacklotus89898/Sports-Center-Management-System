package ca.mcgill.ecse321.scs.dao;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.scs.model.ClassType;

/*
 * DAO (a database link) for crud operations for ClassType.
 */
public interface ClassTypeRepository extends CrudRepository<ClassType, String>{

    public ClassType findClassTypeByClassName(String className);
    
}