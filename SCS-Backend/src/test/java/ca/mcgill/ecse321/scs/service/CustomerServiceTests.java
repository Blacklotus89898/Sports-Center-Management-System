package ca.mcgill.ecse321.scs.service;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertThrows;
// import static org.mockito.ArgumentMatchers.argThat;
// import static org.mockito.Mockito.times;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;

// import java.sql.Date;

// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.springframework.boot.test.context.SpringBootTest;

// import ca.mcgill.ecse321.scs.dao.CustomerRepository;
// import ca.mcgill.ecse321.scs.model.Customer;
// import jakarta.persistence.EntityExistsException;
// import jakarta.persistence.EntityNotFoundException;

// @SpringBootTest
// public class CustomerServiceTests {

//     @Mock
//     private CustomerRepository customerRepository;

//     @InjectMocks
//     private CustomerService customerService;

//     private static Customer mockCustomer;

//     @BeforeAll
//     public static void generateMockCustomer() {
//         final int accountId = 1;
//         final Date date = new Date(0);
//         final String email = "test@mail.com";
//         final String passsword = "******";
//         final String name = "MockCustomer";
//         mockCustomer = new Customer(accountId, date, name, email, passsword);
//         // final Optional <Customer> o = Optional.of(mockCustomer);
//     }

//     // Don't forget to implement the failing test
//     // @Test
//     // public void testGetCustomerById() {
//     // // final int accountId = 1;
//     // // final Date date = new Date(0);
//     // // final String email = "test@mail.com";
//     // // final String passsword = "******";
//     // // final String name = "MockCustomer";
//     // // final Customer mockCustomer = new Customer(accountId, date, name, email,
//     // passsword);
//     // final Optional <Customer> o = Optional.of(mockCustomer);
//     // when(customerRepository.findById(Integer.toString(mockCustomer.getAccountId()))).thenReturn(o);
//     // Customer output =
//     // customerService.findCustomerById(Integer.toString(mockCustomer.getAccountId()));
//     // assertEquals(o.get(), output);
//     // }


//     @Test
//     public void testGetCustomerByValidEmail() {
//         when(customerRepository.findCustomerByEmail(mockCustomer.getEmail())).thenReturn(mockCustomer);
//         Customer output = customerService.getCustomerByEmail(mockCustomer.getEmail());
//         assertEquals(mockCustomer, output);
//     }

//     @Test
//     public void testGetCustomerByInvalidEmail() {
//         final String invalidEmail = "invalid@mail.com";
//         when(customerRepository.findCustomerByEmail(invalidEmail)).thenReturn(null);
//         EntityNotFoundException e = assertThrows(EntityNotFoundException.class,
//         () -> customerService.getCustomerByEmail(invalidEmail));
//         // commented test broken since there are no error catching in the service unless implementation of custom exceptions
//         // Customer output = customerService.getCustomerByEmail("invalidEmail@test.com"); 
//         // assertEquals(null, output);
//         assertEquals(e.getMessage(), String.format("Customer not found with email: %s", invalidEmail)); //will need custom exceptions
//     }

//     @Test
//     public void testUpdateValidCustomer() {
//         final int accountId = 1;
//         final Date date = new Date(0);
//         final String email = "test@mail.com";
//         final String passsword = "******";
//         final String name = "UpdatedMockCustomer";
//         Customer mockUpdateCustomer = new Customer(accountId, date, name, email, passsword);

//         when(customerRepository.findCustomerByEmail(email)).thenReturn(mockCustomer);
//         when(customerRepository.save(mockCustomer)).thenReturn(mockUpdateCustomer);
        
//         Customer output = customerService.updateCustomerById(mockUpdateCustomer);
//         assertEquals(mockUpdateCustomer.getEmail(), output.getEmail());
//         assertEquals(mockUpdateCustomer.getName(), output.getName());
//     }

//     @Test
//     public void testUpdateInvalidEmailCustomer() { //already exist, invalid inputs, etc, check for nested test possibility
//         final int accountId = 1;
//         final Date date = new Date(0);
//         final String invalidEmail = "invalid@mail.com";
//         // final String email = "test@mail.com";
//         final String passsword = "******";
//         final String name = "UpdatedMockCustomer";
//         Customer mockUpdateCustomer = new Customer(accountId, date, name, invalidEmail, passsword);

//         when(customerRepository.findCustomerByEmail(invalidEmail)).thenReturn(null);
//         when(customerRepository.save(mockUpdateCustomer)).thenReturn(mockUpdateCustomer);
//         // Customer output = customerService.updateCustomerByEmail(mockUpdateCustomer);

//         EntityNotFoundException e = assertThrows(EntityNotFoundException.class, () -> customerService.updateCustomerByEmail(mockUpdateCustomer));
//         assertEquals(e.getMessage(), String.format("Customer not found with email: %s", invalidEmail)); //will need custom exceptions
//         // assertEquals(mockUpdateCustomer, output);
//     }

