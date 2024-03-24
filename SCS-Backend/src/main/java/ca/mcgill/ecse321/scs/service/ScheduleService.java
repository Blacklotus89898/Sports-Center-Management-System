package ca.mcgill.ecse321.scs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.scs.dao.ScheduleRepository;
import ca.mcgill.ecse321.scs.exception.SCSException;
import ca.mcgill.ecse321.scs.model.Schedule;

/**
 * The ScheduleService class provides methods for managing schedules.
 * It interacts with the ScheduleRepository to perform CRUD operations on schedules.
 */
@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    /**
     * Create a new schedule with the specified year.
     * @param year
     * @return
     */
    @Transactional
    public Schedule createSchedule(int year) {
        // check if schedule already exists
        if (scheduleRepository.findScheduleByYear(year) != null) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Schedule for year " + year + " already exists.");
        } else if (year < 0) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Year cannot be negative.");
        }
        Schedule schedule = new Schedule();
        schedule.setYear(year);
        schedule = scheduleRepository.save(schedule);
        return schedule;
    }

    /**
     * Get the schedule for the specified year.
     * @param year
     * @return
     */
    @Transactional
    public Schedule getSchedule(int year) {
        Schedule schedule = scheduleRepository.findScheduleByYear(year);
        if (schedule == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Schedule for year " + year + " not found.");
        }
        return schedule;
    }

    /**
     * Get all schedules.
     * @return
     */
    @Transactional
    public List<Schedule> getAllSchedules() {
        return ServiceUtils.toList(scheduleRepository.findAll());
    }

    /**
     * Update the year of the schedule with the specified year.
     * @param year
     * @param newYear
     * @return
     */
    @Transactional
    public void deleteSchedule(int year) {
        Schedule schedule = scheduleRepository.findScheduleByYear(year);
        if (schedule == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Schedule for year " + year + " not found.");
        }
        scheduleRepository.delete(schedule);
    }

    /**
     * Delete all schedules.
     */
    @Transactional
    public void deleteAllSchedules() {
        scheduleRepository.deleteAll();
    }
}
