package ca.mcgill.ecse321.scs.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.scs.model.Customer;
import java.sql.Date;

@SuppressWarnings("null")
@SpringBootTest
public class CustomerRepositoryTests {
    @Autowired
    private CustomerRepository customerRepository;

    @AfterEach
	public void clearDatabase() {
		customerRepository.deleteAll();
	}

    @Test
    public void testPersistAndLoadCustomer() {
        // creation of the class type
        Date date = new Date(0);
        String name = "Koko"; 
        String email = "lol@gmail.com";
        String password = "secret";

        // creation of the owner
        Customer customer = new Customer();
        customer.setCreationDate(date);
        customer.setEmail(email);
        customer.setName(name);
        customer.setPassword(password);

        // save the class type
        Customer sentCustomer = customerRepository.save(customer);

        // read customer type from database
        Customer resultCustomer =  customerRepository.findCustomerByAccountId(sentCustomer.getAccountId());
        // Customer resultCustomer = result.get();

        // assert equals
        assertNotNull(resultCustomer);
        assertEquals(date.toString(), resultCustomer.getCreationDate().toString());
        assertEquals(email, resultCustomer.getEmail());
        assertEquals(name, resultCustomer.getName());
        assertEquals(password, resultCustomer.getPassword());
    }
}
