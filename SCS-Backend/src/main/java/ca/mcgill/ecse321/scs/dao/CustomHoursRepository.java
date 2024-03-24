package ca.mcgill.ecse321.scs.dao;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.scs.model.CustomHours;
import org.springframework.data.jpa.repository.Query;

/*
 * DAO (a database link) for crud operations for CustomHours.
 */
public interface CustomHoursRepository extends CrudRepository<CustomHours, String>{

    @Query("SELECT c FROM CustomHours c WHERE c.name = :name AND c.schedule.year = :year")
    public CustomHours findCustomHoursByName(String name, int year);
    
}