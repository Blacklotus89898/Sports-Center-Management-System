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

import ca.mcgill.ecse321.scs.dao.CustomHoursRepository;
import ca.mcgill.ecse321.scs.exception.SCSException;
import ca.mcgill.ecse321.scs.model.CustomHours;

import ca.mcgill.ecse321.scs.model.Schedule;

@Service
public class CustomHoursService {
    @Autowired
    CustomHoursRepository customHoursRepository;
    @Autowired
    ScheduleService scheduleService;

    public void setScheduleService(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }
    
    @Transactional
    public CustomHours createCustomHours(String name, String description, LocalDate date, LocalTime openTime, LocalTime closeTime, int year) {
        // if closeTime before openTime, throw exception
        
        if (date == null || openTime == null || closeTime == null) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Date or Time cannot be empty.");
        } else if (closeTime.isBefore(openTime)) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Close time cannot be before open time.");
        } else if (customHoursRepository.findCustomHoursByName(name, year) != null) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Custom hours with name " + name + " already exists.");
        } else if (name == null || name.trim().length() == 0) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Name cannot be empty.");
        } else if (description == null || description.trim().length() == 0) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Description cannot be empty.");
        } else if (year != date.getYear()) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Year of date does not match year of schedule.");
        } 

        CustomHours customHours = new CustomHours();
        customHours.setName(name);
        customHours.setDescription(description);
        customHours.setDate(Date.valueOf(date));
        customHours.setOpenTime(Time.valueOf(openTime));
        customHours.setCloseTime(Time.valueOf(closeTime));

        Schedule schedule = scheduleService.getSchedule(year);
        if (schedule == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Schedule with year " + year + " not found.");
        }
        customHours.setSchedule(schedule);

        customHoursRepository.save(customHours);
        return customHours;
    }

    @Transactional
    public CustomHours getCustomHours(String name, int year) {
        CustomHours customHours = customHoursRepository.findCustomHoursByName(name, year);
        if (customHours == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Custom hours with name " + name + " does not exist.");
        }
        return customHours;
    }

    @Transactional
    public CustomHours getCustomHoursByDate(LocalDate date) {
        // there can only be 1 custom hours for a given date
        List<CustomHours> customHours = ServiceUtils.toList(customHoursRepository.findAll());
        for (CustomHours ch : customHours) {
            if (ch.getDate().equals(Date.valueOf(date))) {
                return ch;
            }
        }
        
        throw new SCSException(HttpStatus.NOT_FOUND, "Custom hours for date " + date + " does not exist.");
    }

    @Transactional
    public CustomHours updateCustomHours(String name, String description, LocalDate date, LocalTime openTime, LocalTime closeTime, int year) {
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

        CustomHours customHours = customHoursRepository.findCustomHoursByName(name, year);
        if (customHours == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Custom hours with name " + name + " not found in year " + year + ".");
        }
        customHours.setDescription(description);
        customHours.setDate(Date.valueOf(date));
        customHours.setOpenTime(Time.valueOf(openTime));
        customHours.setCloseTime(Time.valueOf(closeTime));

        Schedule schedule = scheduleService.getSchedule(year);
        if (schedule == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Schedule with year " + year + " not found.");
        }
        customHours.setSchedule(schedule);

        customHoursRepository.save(customHours);
        return customHours;
    }

    @Transactional
    public List<CustomHours> getAllCustomHours(int year) {
        return ServiceUtils.toList(customHoursRepository.findAll());
    }

    @Transactional
    public void deleteCustomHours(String name, int year) {
        CustomHours customHours = customHoursRepository.findCustomHoursByName(name, year);
        if (customHours == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Custom hours with name " + name + " not found in year " + year + ".");
        }
        customHoursRepository.delete(customHours);
    }

    @Transactional
    public void deleteAllCustomHours(int year) {
        List<CustomHours> customHours = ServiceUtils.toList(customHoursRepository.findAll());

        for (CustomHours ch : customHours) {
            if (ch.getSchedule().getYear() == year) {
                customHoursRepository.delete(ch);
            }
        }
    }
}
