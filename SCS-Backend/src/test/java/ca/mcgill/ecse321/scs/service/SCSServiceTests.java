package ca.mcgill.ecse321.scs.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.scs.exception.SCSException;
import ca.mcgill.ecse321.scs.model.Customer;
import ca.mcgill.ecse321.scs.model.Owner;
import ca.mcgill.ecse321.scs.model.Instructor;
import ca.mcgill.ecse321.scs.dao.CustomerRepository;
import ca.mcgill.ecse321.scs.dao.InstructorRepository;
import ca.mcgill.ecse321.scs.dao.OwnerRepository;

@SuppressWarnings("null")
@SpringBootTest
public class SCSServiceTests {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private InstructorRepository instructorRepository;
    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private SCSService service;

    Customer customer;
    Instructor instructor;
    Owner owner;

    @BeforeEach
    public void setMockOutput() {
        Date creationDate = Date.valueOf("2021-03-01");
        String nameCustomer = "Customer Name";
        String nameInstructor = "Instructor Name";
        String nameOwner = "Owner Name";
        String customerEmail = "customer@sports.center";
        String instructorEmail = "instructor@sports.center";
        String ownerEmail = "owner@sports.center";
        String password = "password";

        this.customer = new Customer();
        customer.setName(nameCustomer);
        customer.setEmail(customerEmail);
        customer.setPassword(password);
        customer.setCreationDate(creationDate);

        this.instructor = new Instructor();
        instructor.setName(nameInstructor);
        instructor.setEmail(instructorEmail);
        instructor.setPassword(password);
        instructor.setCreationDate(creationDate);

        this.owner = new Owner();
        owner.setName(nameOwner);
        owner.setEmail(ownerEmail);
        owner.setPassword(password);
        owner.setCreationDate(creationDate);

        when(customerRepository.findCustomerByEmailAndPassword(customerEmail, password)).thenReturn(customer);
        when(instructorRepository.findInstructorByEmailAndPassword(instructorEmail, password)).thenReturn(instructor);
        when(ownerRepository.findOwnerByEmailAndPassword(ownerEmail, password)).thenReturn(owner);
    }

    @Test
    public void testLoginCustomer() {
        // set up
        String email = customer.getEmail();
        String password = customer.getPassword();

        // act
        Customer account = (Customer) service.login(email, password);

        // assert
        assertNotNull(account);
        assertEquals(customer.getEmail(), account.getEmail());
        assertEquals(customer.getPassword(), account.getPassword());
        assertEquals(customer.getName(), account.getName());
        assertEquals(customer.getCreationDate(), account.getCreationDate());
        verify(customerRepository, times(1)).findCustomerByEmailAndPassword(email, password);
    }

    @Test
    public void testLoginInstructor() {
        // set up
        String email = instructor.getEmail();
        String password = instructor.getPassword();

        // act
        Instructor account = (Instructor) service.login(email, password);

        // assert
        assertNotNull(account);
        assertEquals(instructor.getEmail(), account.getEmail());
        assertEquals(instructor.getPassword(), account.getPassword());
        assertEquals(instructor.getName(), account.getName());
        assertEquals(instructor.getCreationDate(), account.getCreationDate());
        verify(instructorRepository, times(1)).findInstructorByEmailAndPassword(email, password);
    }

    @Test
    public void testLoginOwner() {
        // set up
        String email = owner.getEmail();
        String password = owner.getPassword();

        // act
        Owner account = (Owner) service.login(email, password);

        // assert
        assertNotNull(account);
        assertEquals(owner.getEmail(), account.getEmail());
        assertEquals(owner.getPassword(), account.getPassword());
        assertEquals(owner.getName(), account.getName());
        assertEquals(owner.getCreationDate(), account.getCreationDate());
        verify(ownerRepository, times(1)).findOwnerByEmailAndPassword(email, password);
    }

    @Test
    public void testLoginNullEmail() {
        // set up
        String email = null;
        String password = customer.getPassword();

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            service.login(email, password);
        });

        // assert
        assertEquals("Email cannot be empty.", exception.getMessage());
        verify(customerRepository, times(0)).findCustomerByEmailAndPassword(email, password);
    }

    @Test
    public void testLoginNullPassword() {
        // set up
        String email = customer.getEmail();
        String password = null;

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            service.login(email, password);
        });

        // assert
        assertEquals("Password cannot be empty.", exception.getMessage());
        verify(customerRepository, times(0)).findCustomerByEmailAndPassword(email, password);
    }

    @Test
    public void testLoginInvalidCredentials() {
        // set up
        String email = "random@gmail.com";
        String password = "random";

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            service.login(email, password);
        });

        // assert
        assertEquals("Please verify that your email and password is correct.", exception.getMessage());
        verify(customerRepository, times(1)).findCustomerByEmailAndPassword(email, password);
        verify(instructorRepository, times(1)).findInstructorByEmailAndPassword(email, password);
        verify(ownerRepository, times(1)).findOwnerByEmailAndPassword(email, password);
    }
}
