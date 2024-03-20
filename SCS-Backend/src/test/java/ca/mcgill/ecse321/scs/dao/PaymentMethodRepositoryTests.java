package ca.mcgill.ecse321.scs.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.scs.model.Customer;
import ca.mcgill.ecse321.scs.model.PaymentMethod;
import java.sql.Date;
import java.util.Optional;

@SuppressWarnings("null")
@SpringBootTest
public class PaymentMethodRepositoryTests {
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @AfterEach
	public void clearDatabase() {
		paymentMethodRepository.deleteAll();
		customerRepository.deleteAll();
	}

    @Test
    public void testPersistAndLoadPaymentMethod() {
        // creation of the class type

        // move to before each to 
        Date date = new Date(0);
        String name = "Koko"; 
        String email = "lol@gmail.com";
        String password = "secret";

        long number = 1;
        int month = 2;
        int year = 3;
        int code = 4;

        Customer customer = new Customer();
        customer.setCreationDate(date);
        customer.setEmail(email);
        customer.setName(name);
        customer.setPassword(password);
        
        Customer sentCustomer = customerRepository.save(customer);


        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setCardNumber(number);
        paymentMethod.setExpiryMonth(month);
        paymentMethod.setExpiryYear(year);
        paymentMethod.setSecurityCode(code);

        paymentMethod.setCustomer(sentCustomer);
        

        PaymentMethod sentPaymentMethod = paymentMethodRepository.save(paymentMethod);


        // read paymentMethod type from database
        Optional<PaymentMethod> result =  paymentMethodRepository.findById(sentPaymentMethod.getPaymentId());
        PaymentMethod resultPaymentMethod = result.get();

        
        // assert equals
        assertNotNull(resultPaymentMethod);
        assertEquals(number, resultPaymentMethod.getCardNumber());
        assertEquals(month, resultPaymentMethod.getExpiryMonth());
        assertEquals(year, resultPaymentMethod.getExpiryYear());
        assertEquals(code, resultPaymentMethod.getSecurityCode());
        assertEquals(sentCustomer.getAccountId(), resultPaymentMethod.getCustomer().getAccountId());
        assertEquals(sentCustomer.getCreationDate().toString(), resultPaymentMethod.getCustomer().getCreationDate().toString());
        assertEquals(sentCustomer.getEmail(), resultPaymentMethod.getCustomer().getEmail());
        assertEquals(sentCustomer.getName(), resultPaymentMethod.getCustomer().getName());
        assertEquals(sentCustomer.getPassword(), resultPaymentMethod.getCustomer().getPassword());
    }
}
