package ca.mcgill.ecse321.scs.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Test //it raises error since entity are different, so either override inside customer class or just check field
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