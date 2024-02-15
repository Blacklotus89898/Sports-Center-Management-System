package ca.mcgill.ecse321.scs.dao;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.scs.model.SpecificClass;

/*
 * DAO (a database link) for crud operations for SpecificClass.
 */
public interface SpecificClassRepository extends CrudRepository<SpecificClass, Integer>{

    public SpecificClass findSpecificClassByClassId(Integer classId);
    
}