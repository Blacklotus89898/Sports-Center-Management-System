package ca.mcgill.ecse321.scs.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.scs.model.SpecificClass;

/*
 * DAO (a database link) for crud operations for SpecificClass.
 */
public interface SpecificClassRepository extends CrudRepository<SpecificClass, Integer>{
    @Query("SELECT s FROM SpecificClass s WHERE s.schedule.year = :year")
    public List<SpecificClass> findSpecificClassByScheduleYear(int year);

    public SpecificClass findSpecificClassByClassId(Integer classId);
    
}