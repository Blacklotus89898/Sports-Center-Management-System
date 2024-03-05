package ca.mcgill.ecse321.scs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.scs.dao.ScheduleRepository;
import ca.mcgill.ecse321.scs.exception.SCSException;
import ca.mcgill.ecse321.scs.model.Schedule;

@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Transactional
    public Schedule createSchedule(int year) {
        // check if schedule already exists
        if (scheduleRepository.findScheduleByYear(year) != null) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Schedule for year " + year + " already exists.");
        }
        Schedule schedule = new Schedule();
        schedule.setYear(year);
        scheduleRepository.save(schedule);
        return schedule;
    }

    @Transactional
    public Schedule getSchedule(int year) {
        Schedule schedule = scheduleRepository.findScheduleByYear(year);
        if (schedule == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Schedule for year " + year + " not found.");
        }
        return scheduleRepository.findScheduleByYear(year);
    }

    @Transactional
    public List<Schedule> getAllSchedules() {
        return ServiceUtils.toList(scheduleRepository.findAll());
    }

    @Transactional
    public Schedule updateSchedule(int year) {
        Schedule schedule = scheduleRepository.findScheduleByYear(year);
        schedule.setYear(year);
        scheduleRepository.save(schedule);
        return schedule;
    }

    @Transactional
    public void deleteSchedule(int year) {
        Schedule schedule = scheduleRepository.findScheduleByYear(year);
        scheduleRepository.delete(schedule);
    }

    @Transactional
    public void deleteAllSchedules() {
        scheduleRepository.deleteAll();
    }
}
