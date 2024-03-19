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
    public OpeningHours createOpeningHours(String day, LocalTime openTime, LocalTime closeTime, int year) {
        if (day == null || openTime == null || closeTime == null) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Day or Time cannot be empty.");
        } else if (closeTime.isBefore(openTime)) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Close time cannot be before open time.");
        } else if (OpeningHoursRepository.findOpeningHoursByDayOfWeek(parseDayOfWeekFromString(day), year) != null) {
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
    public OpeningHours getOpeningHoursByDay(String day, int year) {
        OpeningHours openingHours = OpeningHoursRepository.findOpeningHoursByDayOfWeek(parseDayOfWeekFromString(day), year);
        
        if (openingHours == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Opening hours for day " + day + " does not exist for the year " + year + ".");
        }

        return openingHours;
    }

    @Transactional 
    public OpeningHours updateOpeningHours(LocalTime openTime, LocalTime closeTime, int year, String day) {
        if (day == null || openTime == null || closeTime == null) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Date or Time cannot be empty.");
        } else if (closeTime.isBefore(openTime)) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Close time cannot be before open time.");
        } 

        OpeningHours OpeningHours = OpeningHoursRepository.findOpeningHoursByDayOfWeek(parseDayOfWeekFromString(day), year);
        if (OpeningHours == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Opening hours with day " + day + " not found.");
        }

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

    @Transactional //gets all opening hours by year
    public List<OpeningHours> getAllOpeningHours(int year) {
        List<OpeningHours> openingHoursList = ServiceUtils.toList(OpeningHoursRepository.findAll());

        for (int i = 0; i < openingHoursList.size(); i++) {
            if (openingHoursList.get(i).getSchedule().getYear() != year) {
                openingHoursList.remove(i);
                i--;
            }
        }

        return openingHoursList;
    }

    @Transactional 
    public void deleteOpeningHours(String day, int year) {
        OpeningHours openingHours = OpeningHoursRepository.findOpeningHoursByDayOfWeek(parseDayOfWeekFromString(day), year);
        if (openingHours == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Opening hours with day " + day + " not found for the year" + year + ".");
        }
        OpeningHoursRepository.delete(openingHours);
    }

    @Transactional //oki
    public void deleteAllOpeningHours(int year) {
        List<OpeningHours> OpeningHours = ServiceUtils.toList(OpeningHoursRepository.findAll());

        for (OpeningHours ch : OpeningHours) {
            if (ch.getSchedule().getYear() == year) {
                OpeningHoursRepository.delete(ch);
            }
        }
    }

    // helper function //not needed if importing from the model
    DayOfWeek parseDayOfWeekFromString(String dayOfWeekString) {
        return DayOfWeek.valueOf(dayOfWeekString.toUpperCase());
    }
}

