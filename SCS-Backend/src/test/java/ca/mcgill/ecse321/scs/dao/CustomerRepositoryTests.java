package ca.mcgill.ecse321.scs.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.scs.model.Customer;
import java.sql.Date;
import java.util.Optional;


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
        // String className = "Yoga"; 
        String name = "Koko"; 
        String email = "lol@.com";
        String password = "secret";

        Customer customer = new Customer();
        customer.setCreationDate(date);
        customer.setEmail(email);
        customer.setName(name);
        customer.setPassword(password);
//   public Customer(int aAccountId, Date aCreationDate, String aName, String aEmail, String aPassword)

        // save the class type
        Customer sentCustomer = customerRepository.save(customer);

        // read customer type from database
        Optional<Customer> result =  customerRepository.findById(Integer.toString(sentCustomer.getAccountId()));
        Customer resultCustomer = result.get();

        // assert equals
        assertNotNull(resultCustomer);
        assertEquals(date.toString(), resultCustomer.getCreationDate().toString());
        assertEquals(email, resultCustomer.getEmail());
        assertEquals(name, resultCustomer.getName());
        assertEquals(password, resultCustomer.getPassword());
    }
}
