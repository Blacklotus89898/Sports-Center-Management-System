package ca.mcgill.ecse321.scs.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.sql.Time;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.scs.model.SpecificClass;
import ca.mcgill.ecse321.scs.model.ClassType;
import ca.mcgill.ecse321.scs.model.Schedule;

@SpringBootTest
public class SpecificClassRepositoryTests {
    @Autowired
    private ClassTypeRepository classTypeRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private SpecificClassRepository specificClassRepository;

    @AfterEach
    public void clearDatabase() {
        specificClassRepository.deleteAll();
        scheduleRepository.deleteAll();
        classTypeRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadSpecificClass() {
        // creation of the schedule
        int year = 2024;

        Schedule schedule = new Schedule(year);

        // save the schedule
        scheduleRepository.save(schedule);


        // creation of the class type
        String className = "Yoga";
        String description = "A class to help you relax your mind and body!";
        boolean isAppoved = true;
        ClassType classType = new ClassType(className, description, isAppoved);

        // save the class type
        classTypeRepository.save(classType);


        // creation of the specific class
        String specificClassName = "Yoga 1A";
        String description2 = "A beginner's class for yoga!";
        Date date = Date.valueOf("2023-05-01");
        Time startTime = Time.valueOf("08:30:00");
        int hourDuration = 1;
        int maxCapacity = 20;
        int currentCapacity = 10;
        double registrationFee = 5.;
        
        SpecificClass specificClass = new SpecificClass();
        specificClass.setSpecificClassName(specificClassName);
        specificClass.setDescription(description2);
        specificClass.setDate(date);
        specificClass.setStartTime(startTime);
        specificClass.setHourDuration(hourDuration);
        specificClass.setMaxCapacity(maxCapacity);
        specificClass.setCurrentCapacity(currentCapacity);
        specificClass.setRegistrationFee(registrationFee);
        specificClass.setClassType(classType);
        specificClass.setSchedule(schedule);

        // save the specific class
        specificClass = specificClassRepository.save(specificClass);
        int classId = specificClass.getClassId();

        // read specific class from database
        specificClass = specificClassRepository.findSpecificClassByClassId(classId);

        // assert equals
        assertNotNull(specificClass);
        assertEquals(specificClassName, specificClass.getSpecificClassName());
        assertEquals(description2, specificClass.getDescription());
        assertEquals(date, specificClass.getDate());
        assertEquals(startTime, specificClass.getStartTime());
        assertEquals(hourDuration, specificClass.getHourDuration());
        assertEquals(maxCapacity, specificClass.getMaxCapacity());
        assertEquals(currentCapacity, specificClass.getCurrentCapacity());
        assertEquals(registrationFee, specificClass.getRegistrationFee());

        // assert equals for associations
        assertEquals(className, specificClass.getClassType().getClassName());
        assertEquals(year, specificClass.getSchedule().getYear());
    }
}
