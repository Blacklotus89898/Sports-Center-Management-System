package ca.mcgill.ecse321.scs.dao;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.scs.model.OpeningHours;
import ca.mcgill.ecse321.scs.model.OpeningHours.DayOfWeek;
import org.springframework.data.jpa.repository.Query;

/*
 * DAO (a database link) for crud operations for OpeningHours.
 */
public interface OpeningHoursRepository extends CrudRepository<OpeningHours, DayOfWeek>{
    @Query("SELECT o FROM OpeningHours o WHERE o.dayOfWeek = :dayOfWeek AND o.schedule.year = :year")
    public OpeningHours findOpeningHoursByDayOfWeek(DayOfWeek dayOfWeek, int year);   
}