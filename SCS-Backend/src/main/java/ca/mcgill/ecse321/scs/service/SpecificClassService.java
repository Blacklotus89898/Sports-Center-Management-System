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

import ca.mcgill.ecse321.scs.dao.ClassTypeRepository;
import ca.mcgill.ecse321.scs.dao.ScheduleRepository;
import ca.mcgill.ecse321.scs.dao.SpecificClassRepository;
import ca.mcgill.ecse321.scs.exception.SCSException;

import ca.mcgill.ecse321.scs.model.ClassType;
import ca.mcgill.ecse321.scs.model.Schedule;
import ca.mcgill.ecse321.scs.model.SpecificClass;

/**
 * The SpecificClassService class provides methods for managing specific classes.
 * It interacts with the SpecificClassRepository, ClassTypeRepository, and ScheduleRepository
 * to perform CRUD operations on specific classes.
 */
@Service
public class SpecificClassService {
    @Autowired
    SpecificClassRepository specificClassRepository;
    
    @Autowired
    ClassTypeRepository classTypeRepository;
    
    @Autowired
    ScheduleRepository scheduleRepository;
    
    @Autowired
    ScheduleService scheduleService;
    
    @Autowired
    ClassTypeService classTypeService;

    /**
     * Set the ClassTypeService and ScheduleService
     * @param classTypeServiceDI
     * @param scheduleServiceDI
     */
    public void dependencyInjection(ClassTypeService classTypeServiceDI, ScheduleService scheduleServiceDI) {
        this.classTypeService = classTypeServiceDI;
        this.scheduleService = scheduleServiceDI;
    }

