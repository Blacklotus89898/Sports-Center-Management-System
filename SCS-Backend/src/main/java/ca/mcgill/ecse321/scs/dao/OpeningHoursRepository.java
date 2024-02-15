package ca.mcgill.ecse321.scs.dao;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.scs.model.OpeningHours;
import ca.mcgill.ecse321.scs.model.OpeningHours.DayOfWeek;

/*
 * DAO (a database link) for crud operations for OpeningHours.
 */
public interface OpeningHoursRepository extends CrudRepository<OpeningHours, DayOfWeek>{
    public OpeningHours findOpeningHoursByDayOfWeek(DayOfWeek dayOfWeek);   
}