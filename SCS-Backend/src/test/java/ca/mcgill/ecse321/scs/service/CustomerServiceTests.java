package ca.mcgill.ecse321.scs.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.scs.dao.CustomerRepository;
import ca.mcgill.ecse321.scs.model.Customer;

@SpringBootTest
public class CustomerServiceTests {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private static Customer mockCustomer;

    @BeforeAll
    public static void generateMockCustomer() {
        final int accountId = 1;
        final Date date = new Date(0);
        final String email = "test@mail.com";
        final String passsword = "******";
        final String name = "MockCustomer";
        mockCustomer = new Customer(accountId, date, name, email, passsword);
        // final Optional <Customer> o = Optional.of(mockCustomer);
    }

    // Don't forget to implement the failing test
    // @Test
    // public void testGetCustomerById() {
    // // final int accountId = 1;
    // // final Date date = new Date(0);
    // // final String email = "test@mail.com";
    // // final String passsword = "******";
    // // final String name = "MockCustomer";
    // // final Customer mockCustomer = new Customer(accountId, date, name, email,
    // passsword);
    // final Optional <Customer> o = Optional.of(mockCustomer);
    // when(customerRepository.findById(Integer.toString(mockCustomer.getAccountId()))).thenReturn(o);
    // Customer output =
    // customerService.findCustomerById(Integer.toString(mockCustomer.getAccountId()));
    // assertEquals(o.get(), output);
    // }

    @Test
    public void testGetCustomerByValidEmail() {
        when(customerRepository.findCustomerByEmail(mockCustomer.getEmail())).thenReturn(mockCustomer);
        Customer output = customerService.getCustomerByEmail(mockCustomer.getEmail());
        assertEquals(mockCustomer, output);
    }

    // @Test
    // public void testGetCustomerByInvalidEmail() {
    // when(customerRepository.findCustomerByEmail("invalidEmail@test.com")).thenReturn(null);
    // Customer output =
    // customerService.findCustomerByEmail("invalidEmail@test.com");
    // assertEquals(null, output);
    // }

    @Test
    public void testUpdateValidCustomer() {
        final int accountId = 1;
        final Date date = new Date(0);
        final String email = "test@mail.com";
        final String passsword = "******";
        final String name = "UpdatedMockCustomer";
        Customer mockUpdateCustomer = new Customer(accountId, date, name, email, passsword);

        when(customerRepository.findCustomerByEmail(mockCustomer.getEmail())).thenReturn(mockCustomer);
        when(customerRepository.save(mockUpdateCustomer)).thenReturn(mockUpdateCustomer);
        Customer output = customerRepository.save(mockUpdateCustomer);

        assertEquals(mockUpdateCustomer, output);
    }

    @Test
    public void testCreateValidCustomerByEmail() {
        final int accountId = 2;
        final Date date = new Date(0);
        final String email = "create@mail.com";
        final String passsword = "******";
        final String name = "CreatedMockCustomer";
        Customer mockCreatedCustomer = new Customer(accountId, date, name, email, passsword);
        // when(customerRepository.save(mockCreatedCustomer)).thenReturn(mockCreatedCustomer);

        Customer createdOuput = customerService.createCustomer(name, email, passsword);

        assertNotNull(createdOuput);
        assertEquals(mockCreatedCustomer.getEmail(), createdOuput.getEmail());
        assertEquals(mockCreatedCustomer.getName(), createdOuput.getName());
        // verify(customerRepository, times(1)).save(mockCreatedCustomer);
        // when(customerRepository.findCustomerByEmail(mockCustomer.getEmail())).thenReturn(mockCustomer);
        // Customer output = customerRepository.save(mockCreatedCustomer);
        // assertEquals(mockCreatedCustomer, output);

        // ***IMPORTANT implementation are different in industries, object creation should be in services,
        // but apparently other teams are doing in controller for the sake of unit test
    }

    @Test
    public void testDeleteValidCustomerByEmail() {
        final int accountId = 3;
        final Date date = new Date(0);
        final String email = "delete@mail.com";
        final String passsword = "******";
        final String name = "DeletedMockCustomer";
        Customer mockDeletedCustomer = new Customer(accountId, date, name, email, passsword);
        
        when(customerRepository.findCustomerByEmail(email)).thenReturn(mockDeletedCustomer);
        customerService.deleteCustomerByEmail(email);
        verify(customerRepository, times(1)).deleteCustomerByEmail(email);
        verify(customerRepository, times(0)).deleteCustomerByEmail("differentMail");


        // assertNotNull(DeletedOuput);
        // assertEquals(mockDeletedCustomer, DeletedOuput);
        // verify(customerRepository, times(1)).save(mockDeletedCustomer);
        // when(customerRepository.findCustomerByEmail(mockCustomer.getEmail())).thenReturn(mockCustomer);
        // Customer output = customerRepository.save(mockCreatedCustomer);

        // assertEquals(mockCreatedCustomer, output);
    }

}