    /**
     * Create a new specific class with the specified parameters.
     * @param className
     * @param year
     * @param specificClassName
     * @param description
     * @param date
     * @param startTime
     * @param hourDuration
     * @param maxCapacity
     * @param currentCapacity
     * @param registrationFee
     * @return
     */
    @Transactional
    public SpecificClass createSpecificClass(String className, int year, String specificClassName, String description, LocalDate date, LocalTime startTime, int hourDuration, int maxCapacity, int currentCapacity, double registrationFee) {
        if (className == null || className.isEmpty()) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Class type cannot be empty.");
        } else if (description == null || description.isEmpty()) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Description cannot be empty.");
        } else if (date == null) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Invalid date.");
        } else if (startTime == null) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Invalid start time.");
        } else if (hourDuration <= 0) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "The duration cannot be negative.");
        } else if (maxCapacity <= 0) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Maximum capacity must be greater than 0.");
        } else if (currentCapacity < 0) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Current capacity cannot be smaller than 0.");
        } else if (currentCapacity > maxCapacity) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Current capacity must be less than or equal to the max capacity.");
        } else if (registrationFee < 0) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Registration fee cannot be negative.");
        } else if (year != date.getYear()) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Schedule year does not match the date.");
        }

        // check for conflicts with other existing specific classes
        List<SpecificClass> specificClasses = specificClassRepository.findSpecificClassByScheduleYear(year);

        // check for conflicts with other existing specific classes
        for (SpecificClass sC : specificClasses) {
            LocalTime sCStartTime = sC.getStartTime().toLocalTime();
            boolean timeConflict = (startTime.isAfter(sCStartTime) && startTime.isBefore(sCStartTime.plusHours(sC.getHourDuration())))
                    || (startTime.plusHours(hourDuration).isAfter(sCStartTime) && startTime.plusHours(hourDuration).isBefore(sCStartTime.plusHours(sC.getHourDuration())))
                    || (startTime.isBefore(sCStartTime) && startTime.plusHours(hourDuration).isAfter(sCStartTime.plusHours(sC.getHourDuration())));
            if (sC.getDate().equals(Date.valueOf(date)) && timeConflict) {
                throw new SCSException(HttpStatus.BAD_REQUEST, "There is already a specific class at this time.");
            }
        }

        ClassType classType = classTypeService.getClassType(className);
        if (!classType.getIsApproved()) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Class type " + className + " is not approved.");
        }
        Schedule schedule = scheduleService.getSchedule(year);

        SpecificClass specificClass = new SpecificClass();
        specificClass.setClassType(classType);
        specificClass.setSchedule(schedule);
        specificClass.setSpecificClassName(specificClassName);
        specificClass.setDescription(description);
        specificClass.setDate(Date.valueOf(date));
        specificClass.setStartTime(Time.valueOf(startTime));
        specificClass.setHourDuration(hourDuration);
        specificClass.setMaxCapacity(maxCapacity);
        specificClass.setCurrentCapacity(currentCapacity);
        specificClass.setRegistrationFee(registrationFee);

        specificClass = specificClassRepository.save(specificClass);
        return specificClass;
    }

    /**
     * Get the specific class with the specified class ID.
     * @param classId
     * @return
     */
    @Transactional
    public SpecificClass getSpecificClass(int classId) {
        SpecificClass specificClass = specificClassRepository.findSpecificClassByClassId(classId);
        if (specificClass == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Specific class with id " + classId + " not found.");
        }
        return specificClass;
    }

    /**
     * Update the specific class with the specified class ID.
     * @param classId
     * @param className
     * @param year
     * @param specificClassName
     * @param description
     * @param date
     * @param startTime
     * @param hourDuration
     * @param maxCapacity
     * @param currentCapacity
     * @param registrationFee
     * @return
     */
    @Transactional
    public SpecificClass updateSpecificClass(int classId, String className, int year, String specificClassName, String description, LocalDate date, LocalTime startTime, int hourDuration, int maxCapacity, int currentCapacity, double registrationFee) {
        if (className == null || className.isEmpty()) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Class type cannot be empty.");
        } else if (description == null || description.isEmpty()) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Description cannot be empty.");
        } else if (date == null) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Invalid date.");
        } else if (startTime == null) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Invalid start time.");
        } else if (hourDuration <= 0) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "The duration cannot be negative.");
        } else if (maxCapacity <= 0) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Maximum capacity must be greater than 0.");
        } else if (currentCapacity < 0) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Current capacity cannot be smaller than 0.");
        } else if (currentCapacity > maxCapacity) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Current capacity must be less than or equal to the max capacity.");
        } else if (registrationFee < 0) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Registration fee cannot be negative.");
        } else if (year != date.getYear()) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Schedule year does not match the date.");
        }
        
        ClassType classType = classTypeService.getClassType(className);
        if (!classType.getIsApproved()) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Class type " + className + " is not approved.");
        }
        Schedule schedule = scheduleService.getSchedule(year);
        
        SpecificClass specificClass = specificClassRepository.findSpecificClassByClassId(classId);
        if (specificClass == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Specific class with id " + classId + " not found.");
        }

        // check for conflicts with other existing specific classes
        List<SpecificClass> specificClasses = specificClassRepository.findSpecificClassByScheduleYear(year);

        // check for conflicts with other existing specific classes
        for (SpecificClass sC : specificClasses) {
            // if current class, skip
            if (sC.getClassId() == classId) {
                continue;
            }
            
            // boolean for if the startTime is within the start and end time of another specific class
            LocalTime sCStartTime = sC.getStartTime().toLocalTime();
            boolean timeConflict = (startTime.isAfter(sCStartTime) && startTime.isBefore(sCStartTime.plusHours(sC.getHourDuration())))
                    || (startTime.plusHours(hourDuration).isAfter(sCStartTime) && startTime.plusHours(hourDuration).isBefore(sCStartTime.plusHours(sC.getHourDuration())))
                    || (startTime.isBefore(sCStartTime) && startTime.plusHours(hourDuration).isAfter(sCStartTime.plusHours(sC.getHourDuration())));
            if (sC.getDate().equals(Date.valueOf(date)) && timeConflict) {
                throw new SCSException(HttpStatus.BAD_REQUEST, "There is already a specific class at this time.");
            }
        }

        specificClass.setClassType(classType);
        specificClass.setSchedule(schedule);
        specificClass.setSpecificClassName(specificClassName);
        specificClass.setDescription(description);
        specificClass.setDate(Date.valueOf(date));
        specificClass.setStartTime(Time.valueOf(startTime));
        specificClass.setHourDuration(hourDuration);
        specificClass.setMaxCapacity(maxCapacity);
        specificClass.setCurrentCapacity(currentCapacity);
        specificClass.setRegistrationFee(registrationFee);

        specificClass = specificClassRepository.save(specificClass);
        return specificClass;
    }

    /**
     * Get all specific classes.
     * @return
     */
    @Transactional
    public List<SpecificClass> getAllSpecificClasses(int year) {
        return specificClassRepository.findSpecificClassByScheduleYear(year);
        // return ServiceUtils.toList(specificClassRepository.findAll());
    }

    /**
     * Delete the specific class with the specified class ID.
     * @param classId
     */
    @Transactional
    public void deleteSpecificClass(int classId) {
        SpecificClass specificClass = specificClassRepository.findSpecificClassByClassId(classId);
        if (specificClass == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Specific class with id " + classId + " not found.");
        }
        specificClassRepository.delete(specificClass);
    }

    /**
     * Delete all specific classes.
     */
    @Transactional
    public void deleteAllSpecificClasses() {
        specificClassRepository.deleteAll();
    }

    /**
     * Get the specific class for a specific class ID.
     * @param specificClassId
     * @return
     */
    @Transactional
    public void deleteAllSpecificClassesByYear(int year) {
        List<SpecificClass> specificClasses = specificClassRepository.findSpecificClassByScheduleYear(year);
        for (SpecificClass specificClass : specificClasses) {
            specificClassRepository.delete(specificClass);
        }
    }
}