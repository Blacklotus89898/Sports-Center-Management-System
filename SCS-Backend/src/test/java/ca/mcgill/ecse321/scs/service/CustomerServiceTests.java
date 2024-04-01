package ca.mcgill.ecse321.scs.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ca.mcgill.ecse321.scs.dao.CustomerRepository;
import ca.mcgill.ecse321.scs.dao.InstructorRepository;
import ca.mcgill.ecse321.scs.dao.OwnerRepository;
import ca.mcgill.ecse321.scs.exception.SCSException;
import ca.mcgill.ecse321.scs.model.Customer;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.springframework.boot.test.context.SpringBootTest;


/**
 * This class contains the unit tests for the CustomerService class.
 * It tests various functionalities of the CustomerService class, such as creating a customer,
 * retrieving a customer by ID, updating a customer, and more.
 */
@SpringBootTest
public class CustomerServiceTests {
    @Mock
    private InstructorRepository instructorRepository;
    @Mock
    private OwnerRepository ownerRepository;
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    Customer customer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        int id = 1;
        String email = "customer@sports.center";
        String password = "password";
        String name = "Customer Name";
        Date creationDate = Date.valueOf(LocalDate.now());
        
        customer = new Customer();
        customer.setAccountId(id);
        customer.setEmail(email);
        customer.setPassword(password);
        customer.setName(name);
        customer.setCreationDate(creationDate);

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerRepository.findCustomerByEmailAndPassword(email, password)).thenReturn(customer);
        when(customerRepository.findCustomerByAccountId(id)).thenReturn(customer);
    }

    @Test
    public void testCreateCustomer() {
        // set up
        String email = customer.getEmail();
        String password = customer.getPassword();
        String name = customer.getName();

        Customer createdCustomer = null;

        when(customerRepository.findCustomerByEmail(email)).thenReturn(null);
        when(instructorRepository.findInstructorByEmail(email)).thenReturn(null);
        when(ownerRepository.findOwnerByEmail(email)).thenReturn(null);

        // act
        createdCustomer = customerService.createCustomer(name, email, password);

        // assert
        assertNotNull(createdCustomer);
        assertEquals(customer.getEmail(), createdCustomer.getEmail());
        assertEquals(customer.getPassword(), createdCustomer.getPassword());
        assertEquals(customer.getName(), createdCustomer.getName());
    }

    @Test
    public void testCreateCustomerNullEmail() {
        // set up
        String email = null;
        String password = customer.getPassword();
        String name = customer.getName();

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            customerService.createCustomer(name, email, password);
        });

        // assert
        assertEquals("Email cannot be empty.", exception.getMessage());
    }

    @Test
    public void testCreateCustomerInvalidEmail() {
        // set up
        String email = "invalid email";
        String password = customer.getPassword();
        String name = customer.getName();

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            customerService.createCustomer(name, email, password);
        });

        // assert
        assertEquals("Email is not valid.", exception.getMessage());
    }

    @Test
    public void testCreateCustomerNullPassword() {
        // set up
        String email = customer.getEmail();
        String password = null;
        String name = customer.getName();

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            customerService.createCustomer(name, email, password);
        });

        // assert
        assertEquals("Password cannot be empty.", exception.getMessage());
    }

    @Test
    public void testCreateCustomerNullName() {
        // set up
        String email = customer.getEmail();
        String password = customer.getPassword();
        String name = null;

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            customerService.createCustomer(name, email, password);
        });

        // assert
        assertEquals("Name cannot be empty.", exception.getMessage());
    }

    @Test
    public void testCreateCustomerExistingAccount() {
        // set up
        String email = customer.getEmail();
        String password = customer.getPassword();
        String name = customer.getName();

        when(customerRepository.findCustomerByEmail(email)).thenReturn(customer);

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            customerService.createCustomer(name, email, password);
        });

        // assert
        assertEquals("An account with this email already exists.", exception.getMessage());
    }

    @Test
    public void testGetCustomerById() {
        // set up
        int id = customer.getAccountId();

        Customer foundCustomer = null;

        // act
        foundCustomer = customerService.getCustomerById(id);

        // assert
        assertNotNull(foundCustomer);
        assertEquals(customer.getAccountId(), foundCustomer.getAccountId());
        assertEquals(customer.getEmail(), foundCustomer.getEmail());
        assertEquals(customer.getPassword(), foundCustomer.getPassword());
        assertEquals(customer.getName(), foundCustomer.getName());
        assertEquals(customer.getCreationDate(), foundCustomer.getCreationDate());
    }

    @Test
    public void testGetCustomerByIdNotFound() {
        // set up
        int id = 2;

        when(customerRepository.findCustomerByAccountId(id)).thenReturn(null);

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            customerService.getCustomerById(id);
        });

        // assert
        assertEquals("Customer not found.", exception.getMessage());
    }

    @Test
    public void testGetAllCustomers() {
        // set up
        List<Customer> customers = Collections.singletonList(customer);

        when(customerRepository.findAll()).thenReturn(customers);

        // act
        List<Customer> foundCustomers = customerService.getAllCustomer();

        // assert
        assertNotNull(foundCustomers);
        assertEquals(customers.size(), foundCustomers.size());
        assertEquals(customers.get(0).getAccountId(), foundCustomers.get(0).getAccountId());
        assertEquals(customers.get(0).getEmail(), foundCustomers.get(0).getEmail());
        assertEquals(customers.get(0).getPassword(), foundCustomers.get(0).getPassword());
        assertEquals(customers.get(0).getName(), foundCustomers.get(0).getName());
        assertEquals(customers.get(0).getCreationDate(), foundCustomers.get(0).getCreationDate());
    }

    @Test
    public void testUpdateCustomer() {
        // set up
        int id = customer.getAccountId();
        String email = "random@sports.center";
        String password = "new password";
        String name = "New Name";

        Customer updatedCustomer = null;

        when(customerRepository.findCustomerByAccountId(id)).thenReturn(customer);

        // act
        updatedCustomer = customerService.updateCustomerById(id, name, email, password);

        // assert
        assertNotNull(updatedCustomer);
        assertEquals(customer.getAccountId(), updatedCustomer.getAccountId());
        assertEquals(email, updatedCustomer.getEmail());
        assertEquals(password, updatedCustomer.getPassword());
        assertEquals(name, updatedCustomer.getName());
    }

    @Test
    public void testUpdateCustomerNotFound() {
        // set up
        int id = 2;
        String email = "any@sports.center";
        String password = "new password";
        String name = "New Name";

        when(customerRepository.findCustomerByAccountId(id)).thenReturn(null);

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            customerService.updateCustomerById(id, name, email, password);
        });

        // assert
        assertEquals("Customer not found.", exception.getMessage());
    }

    @Test
    public void testUpdateCustomerInvalidEmail() {
        // set up
        int id = customer.getAccountId();
        String email = "invalid email";
        String password = customer.getPassword();
        String name = customer.getName();

        when(customerRepository.findCustomerByAccountId(id)).thenReturn(customer);

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            customerService.updateCustomerById(id, name, email, password);
        });

        // assert
        assertEquals("Email is not valid.", exception.getMessage());
    }

    @Test
    public void testUpdateCustomerNullPassword() {
        // set up
        int id = customer.getAccountId();
        String email = customer.getEmail();
        String password = null;
        String name = customer.getName();

        when(customerRepository.findCustomerByAccountId(id)).thenReturn(customer);

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            customerService.updateCustomerById(id, name, email, password);
        });

        // assert
        assertEquals("Password cannot be empty.", exception.getMessage());
    }

    @Test
    public void testUpdateCustomerNullName() {
        // set up
        int id = customer.getAccountId();
        String email = customer.getEmail();
        String password = customer.getPassword();
        String name = null;

        when(customerRepository.findCustomerByAccountId(id)).thenReturn(customer);

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            customerService.updateCustomerById(id, name, email, password);
        });

        // assert
        assertEquals("Name cannot be empty.", exception.getMessage());
    }

    @Test
    public void testDeleteCustomer() {
        // set up
        int id = customer.getAccountId();

        when(customerRepository.findCustomerByAccountId(id)).thenReturn(customer);

        // act
        customerService.deleteCustomerById(id);

        // assert
        verify(customerRepository, times(1)).delete(customer);
    }

    @Test
    public void testDeleteCustomerNotFound() {
        // set up
        int id = 2;

        when(customerRepository.findCustomerByAccountId(id)).thenReturn(null);

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            customerService.deleteCustomerById(id);
        });

        // assert
        assertEquals("Customer not found.", exception.getMessage());
    }

    @Test
    public void testDeleteAllCustomers() {
        // set up
        List<Customer> customers = Collections.singletonList(customer);

        when(customerRepository.findAll()).thenReturn(customers);

        // act
        customerService.deleteAllCustomers();

        // assert
        verify(customerRepository, times(1)).deleteAll();
    }
}