package ca.mcgill.ecse321.scs.dao;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.scs.model.CustomHours;

/*
 * DAO (a database link) for crud operations for ClassType.
 */
public interface CustomHoursRepository extends CrudRepository<CustomHours, String>{

    public CustomHours findCustomHoursByName(String name);
    
}