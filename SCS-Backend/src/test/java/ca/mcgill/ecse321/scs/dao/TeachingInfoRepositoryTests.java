package ca.mcgill.ecse321.scs.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.sql.Time;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.scs.model.TeachingInfo;
import ca.mcgill.ecse321.scs.model.Instructor;
import ca.mcgill.ecse321.scs.model.Schedule;
import ca.mcgill.ecse321.scs.model.SpecificClass;
import ca.mcgill.ecse321.scs.model.ClassType;

@SpringBootTest
public class TeachingInfoRepositoryTests {
    @Autowired
    private TeachingInfoRepository teachingInfoRepository;
    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private SpecificClassRepository specificClassRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private ClassTypeRepository classTypeRepository;

    @AfterEach
    public void clearDatabase() {
        teachingInfoRepository.deleteAll();
        instructorRepository.deleteAll();
        specificClassRepository.deleteAll();
        scheduleRepository.deleteAll();
        classTypeRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadTeachingInfo() {
        // creation of the owner
        String name = "Koko"; 
        Date date = new Date(0);
        String email = "Kolo@gym.com";
        String password = "secret";

        Instructor instructor = new Instructor();
        instructor.setCreationDate(date);
        instructor.setEmail(email);
        instructor.setName(name);
        instructor.setPassword(password);

        instructor = instructorRepository.save(instructor);

        int accountId = instructor.getAccountId();

        // creation of schedule
        int year = 2024;
        Schedule schedule = new Schedule(year);
        
        // save the schedule
        scheduleRepository.save(schedule);
        
        // creation of class type
        String className = "Yoga";
        String description = "A class to help you relax your mind and body!";
        boolean isAppoved = true;
        ClassType classType = new ClassType(className, description, isAppoved, "icon.png");

        // save the class type
        classTypeRepository.save(classType);

        // creation of the specific class
        String specificClassName = "Yoga 1A";
        String description2 = "A beginner's class for yoga!";
        Date date2 = Date.valueOf("2023-05-01");
        Time startTime = Time.valueOf("08:30:00");
        int hourDuration = 1;
        int maxCapacity = 20;
        int currentCapacity = 10;
        double registrationFee = 5.;
        
        // creation of the specific class
        SpecificClass specificClass = new SpecificClass();
        specificClass.setSpecificClassName(specificClassName);
        specificClass.setDescription(description2);
        specificClass.setDate(date2);
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

        // creation of the teaching info
        TeachingInfo teachingInfo = new TeachingInfo();
        teachingInfo.setInstructor(instructor);
        teachingInfo.setSpecificClass(specificClass);

        // save the teaching info
        teachingInfo = teachingInfoRepository.save(teachingInfo);
        int teachingInfoId = teachingInfo.getTeachingInfoId();

        // read teaching info from database
        teachingInfo = teachingInfoRepository.findTeachingInfoByTeachingInfoId(teachingInfoId);

        // assert equals
        assertNotNull(teachingInfo);
        assertEquals(accountId, teachingInfo.getInstructor().getAccountId());
        assertEquals(classId, teachingInfo.getSpecificClass().getClassId());
        assertEquals(className, teachingInfo.getSpecificClass().getClassType().getClassName());
        assertEquals(year, teachingInfo.getSpecificClass().getSchedule().getYear());

    }
}
