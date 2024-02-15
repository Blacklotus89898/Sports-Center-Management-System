package ca.mcgill.ecse321.scs.dao;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.scs.model.Schedule;

/*
 * DAO (a database link) for crud operations for Schedule.
 */
public interface ScheduleRepository extends CrudRepository<Schedule, Integer>{

    public Schedule findScheduleByYear(Integer year);
    
}