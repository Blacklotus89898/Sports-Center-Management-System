package ca.mcgill.ecse321.scs.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.scs.exception.SCSException;
import ca.mcgill.ecse321.scs.model.Schedule;
import ca.mcgill.ecse321.scs.model.SpecificClass;
import ca.mcgill.ecse321.scs.dao.SpecificClassRepository;
import ca.mcgill.ecse321.scs.model.ClassRegistration;
import ca.mcgill.ecse321.scs.model.ClassType;
import ca.mcgill.ecse321.scs.model.Customer;
import ca.mcgill.ecse321.scs.dao.ClassRegistrationRepository;
import ca.mcgill.ecse321.scs.dao.CustomerRepository;

@SuppressWarnings("null")
@SpringBootTest
public class ClassRegistrationServiceTests {
    @Mock
    private ClassRegistrationRepository classRegistrationRepository;
    @InjectMocks
    private ClassRegistrationService classRegistrationService;

    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerService customerService;

    @Mock
    private SpecificClassRepository specificClassRepository;
    @InjectMocks
    private SpecificClassService specificClassService;

    private Customer customer;
    private SpecificClass specificClass;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);                     // initializes mocks and injects them

        // manually inject the customerService and specificClassService into classRegistrationService if required
        classRegistrationService.setCustomerService(customerService);
        classRegistrationService.setSpecificClassService(specificClassService);

        int id1 = 1;
        Date date1 = Date.valueOf("2007-10-02");
        String name1 = "Bob";
        String email1 = "bob@gmail.com";
        String password1 = "bobpassword";

        Customer customer = new Customer(id1, date1, name1, email1, password1);
        this.customer = customer;

        int id3 = 2;
        Date date3 = Date.valueOf("2008-11-03");
        String name3 = "Jeremy";
        String email3 = "jeremy@gmail.com";
        String password3 = "jeremypassword";
        Customer customer2 = new Customer(id3, date3, name3, email3, password3);

        int id2 = 1234;
        String specificClassName = "Yoga with Bob";
        String description = "Bring your own mat";
        Date date2 = Date.valueOf("2020-01-01");
        Time startTime = Time.valueOf("10:00:00");
        int hourDuration = 3;
        int maxCapacity = 200;
        int currentCapacity = 50;
        double registrationFee = 10.00;
        ClassType classType = new ClassType("Yoga", "Come relax with some yoga", true);
        Schedule schedule = new Schedule(2020);
        SpecificClass specificClass = new SpecificClass(id2, specificClassName, description, date2, startTime, hourDuration, maxCapacity, currentCapacity, registrationFee, classType, schedule);
        this.specificClass = specificClass;

        when(customerRepository.findCustomerByAccountId(id1)).thenReturn(customer);
        when(customerRepository.findCustomerByAccountId(id3)).thenReturn(customer2);
        when(specificClassRepository.findSpecificClassByClassId(id2)).thenReturn(specificClass);
    }

    @Test
    public void testCreateValidClassRegistration() {
        // set up
        int accountId = 1;
        int specificClassId = 1234;

        when(classRegistrationRepository.save(any(ClassRegistration.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        ClassRegistration classRegistration = classRegistrationService.createClassRegistration(accountId, specificClassId);

        // assert
        assertNotNull(classRegistration);
        assertEquals(accountId, classRegistration.getCustomer().getAccountId());
        assertEquals(specificClassId, classRegistration.getSpecificClass().getClassId());

        verify(classRegistrationRepository, times(1)).save(any(ClassRegistration.class));
    }

    @Test
    public void testCreateInvalidClassRegistrationInvalidCustomer() {
        // customer is invalid

        // set up
        int accountId = -1;
        int specificClassId = 1234;

        when(classRegistrationRepository.save(any(ClassRegistration.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            classRegistrationService.createClassRegistration(accountId, specificClassId);
        });

        // assert
        assertEquals("Customer not found.", exception.getMessage());
    }

    @Test
    public void testCreateInvalidClassRegistrationInvalidSpecificClass() {
        // specific class is invalid

        // set up
        int accountId = 1;
        int specificClassId = -1;

        when(classRegistrationRepository.save(any(ClassRegistration.class))).thenAnswer((invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            classRegistrationService.createClassRegistration(accountId, specificClassId);
        });

        // assert
        assertEquals("Specific class with id " + specificClassId + " not found.", exception.getMessage());
    }

    @Test
    public void testUpdateValidClassRegistration() {
        // set up
        int registrationId = 5;
        int specificClassId = 1234;

        ClassRegistration classRegistration = new ClassRegistration(registrationId, this.customer, this.specificClass);
        when(classRegistrationRepository.findClassRegistrationByRegistrationId(registrationId)).thenReturn(classRegistration);
        when(classRegistrationRepository.save(any(ClassRegistration.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        ClassRegistration updatedClassRegistration = classRegistrationService.updateClassRegistration(registrationId, 2, specificClassId);

        // assert
        assertNotNull(updatedClassRegistration);
        assertEquals(registrationId, updatedClassRegistration.getRegistrationId());
        assertEquals(2, updatedClassRegistration.getCustomer().getAccountId());
        assertEquals(specificClassId, updatedClassRegistration.getSpecificClass().getClassId());

        verify(classRegistrationRepository, times(1)).save(any(ClassRegistration.class));
    
    }

    @Test
    public void testUpdateInvalidClassRegistrationInvalidRegistrationId() {
        // registration id is invalid

        // set up
        int registrationId = -1;
        int accountId = 1;
        int specificClassId = 1234;

        when(classRegistrationRepository.findClassRegistrationByRegistrationId(registrationId)).thenReturn(null);
        when(classRegistrationRepository.save(any(ClassRegistration.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            classRegistrationService.updateClassRegistration(registrationId, accountId, specificClassId);
        });

        // assert
        assertEquals("Class registration with id " + registrationId + " not found.", exception.getMessage());
    }

    @Test
    public void testDeleteClassRegistration() {
        // set up
        int registrationId = 5;

        ClassRegistration classRegistration = new ClassRegistration(registrationId, this.customer, this.specificClass);
        when(classRegistrationRepository.findClassRegistrationByRegistrationId(registrationId)).thenReturn(classRegistration).thenReturn(null);

        // act
        classRegistrationService.deleteClassRegistration(registrationId);
        verify(classRegistrationRepository, times(1)).delete(classRegistration);

        // act & assert exception
        Exception exception = assertThrows(SCSException.class, () -> {
            classRegistrationService.getClassRegistration(registrationId);
        });

        // assert
        assertEquals("Registration info with id " + registrationId + " not found.", exception.getMessage());
        verify(classRegistrationRepository, times(1)).delete(classRegistration);
    }

    @Test
    public void testUpdateClassRegistrationInvalidCustomer() {
        // trying to update with an invalid customer
        // set up
        int registrationId = 5;
        int specificClassId = 1234;

        ClassRegistration classRegistration = new ClassRegistration(registrationId, this.customer, this.specificClass);
        when(classRegistrationRepository.findClassRegistrationByRegistrationId(registrationId)).thenReturn(classRegistration);
        when(classRegistrationRepository.save(any(ClassRegistration.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            classRegistrationService.updateClassRegistration(registrationId, -1, specificClassId);
        });

        // assert
        assertEquals("Customer not found.", exception.getMessage());
    
    }

    @Test
    public void testGetAllClassRegistrations() {
        // set up
        int registrationId = 5;

        ClassRegistration classRegistration = new ClassRegistration(registrationId, this.customer, this.specificClass);
        ClassRegistration classRegistration2 = new ClassRegistration(6, customerService.getCustomerById(2), this.specificClass);
        when(classRegistrationRepository.findAll()).thenReturn(List.of(classRegistration, classRegistration2));

        // act
        List<ClassRegistration> classRegistrationsList = classRegistrationService.getAllClassRegistrations();

        // assert
        assertNotNull(classRegistrationsList);
        assertEquals(2, classRegistrationsList.size());
        assertTrue(classRegistrationsList.contains(classRegistration));
        assertTrue(classRegistrationsList.contains(classRegistration2));
    }

    @Test
    public void testUpdateClassRegistrationInvalidSpecificClass() {
        // trying to update with an invalid specific class
        // set up
        int registrationId = 5;
        int accountId = 1;

        ClassRegistration classRegistration = new ClassRegistration(registrationId, this.customer, this.specificClass);
        when(classRegistrationRepository.findClassRegistrationByRegistrationId(registrationId)).thenReturn(classRegistration);
        when(classRegistrationRepository.save(any(ClassRegistration.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            classRegistrationService.updateClassRegistration(registrationId, accountId, -1);
        });

        // assert
        assertEquals("Specific class with id -1 not found.", exception.getMessage());
    
    }

    @Test
    public void testCreateClassRegistrationInvalidCustomer(){
        // set up
        int accountId = -1;
        int specificClassId = 1234;

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            classRegistrationService.createClassRegistration(accountId, specificClassId);
        });

        // assert
        assertEquals("Customer not found.", exception.getMessage());
    
    }

    @Test
    public void testDeleteAllClassRegistrations(){
        // set up
        int registrationId = 5;

        ClassRegistration classRegistration = new ClassRegistration(registrationId, this.customer, this.specificClass);
        ClassRegistration classRegistration2 = new ClassRegistration(6, customerService.getCustomerById(2), this.specificClass);
        when(classRegistrationRepository.findAll()).thenReturn(List.of(classRegistration, classRegistration2));

        // act
        classRegistrationService.deleteAllClassRegistrations();
        verify(classRegistrationRepository, times(1)).deleteAll();
    
    }

    @Test
    public void testGetClassRegistration() {
        // set up
        int registrationId = 5;
        int accountId = 1;
        int specificClassId = 1234;

        ClassRegistration classRegistration = new ClassRegistration(registrationId, this.customer, this.specificClass);
        when(classRegistrationRepository.findClassRegistrationByRegistrationId(registrationId)).thenReturn(classRegistration);

        // act
        ClassRegistration returnedClassRegistration = classRegistrationService.getClassRegistration(registrationId);

        // assert
        assertNotNull(returnedClassRegistration);
        assertEquals(registrationId, returnedClassRegistration.getRegistrationId());
        assertEquals(accountId, returnedClassRegistration.getCustomer().getAccountId());
        assertEquals(specificClassId, returnedClassRegistration.getSpecificClass().getClassId());
    
    }

    @Test
    //test for getting the class registration for a specific class given the class id
    public void testGetClassRegistrationsByClassId() {
        // set up
        int registrationId = 5;
        int accountId = 1;
        int specificClassId = 1234;

        ClassRegistration classRegistration = new ClassRegistration(registrationId, new Customer(accountId, null, null, null, null), this.specificClass);
        ClassRegistration classRegistration1 = new ClassRegistration(registrationId + 1, new Customer(accountId + 1, null, null, null, null), this.specificClass);
        ClassRegistration classRegistration2 = new ClassRegistration(registrationId + 2, new Customer(accountId + 2, null, null, null, null), this.specificClass);
        when(classRegistrationRepository.findClassRegistrationsByClassId(specificClassId)).thenReturn(List.of(classRegistration, classRegistration1, classRegistration2));

        // act
        List<ClassRegistration> returnedClassRegistration = classRegistrationService.getClassRegistrationByClassId(specificClassId);

        // assert
        assertNotNull(returnedClassRegistration);
        assertEquals(3, returnedClassRegistration.size());
        assertTrue(returnedClassRegistration.contains(classRegistration));
        assertTrue(returnedClassRegistration.contains(classRegistration1));
        assertTrue(returnedClassRegistration.contains(classRegistration2));
    }

    @Test
    //test for invalid delete class registration because registrationId not found
    public void testDeleteClassRegistrationInvalidRegistrationId() {
        // set up
        int registrationId = 5;

        when(classRegistrationRepository.findClassRegistrationByRegistrationId(registrationId)).thenReturn(null);

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            classRegistrationService.deleteClassRegistration(registrationId);
        });

        // assert
        assertEquals("Class registration with id " + registrationId + " not found.", exception.getMessage());
    }

    @Test
    public void testCreateClassRegistrationClassFull() {
        // set up
        int accountId = 1;
        int specificClassId = 1234;

        SpecificClass specificClass = new SpecificClass(1234, "Yoga with Bob", "Bring your own mat", Date.valueOf("2020-01-01"), Time.valueOf("10:00:00"), 3, 50, 50, 10.00, new ClassType("Yoga", "Come relax with some yoga", true), new Schedule(2020));
        Customer customer = new Customer();
        when(customerRepository.findCustomerByAccountId(accountId)).thenReturn(customer);
        when(specificClassRepository.findSpecificClassByClassId(specificClassId)).thenReturn(specificClass);

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            classRegistrationService.createClassRegistration(accountId, specificClassId);
        });

        // assert
        assertEquals("Class with id " + specificClassId + " is full.", exception.getMessage());
    }

    @Test
    public void testUpdateClassRegistrationClassFull() {
        // set up
        int registrationId = 5;
        int specificClassId = 1234;

        SpecificClass specificClass = new SpecificClass(1234, "Yoga with Bob", "Bring your own mat", Date.valueOf("2020-01-01"), Time.valueOf("10:00:00"), 3, 50, 50, 10.00, new ClassType("Yoga", "Come relax with some yoga", true), new Schedule(2020));
        SpecificClass specificClass2 = new SpecificClass(1235, "Yoga with Amy", "Bring your own mat", Date.valueOf("2020-01-01"), Time.valueOf("10:00:00"), 3, 50, 50, 10.00, new ClassType("Yoga", "Come relax with some yoga", true), new Schedule(2020));
        
        Customer customer = new Customer();
        
        ClassRegistration classRegistration = new ClassRegistration(registrationId, customer, specificClass);
        
        when(classRegistrationRepository.findClassRegistrationByRegistrationId(registrationId)).thenReturn(classRegistration);
        when(specificClassRepository.findSpecificClassByClassId(specificClassId)).thenReturn(specificClass);
        when(specificClassRepository.findSpecificClassByClassId(specificClassId + 1)).thenReturn(specificClass2);

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            classRegistrationService.updateClassRegistration(registrationId, 2, specificClassId + 1);
        });

        // assert
        assertEquals("Class with id " + (specificClassId + 1) + " is full.", exception.getMessage());
    }

    @Test
    public void testCreateClassRegistrationCustomerAlreadyRegistered()  {
        // set up
        int accountId = 1;
        int specificClassId = 1234;
        int registrationId = 5;

        SpecificClass specificClass = new SpecificClass(1234, "Yoga with Bob", "Bring your own mat", Date.valueOf("2020-01-01"), Time.valueOf("10:00:00"), 3, 50, 15, 10.00, new ClassType("Yoga", "Come relax with some yoga", true), new Schedule(2020));
        Customer customer = new Customer(accountId, null, null, null, null);
        ClassRegistration classRegistration = new ClassRegistration(registrationId, customer, specificClass);
        
        when(customerRepository.findCustomerByAccountId(accountId)).thenReturn(customer);
        when(specificClassRepository.findSpecificClassByClassId(specificClassId)).thenReturn(specificClass);
        when(classRegistrationRepository.findAll()).thenReturn(List.of(classRegistration));

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            classRegistrationService.createClassRegistration(accountId, specificClassId);
        });

        // assert
        assertEquals("Customer with id " + accountId + " is already registered for class with id " + specificClassId + ".", exception.getMessage());
    }

}