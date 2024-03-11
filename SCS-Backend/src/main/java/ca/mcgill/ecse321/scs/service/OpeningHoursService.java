package ca.mcgill.ecse321.scs.service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
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
import ca.mcgill.ecse321.scs.model.OpeningHours.DayOfWeek;

@Service
public class OpeningHoursService {
    @Autowired
    OpeningHoursRepository OpeningHoursRepository;
    @Autowired
    ScheduleService scheduleService;

    public void setScheduleService(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }
    
    @Transactional //date string check
    public OpeningHours createOpeningHours(String name, String description, DayOfWeek date, LocalTime openTime, LocalTime closeTime, int year) {
        // if closeTime before openTime, throw exception
        //date could be a string
        if (date == null || openTime == null || closeTime == null) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Date or Time cannot be empty.");
        } else if (closeTime.isBefore(openTime)) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Close time cannot be before open time.");
        } else if (OpeningHoursRepository.findOpeningHoursByDayOfWeek(date) != null) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Opening hours with date " + date + " already exists.");
        } else if (name == null || name.trim().length() == 0) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Name cannot be empty.");
        } else if (description == null || description.trim().length() == 0) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Description cannot be empty.");
        } 
        // else if (year != date.getYear()) {
        //     throw new SCSException(HttpStatus.BAD_REQUEST, "Year of date does not match year of schedule.");
        // } 

        OpeningHours OpeningHours = new OpeningHours();
        // OpeningHours.setName(name);
        // OpeningHours.setDescription(description);
        OpeningHours.setDayOfWeek(date); //date may need to be parsed from a string
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

    // @Transactional // no need 
    // public OpeningHours getOpeningHours(String name, int year, DayOfWeek day) { //get all of them
    //     OpeningHours OpeningHours = OpeningHoursRepository.findOpeningHoursByDayOfWeek(day);
    //     if (OpeningHours == null) {
    //         throw new SCSException(HttpStatus.NOT_FOUND, "Custom hours with name " + name + " does not exist.");
    //     }
    //     return OpeningHours;
    // }

    @Transactional //check day type
    public OpeningHours getOpeningHoursByDay(DayOfWeek day) {
        // there can only be 1 custom hours for a given date
        List<OpeningHours> OpeningHours = ServiceUtils.toList(OpeningHoursRepository.findAll());
        for (OpeningHours ch : OpeningHours) {
            if (ch.getDayOfWeek().equals(day)) {
                return ch;
            }
        }
        
        throw new SCSException(HttpStatus.NOT_FOUND, "Custom hours for day " + day + " does not exist.");
    }

    @Transactional //need fix
    public OpeningHours updateOpeningHours(String name, String description, LocalDate date, LocalTime openTime, LocalTime closeTime, int year, DayOfWeek day) {
        if (date == null || openTime == null || closeTime == null) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Date or Time cannot be empty.");
        } else if (closeTime.isBefore(openTime)) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Close time cannot be before open time.");
        } else if (name == null || name.trim().length() == 0) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Name cannot be empty.");
        } else if (description == null || description.trim().length() == 0) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Description cannot be empty.");
        } else if (year != date.getYear()) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Year of date does not match year of schedule.");
        } 

        OpeningHours OpeningHours = OpeningHoursRepository.findOpeningHoursByDayOfWeek(day);
        if (OpeningHours == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Custom hours with name " + name + " not found in year " + year + ".");
        }
        // OpeningHours.setDescription(description);
        // OpeningHours.setDate(Date.valueOf(date));
        OpeningHours.setDayOfWeek(day);
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
    public List<OpeningHours> getAllOpeningHours() {
        return ServiceUtils.toList(OpeningHoursRepository.findAll());
    }

    @Transactional //oki check the type of DayOfWeek
    public void deleteOpeningHours(String name, int year, DayOfWeek day) {
        OpeningHours OpeningHours = OpeningHoursRepository.findOpeningHoursByDayOfWeek(day);
        if (OpeningHours == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Opening hours with day " + day + " not found.");
        }
        OpeningHoursRepository.delete(OpeningHours);
    }

    @Transactional //oki
    public void deleteAllOpeningHours() {
        OpeningHoursRepository.deleteAll();
    }

    // helper function
    private DayOfWeek parseDayOfWeekFromLocalDate(LocalDate date) {
        switch (date.getDayOfWeek()) {
            case MONDAY:
                return DayOfWeek.MONDAY;
            case TUESDAY:
                return DayOfWeek.TUESDAY;
            case WEDNESDAY:
                return DayOfWeek.WEDNESDAY;
            case THURSDAY:
                return DayOfWeek.THURSDAY;
            case FRIDAY:
                return DayOfWeek.FRIDAY;
            case SATURDAY:
                return DayOfWeek.SATURDAY;
            case SUNDAY:
                return DayOfWeek.SUNDAY;
            default:
            throw new SCSException(HttpStatus.NOT_FOUND, "Day with day " + date.getDayOfWeek() + " not found.");
        }
        } 
    
}