//     @Test
//     public void testCreateCustomerByValidEmail() {
//         final int accountId = 2;
//         final Date date = new Date(0);
//         final String email = "create@mail.com";
//         final String passsword = "******";
//         final String name = "CreatedMockCustomer";
//         Customer mockCreatedCustomer = new Customer(accountId, date, name, email, passsword);
//         when(customerRepository.findCustomerByEmail(email)).thenReturn(null);

//         // when(customerRepository.save(mockCreatedCustomer)).thenReturn(mockCreatedCustomer);

//         Customer createdOuput = customerService.createCustomer(name, email, passsword);

//         assertNotNull(createdOuput);
//         assertEquals(mockCreatedCustomer.getEmail(), createdOuput.getEmail());
//         assertEquals(mockCreatedCustomer.getName(), createdOuput.getName());
//         // verify(customerRepository, times(1)).save(mockCreatedCustomer);
//         // when(customerRepository.findCustomerByEmail(mockCustomer.getEmail())).thenReturn(mockCustomer);
//         // Customer output = customerRepository.save(mockCreatedCustomer);
//         // assertEquals(mockCreatedCustomer, output);

//         // ***IMPORTANT implementation are different in industries, object creation
//         // should be in services,
//         // but apparently other teams are doing in controller for the sake of unit test
//     }
//     @Test
//     public void testCreateDuplicateCustomerByEmail() {
//         final int accountId = 2;
//         final Date date = new Date(0);
//         final String email = "create@mail.com";
//         final String passsword = "******";
//         final String name = "CreatedMockCustomer";
//         Customer mockCreatedCustomer = new Customer(accountId, date, name, email, passsword);
//         // when(customerRepository.save(mockCreatedCustomer)).thenReturn(mockCreatedCustomer);
//         when(customerRepository.findCustomerByEmail(email)).thenReturn(mockCreatedCustomer);

//         EntityExistsException e = assertThrows(EntityExistsException.class, () -> customerService.createCustomer(name, email, passsword));
//         assertEquals(e.getMessage(), String.format("Email account already exists: %s", email)); //will need custom exceptions
        
//         // Customer createdOuput = customerService.createCustomer(name, email, passsword);

//         // assertNotNull(createdOuput);
//         // assertEquals(mockCreatedCustomer.getEmail(), createdOuput.getEmail());
//         // assertEquals(mockCreatedCustomer.getName(), createdOuput.getName());
//         // verify(customerRepository, times(1)).save(mockCreatedCustomer);
//         // when(customerRepository.findCustomerByEmail(mockCustomer.getEmail())).thenReturn(mockCustomer);
//         // Customer output = customerRepository.save(mockCreatedCustomer);
//         // assertEquals(mockCreatedCustomer, output);
//     }
//     @Test
//     public void testCreateInvalidPasswordCustomerByEmail() {
//         final int accountId = 2;
//         final Date date = new Date(0);
//         final String email = "create@mail.com";
//         final String password = new String(new char[300]).replace("\0", "*");
//         final String name = "CreatedMockCustomer";
//         Customer mockCreatedCustomer = new Customer(accountId, date, name, email, password);
//         // when(customerRepository.save(mockCreatedCustomer)).thenReturn(mockCreatedCustomer);
//         when(customerRepository.findCustomerByEmail(email)).thenReturn(mockCreatedCustomer);

//         IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> customerService.createCustomer(name, email, password));
//         assertEquals(e.getMessage(), String.format("Invalid password: %s", password)); //will need custom exceptions
        
//         // Customer createdOuput = customerService.createCustomer(name, email, passsword);

//         // assertNotNull(createdOuput);
//         // assertEquals(mockCreatedCustomer.getEmail(), createdOuput.getEmail());
//         // assertEquals(mockCreatedCustomer.getName(), createdOuput.getName());
//         // verify(customerRepository, times(1)).save(mockCreatedCustomer);
//         // when(customerRepository.findCustomerByEmail(mockCustomer.getEmail())).thenReturn(mockCustomer);
//         // Customer output = customerRepository.save(mockCreatedCustomer);
//         // assertEquals(mockCreatedCustomer, output);
//     }



//     @Test
//     public void testDeleteCustomerByValidEmail() {
//         final int accountId = 3;
//         final Date date = new Date(0);
//         final String email = "delete@mail.com";
//         final String passsword = "******";
//         final String name = "DeletedMockCustomer";
//         Customer mockDeletedCustomer = new Customer(accountId, date, name, email, passsword);

//         when(customerRepository.findCustomerByEmail(email)).thenReturn(mockDeletedCustomer);
//         customerService.deleteCustomerByEmail(email);
//         verify(customerRepository, times(1)).delete(argThat((Customer e) -> email.equals(e.getEmail())));
//         verify(customerRepository, times(0)).delete(argThat((Customer e) -> !email.equals(e.getEmail())));

