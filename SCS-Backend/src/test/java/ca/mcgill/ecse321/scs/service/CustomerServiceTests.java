package ca.mcgill.ecse321.scs.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.Optional;

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

    @Test
    public void testGetCustomerById() {
        final int accountId = 1;
        final Date date = new Date(0);
        final String email = "test@mail.com";
        final String passsword = "******";
        final String name = "MockCustomer";
        final Customer mockCustomer = new Customer(accountId, date, name, email, passsword);
        final Optional <Customer> o = Optional.of(mockCustomer);

        when(customerRepository.findById(Integer.toString(accountId))).thenReturn(o);
        
        Customer output = customerService.findCustomerById(Integer.toString(accountId));
        assertEquals(o.get(), output);
    }
}
