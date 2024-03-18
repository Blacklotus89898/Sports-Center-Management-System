package ca.mcgill.ecse321.scs.service;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.scs.dao.OpeningHoursRepository;
import ca.mcgill.ecse321.scs.exception.SCSException;
import ca.mcgill.ecse321.scs.model.OpeningHours;

import ca.mcgill.ecse321.scs.model.Schedule;
import ca.mcgill.ecse321.scs.model.OpeningHours.DayOfWeek; //may not be appropriate as controller has no access to it

@Service
public class OpeningHoursService {
    @Autowired
    OpeningHoursRepository OpeningHoursRepository;
    @Autowired
    ScheduleService scheduleService;

    public void setScheduleService(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }
    
    @Transactional //day as a string for controller
    public OpeningHours createOpeningHours(  String day, LocalTime openTime, LocalTime closeTime, int year) {
        // if closeTime before openTime, throw exception
        // day could be a string
        if (day == null || openTime == null || closeTime == null) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Day or Time cannot be empty.");
        } else if (closeTime.isBefore(openTime)) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Close time cannot be before open time.");
        } else if (OpeningHoursRepository.findOpeningHoursByDayOfWeek(parseDayOfWeekFromString(day)) != null) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Opening hours with day " + day + " already exists.");
        } 

        OpeningHours OpeningHours = new OpeningHours();
        OpeningHours.setDayOfWeek(parseDayOfWeekFromString(day));
        OpeningHours.setOpenTime(Time.valueOf(openTime));
        OpeningHours.setCloseTime(Time.valueOf(closeTime));

        Schedule schedule = scheduleService.getSchedule(year);
        if (schedule == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Schedule with year " + year + " not found.");
        }
        OpeningHours.setSchedule(schedule);

        OpeningHoursRepository.save(OpeningHours);
        return OpeningHours;
    }

    @Transactional
    public OpeningHours getOpeningHoursByDay(String day) {
        List<OpeningHours> OpeningHours = ServiceUtils.toList(OpeningHoursRepository.findAll());
        for (OpeningHours ch : OpeningHours) {
            if (ch.getDayOfWeek().equals(DayOfWeek.valueOf(day.toUpperCase()))) {
                return ch;
            }
        }
        
        throw new SCSException(HttpStatus.NOT_FOUND, "Opening hours for day " + day + " does not exist.");
    }

    @Transactional 
    public OpeningHours updateOpeningHours( LocalTime openTime, LocalTime closeTime, int year, String day) {
        if (day == null || openTime == null || closeTime == null) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Date or Time cannot be empty.");
        } else if (closeTime.isBefore(openTime)) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Close time cannot be before open time.");
        } 

        OpeningHours OpeningHours = OpeningHoursRepository.findOpeningHoursByDayOfWeek(parseDayOfWeekFromString(day));
        if (OpeningHours == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Opening hours with day " + day + ".");
        }
        OpeningHours.setDayOfWeek(parseDayOfWeekFromString(day));
        OpeningHours.setOpenTime(Time.valueOf(openTime));
        OpeningHours.setCloseTime(Time.valueOf(closeTime));

        Schedule schedule = scheduleService.getSchedule(year);
        if (schedule == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Schedule with year " + year + " not found.");
        }
        OpeningHours.setSchedule(schedule);

        OpeningHoursRepository.save(OpeningHours);
        return OpeningHours;
    }

    @Transactional //gets all opening hours
    public List<OpeningHours> getAllOpeningHours() {
        return ServiceUtils.toList(OpeningHoursRepository.findAll());
    }

    @Transactional 
    public void deleteOpeningHours(String day) {
        OpeningHours OpeningHours = OpeningHoursRepository.findOpeningHoursByDayOfWeek(parseDayOfWeekFromString(day));
        if (OpeningHours == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Opening hours with day " + day + " not found.");
        }
        OpeningHoursRepository.delete(OpeningHours);
    }

    @Transactional //oki
    public void deleteAllOpeningHours() {
        OpeningHoursRepository.deleteAll();
    }

    // helper function //not needed if importing from the model
    DayOfWeek parseDayOfWeekFromString(String dayOfWeekString) {
        return DayOfWeek.valueOf(dayOfWeekString.toUpperCase());
    }
}

