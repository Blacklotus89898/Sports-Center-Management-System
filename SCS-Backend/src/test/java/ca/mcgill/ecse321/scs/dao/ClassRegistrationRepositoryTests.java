package ca.mcgill.ecse321.scs.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.sql.Time;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.scs.model.Customer;
import ca.mcgill.ecse321.scs.model.Schedule;
import ca.mcgill.ecse321.scs.model.SpecificClass;
import ca.mcgill.ecse321.scs.model.ClassRegistration;
import ca.mcgill.ecse321.scs.model.ClassType;

@SpringBootTest
public class ClassRegistrationRepositoryTests {
    @Autowired
    private ClassRegistrationRepository classRegistrationRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private SpecificClassRepository specificClassRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private ClassTypeRepository classTypeRepository;

    @AfterEach
    public void clearDatabase() {
        classRegistrationRepository.deleteAll();
        customerRepository.deleteAll();
        specificClassRepository.deleteAll();
        scheduleRepository.deleteAll();
        classTypeRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadClassRegistration() {
        // creation of the customer
        String name = "Koko"; 
        Date date = new Date(0);
        String email = "Kolo@gmail.com";
        String password = "secret";

        Customer customer = new Customer();
        customer.setCreationDate(date);
        customer.setEmail(email);
        customer.setName(name);
        customer.setPassword(password);

        customer = customerRepository.save(customer);

        int accountId = customer.getAccountId();

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

        // creation of the class registration
        ClassRegistration classRegistration = new ClassRegistration();
        classRegistration.setCustomer(customer);
        classRegistration.setSpecificClass(specificClass);

        // save the class registration
        classRegistration = classRegistrationRepository.save(classRegistration);
        int registrationId = classRegistration.getRegistrationId();

        // read class registration from database
        classRegistration = classRegistrationRepository.findClassRegistrationByRegistrationId(registrationId);

        // assert equals
        assertNotNull(classRegistration);
        assertEquals(accountId, classRegistration.getCustomer().getAccountId());
        assertEquals(classId, classRegistration.getSpecificClass().getClassId());
        assertEquals(className, classRegistration.getSpecificClass().getClassType().getClassName());
        assertEquals(year, classRegistration.getSpecificClass().getSchedule().getYear());
    }
}