//         // assertNotNull(DeletedOuput);
//         // assertEquals(mockDeletedCustomer, DeletedOuput);
//         // verify(customerRepository, times(1)).save(mockDeletedCustomer);
//         // when(customerRepository.findCustomerByEmail(mockCustomer.getEmail())).thenReturn(mockCustomer);
//         // Customer output = customerRepository.save(mockCreatedCustomer);

//         // assertEquals(mockCreatedCustomer, output);
//     }

//     @Test
//     public void testDeleteCustomerByInvalidEmail() {
//         // final int accountId = 3;
//         // final Date date = new Date(0);
//         // final String email = "delete@mail.com";
//         // final String passsword = "******";
//         // final String name = "DeletedMockCustomer";
//         // Customer mockDeletedCustomer = new Customer(accountId, date, name, email, passsword);
//         final String invalidEmail = "invalid@mail.com";

//         when(customerRepository.findCustomerByEmail(invalidEmail)).thenReturn(null);

//         EntityNotFoundException e = assertThrows(EntityNotFoundException.class,
//         () -> customerService.deleteCustomerByEmail(invalidEmail));
//         //need the check status code also eventually
//         assertEquals(e.getMessage(), String.format("Customer not found with email: %s", invalidEmail)); //will need custom exceptions
//         // assertNotNull(DeletedOuput);
//         // assertEquals(mockDeletedCustomer, DeletedOuput);
//         // verify(customerRepository, times(1)).save(mockDeletedCustomer);
//         // when(customerRepository.findCustomerByEmail(mockCustomer.getEmail())).thenReturn(mockCustomer);
//         // Customer output = customerRepository.save(mockCreatedCustomer);

//         // assertEquals(mockCreatedCustomer, output);
//     }

// }

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.http.HttpStatus;

import ca.mcgill.ecse321.scs.dao.CustomerRepository;
import ca.mcgill.ecse321.scs.exception.SCSException;
import ca.mcgill.ecse321.scs.model.Customer;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTests {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;


    @Test
    public void testGetAllCustomer() {
        when(customerRepository.findAll()).thenReturn(Arrays.asList(new Customer(), new Customer()));
        assertEquals(2, customerService.getAllCustomer().size());
    }

    @Test
    public void testGetCustomerById() {
        Customer customer = new Customer();
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(customer));
        assertEquals(customer, customerService.getCustomerById(1));
    }

    @Test
    public void testGetCustomerByIdNotFound() {
        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(SCSException.class, () -> customerService.getCustomerById(1));
    }

    @Test
    public void testGetCustomerByEmail() {
        Customer customer = new Customer();
        when(customerRepository.findCustomerByEmail(anyString())).thenReturn(customer);
        assertEquals(customer, customerService.getCustomerByEmail("test@test.com"));
    }

    @Test
    public void testGetCustomerByEmailNotFound() {
        when(customerRepository.findCustomerByEmail(anyString())).thenReturn(null);
        assertThrows(SCSException.class, () -> customerService.getCustomerByEmail("test@test.com"));
    }

    @Test //raise error since entity are different so either override inside customer class or just check field
    public void testCreateCustomer() {
        Customer customer = new Customer(0, null, "test", "test@test.com", "password");
        when(customerRepository.findCustomerByEmail(anyString())).thenReturn(null);
        when(customerRepository.save(any())).thenReturn(customer);
        Customer result = customerService.createCustomer("test", "test@test.com", "password");
        assertEquals(customer.getEmail(), result.getEmail());
        assertEquals(customer.getAccountId(), result.getAccountId());
        assertEquals(customer.getName(), result.getName());
        assertEquals(customer.getPassword(), result.getPassword());
    }

    @Test
    public void testCreateCustomerEmailExists() {
        when(customerRepository.findCustomerByEmail(anyString())).thenReturn(new Customer());
        assertThrows(SCSException.class, () -> customerService.createCustomer("test", "test@test.com", "password"));
    }

    @Test
    public void testUpdateCustomerById() {
        Customer customer = new Customer();
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(customer));
        when(customerRepository.save(any())).thenReturn(customer);
        assertEquals(customer, customerService.updateCustomerById(1, "test", "test@test.com", "password"));
    }

    @Test
    public void testUpdateCustomerByIdNotFound() {
        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(SCSException.class, () -> customerService.updateCustomerById(1, "test", "test@test.com", "password"));
    }

    @Test
    public void testDeleteCustomerById() {
        Customer customer = new Customer();
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(customer));
        doNothing().when(customerRepository).delete(any());
        assertDoesNotThrow(() -> customerService.deleteCustomerById(1));
    }

    @Test
    public void testDeleteCustomerByIdNotFound() {
        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(SCSException.class, () -> customerService.deleteCustomerById(1));
    }
}