package ca.mcgill.ecse321.scs.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.description;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Collections;

import java.time.LocalDate;
import java.time.LocalTime;

import java.sql.Date;
import java.sql.Time;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.scs.exception.SCSException;
import ca.mcgill.ecse321.scs.model.CustomHours;
import ca.mcgill.ecse321.scs.model.Customer;
import ca.mcgill.ecse321.scs.dao.CustomerRepository;
import ca.mcgill.ecse321.scs.model.PaymentMethod;
import ca.mcgill.ecse321.scs.dao.PaymentMethodRepository;

@SpringBootTest
public class PaymentMethodServiceTests {
    @Mock
    private PaymentMethodRepository paymentMethodRepository;
    @InjectMocks
    private PaymentMethodService paymentMethodService;

    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);                     // initializes mocks and injects them

        paymentMethodService.setCustomerService(customerService);

        int accountId1 = 1;
        Date creationDate1 = new Date(20240316);
        String name1 = "Henry";
        String email1 = "henry@test";
        String password1 = "hry123";
        Customer customer1 = new Customer(accountId1, creationDate1, name1, email1, password1);
        this.customer = customer1;

        int accountId2 = 2;
        Date creationDate2 = new Date(20240315);
        String name2 = "Qasim";
        String email2 = "Qasim@test";
        String password2 = "qsm123";
        Customer customer2 = new Customer(accountId2, creationDate2, name2, email2, password2);

        when(customerRepository.findCustomerByEmail(email1)).thenReturn(customer1);
        when(customerRepository.findCustomerByEmail(email2)).thenReturn(customer2);
    }

    @Test
    public void testCreateValidPaymentMethod() {
        // set up
        long cardNumber = 1234567890123456L;
        int expiryMonth = 07;
        int expiryYear = 24;
        int securityCode = 123;
        int paymentId = 1;
        String customerEmail = "henry@test";

        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        PaymentMethod paymentMethod = paymentMethodService.createPaymentMethod(cardNumber, expiryMonth, expiryYear, securityCode, paymentId, customerEmail);

        // assert
        assertNotNull(paymentMethod);
        assertEquals(cardNumber, paymentMethod.getCardNumber());
        assertEquals(expiryMonth, paymentMethod.getExpiryMonth());
        assertEquals(expiryYear, paymentMethod.getExpiryYear());
        assertEquals(securityCode, paymentMethod.getSecurityCode());
        assertEquals(paymentId, paymentMethod.getPaymentId());
        assertEquals(customerEmail, paymentMethod.getCustomer().getEmail());
        verify(paymentMethodRepository, times(1)).save(any(PaymentMethod.class));
    }

    @Test
    public void testCreateInvalidPaymentMethodWrongCardNumber() {
        // test for card number

        // set up
        long cardNumber = 123456789012345L;
        int expiryMonth = 07;
        int expiryYear = 24;
        int securityCode = 123;
        int paymentId = 1;
        String customerEmail = "henry@test";

        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            paymentMethodService.createPaymentMethod(cardNumber, expiryMonth, expiryYear, securityCode, paymentId, customerEmail);
        });

        // assert
        assertEquals("Card number must be 16 digits.", exception.getMessage());
    }

    @Test
    public void testCreateInvalidPaymentMethodWrongSecurityCode() {
        // test for security code

        // set up
        long cardNumber = 1234567890123456L;
        int expiryMonth = 07;
        int expiryYear = 24;
        int securityCode = 1234;
        int paymentId = 1;
        String customerEmail = "henry@test";

        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            paymentMethodService.createPaymentMethod(cardNumber, expiryMonth, expiryYear, securityCode, paymentId, customerEmail);
        });

        // assert
        assertEquals("Security code must be 3 digits.", exception.getMessage());
    }

    @Test
    public void testCreateInvalidPaymentMethodWrongExpiryMonth() {
        // test for expiry month

        // set up
        long cardNumber = 1234567890123456L;
        int expiryMonth = 13;
        int expiryYear = 24;
        int securityCode = 123;
        int paymentId = 1;
        String customerEmail = "henry@test";

        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            paymentMethodService.createPaymentMethod(cardNumber, expiryMonth, expiryYear, securityCode, paymentId, customerEmail);
        });

        // assert
        assertEquals("Expiry month must be in the range from 1 to 12.", exception.getMessage());
    }

    @Test
    public void testCreateInvalidPaymentMethodWrongExpiryMonthLength() {
        // test for expiry month

        // set up
        long cardNumber = 1234567890123456L;
        int expiryMonth = 128;
        int expiryYear = 24;
        int securityCode = 123;
        int paymentId = 1;
        String customerEmail = "henry@test";

        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            paymentMethodService.createPaymentMethod(cardNumber, expiryMonth, expiryYear, securityCode, paymentId, customerEmail);
        });

        // assert
        assertEquals("Expiry month must be 2 digits.", exception.getMessage());
    }

    @Test
    public void testCreateInvalidPaymentMethodWrongExpiryYear() {
        // test for expiry yeaer

        // set up
        long cardNumber = 1234567890123456L;
        int expiryMonth = 10;
        int expiryYear = 23;
        int securityCode = 123;
        int paymentId = 1;
        String customerEmail = "henry@test";

        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            paymentMethodService.createPaymentMethod(cardNumber, expiryMonth, expiryYear, securityCode, paymentId, customerEmail);
        });

        // assert
        assertEquals("Expiry year must not be expired.", exception.getMessage());
    }

    @Test
    public void testCreateInvalidPaymentMethodWrongExpiryYearLength() {
        // test for expiry year

        // set up
        long cardNumber = 1234567890123456L;
        int expiryMonth = 10;
        int expiryYear = 2024;
        int securityCode = 123;
        int paymentId = 1;
        String customerEmail = "henry@test";

        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            paymentMethodService.createPaymentMethod(cardNumber, expiryMonth, expiryYear, securityCode, paymentId, customerEmail);
        });

        // assert
        assertEquals("Expiry year must be 2 digits.", exception.getMessage());
    }

    @Test
    public void testCreateInvalidPaymentMethodNullEmail() {
        // test for email address

        // set up
        long cardNumber = 1234567890123456L;
        int expiryMonth = 10;
        int expiryYear = 24;
        int securityCode = 123;
        int paymentId = 1;
        String customerEmail = "";

        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            paymentMethodService.createPaymentMethod(cardNumber, expiryMonth, expiryYear, securityCode, paymentId, customerEmail);
        });

        // assert
        assertEquals("Email cannot be empty.", exception.getMessage());
    }

    @Test
    public void testCreateInvalidCPaymentMethodExists() {
        // payment method already exists

        // set up
        long cardNumber = 1234567890123456L;
        int expiryMonth = 10;
        int expiryYear = 24;
        int securityCode = 123;
        int paymentId = 1;
        String customerEmail = "henry@test";

        PaymentMethod existingPaymentMethod = new PaymentMethod(cardNumber, expiryMonth, expiryYear, securityCode, paymentId, this.customer);
        when(paymentMethodRepository.findPaymentMethodByPaymentId(paymentId)).thenReturn(existingPaymentMethod);

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            paymentMethodService.createPaymentMethod(cardNumber, expiryMonth, expiryYear, securityCode, paymentId, customerEmail);
        });

        // assert
        assertEquals("Payment method with id " + paymentId + " already exists.", exception.getMessage());
    }

    @Test
    public void testGetPaymentMethod() {
        // set up
        long cardNumber = 1234567890123456L;
        int expiryMonth = 10;
        int expiryYear = 24;
        int securityCode = 123;
        int paymentId = 1;
        String customerEmail = "henry@test";

        PaymentMethod paymentMethod = new PaymentMethod(cardNumber, expiryMonth, expiryYear, securityCode, paymentId, customer);
        when(paymentMethodRepository.findPaymentMethodByPaymentId(paymentId)).thenReturn(paymentMethod);

        // act
        PaymentMethod returnedPaymentMethod = paymentMethodService.getPaymentMethod(paymentId);

        // assert
        assertNotNull(returnedPaymentMethod);
        assertEquals(cardNumber, returnedPaymentMethod.getCardNumber());
        assertEquals(expiryMonth, returnedPaymentMethod.getExpiryMonth());
        assertEquals(expiryYear, returnedPaymentMethod.getExpiryYear());
        assertEquals(securityCode, returnedPaymentMethod.getSecurityCode());
        assertEquals(paymentId, returnedPaymentMethod.getPaymentId());
        assertEquals(customerEmail, returnedPaymentMethod.getCustomer().getEmail());
    }

    @Test
    public void testGetPaymentMethodNull() {
        // set up
        int paymentId = 1;

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            paymentMethodService.getPaymentMethod(paymentId);
        });

        // assert
        assertEquals("Payment method with ID " + paymentId + " does not exist.", exception.getMessage());
    }

    @Test
    public void testGetPaymentMethodByCardNumber() {
        // set up
        long cardNumber = 1234567890123456L;
        int expiryMonth = 10;
        int expiryYear = 24;
        int securityCode = 123;
        int paymentId = 1;
        String customerEmail = "henry@test";

        PaymentMethod paymentMethod = new PaymentMethod(cardNumber, expiryMonth, expiryYear, securityCode, paymentId, customer);
        when(paymentMethodRepository.findAll()).thenReturn(Collections.singletonList(paymentMethod));

        // act
        PaymentMethod returnedPaymentMethod = paymentMethodService.getPaymentMethodByCardNumber(cardNumber);

        // assert
        assertNotNull(returnedPaymentMethod);
        assertEquals(cardNumber, returnedPaymentMethod.getCardNumber());
        assertEquals(expiryMonth, returnedPaymentMethod.getExpiryMonth());
        assertEquals(expiryYear, returnedPaymentMethod.getExpiryYear());
        assertEquals(securityCode, returnedPaymentMethod.getSecurityCode());
        assertEquals(paymentId, returnedPaymentMethod.getPaymentId());
        assertEquals(customerEmail, returnedPaymentMethod.getCustomer().getEmail());
    }

    @Test
    public void testGetPaymentMethodByCardNumberNull() {
        // set up
        long cardNumber = 1234567890123456L;
        when(paymentMethodRepository.findAll()).thenReturn(Collections.emptyList());

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            paymentMethodService.getPaymentMethodByCardNumber(cardNumber);
        });

        // assert
        assertEquals("Payment method with card number " + cardNumber + " does not exist.", exception.getMessage());
    }

    @Test
    public void testUpdatePaymentMethodCardNumber() {
        // set up
        long cardNumber = 1234567890123456L;
        int expiryMonth = 10;
        int expiryYear = 24;
        int securityCode = 123;
        int paymentId = 1;
        String customerEmail = "henry@test";

        PaymentMethod paymentMethod = new PaymentMethod(cardNumber, expiryMonth, expiryYear, securityCode, paymentId, customer);
        when(paymentMethodRepository.findPaymentMethodByPaymentId(paymentId)).thenReturn(paymentMethod);
        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        long cardNumber1 = 2234567890123456L;
        PaymentMethod updatedPaymentMethod = paymentMethodService.updatePaymentMethod(paymentId, cardNumber1, expiryMonth, expiryYear, securityCode, customerEmail);

        // assert
        assertNotNull(updatedPaymentMethod);
        assertEquals(cardNumber1, updatedPaymentMethod.getCardNumber());
        assertEquals(expiryMonth, updatedPaymentMethod.getExpiryMonth());
        assertEquals(expiryYear, updatedPaymentMethod.getExpiryYear());
        assertEquals(securityCode, updatedPaymentMethod.getSecurityCode());
        assertEquals(paymentId, updatedPaymentMethod.getPaymentId());
        assertEquals(customerEmail, updatedPaymentMethod.getCustomer().getEmail());
        verify(paymentMethodRepository, times(1)).save(any(PaymentMethod.class));
    }

    @Test
    public void testUpdatePaymentMethodExpiryYear() {
        // set up
        long cardNumber = 1234567890123456L;
        int expiryMonth = 10;
        int expiryYear = 24;
        int securityCode = 123;
        int paymentId = 1;
        String customerEmail = "henry@test";

        PaymentMethod paymentMethod = new PaymentMethod(cardNumber, expiryMonth, expiryYear, securityCode, paymentId, customer);
        when(paymentMethodRepository.findPaymentMethodByPaymentId(paymentId)).thenReturn(paymentMethod);
        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        int expiryYear1 = 25;
        PaymentMethod updatedPaymentMethod = paymentMethodService.updatePaymentMethod(paymentId, cardNumber, expiryMonth, expiryYear1, securityCode, customerEmail);

        // assert
        assertNotNull(updatedPaymentMethod);
        assertEquals(cardNumber, updatedPaymentMethod.getCardNumber());
        assertEquals(expiryMonth, updatedPaymentMethod.getExpiryMonth());
        assertEquals(expiryYear1, updatedPaymentMethod.getExpiryYear());
        assertEquals(securityCode, updatedPaymentMethod.getSecurityCode());
        assertEquals(paymentId, updatedPaymentMethod.getPaymentId());
        assertEquals(customerEmail, updatedPaymentMethod.getCustomer().getEmail());
        verify(paymentMethodRepository, times(1)).save(any(PaymentMethod.class));
    }

    @Test
    public void testUpdatePaymentMethodExpiryMonth() {
        // set up
        long cardNumber = 1234567890123456L;
        int expiryMonth = 10;
        int expiryYear = 24;
        int securityCode = 123;
        int paymentId = 1;
        String customerEmail = "henry@test";

        PaymentMethod paymentMethod = new PaymentMethod(cardNumber, expiryMonth, expiryYear, securityCode, paymentId, customer);
        when(paymentMethodRepository.findPaymentMethodByPaymentId(paymentId)).thenReturn(paymentMethod);
        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        int expiryMonth1 = 7;
        PaymentMethod updatedPaymentMethod = paymentMethodService.updatePaymentMethod(paymentId, cardNumber, expiryMonth1, expiryYear, securityCode, customerEmail);

        // assert
        assertNotNull(updatedPaymentMethod);
        assertEquals(cardNumber, updatedPaymentMethod.getCardNumber());
        assertEquals(expiryMonth1, updatedPaymentMethod.getExpiryMonth());
        assertEquals(expiryYear, updatedPaymentMethod.getExpiryYear());
        assertEquals(securityCode, updatedPaymentMethod.getSecurityCode());
        assertEquals(paymentId, updatedPaymentMethod.getPaymentId());
        assertEquals(customerEmail, updatedPaymentMethod.getCustomer().getEmail());
        verify(paymentMethodRepository, times(1)).save(any(PaymentMethod.class));
    }

    @Test
    public void testUpdatePaymentMethodSecurityCode() {
        // set up
        long cardNumber = 1234567890123456L;
        int expiryMonth = 10;
        int expiryYear = 24;
        int securityCode = 123;
        int paymentId = 1;
        String customerEmail = "henry@test";

        PaymentMethod paymentMethod = new PaymentMethod(cardNumber, expiryMonth, expiryYear, securityCode, paymentId, customer);
        when(paymentMethodRepository.findPaymentMethodByPaymentId(paymentId)).thenReturn(paymentMethod);
        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        int securityCode1 = 735;
        PaymentMethod updatedPaymentMethod = paymentMethodService.updatePaymentMethod(paymentId, cardNumber, expiryMonth, expiryYear, securityCode1, customerEmail);

        // assert
        assertNotNull(updatedPaymentMethod);
        assertEquals(cardNumber, updatedPaymentMethod.getCardNumber());
        assertEquals(expiryMonth, updatedPaymentMethod.getExpiryMonth());
        assertEquals(expiryYear, updatedPaymentMethod.getExpiryYear());
        assertEquals(securityCode1, updatedPaymentMethod.getSecurityCode());
        assertEquals(paymentId, updatedPaymentMethod.getPaymentId());
        assertEquals(customerEmail, updatedPaymentMethod.getCustomer().getEmail());
        verify(paymentMethodRepository, times(1)).save(any(PaymentMethod.class));
    }

    @Test
    public void testUpdatePaymentMethodInvalidCardNumber() {
        // set up
        long cardNumber = 1234567890123456L;
        int expiryMonth = 10;
        int expiryYear = 24;
        int securityCode = 123;
        int paymentId = 1;
        String customerEmail = "henry@test";

        PaymentMethod paymentMethod = new PaymentMethod(cardNumber, expiryMonth, expiryYear, securityCode, paymentId, customer);
        when(paymentMethodRepository.findPaymentMethodByPaymentId(paymentId)).thenReturn(paymentMethod);
        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        long cardNumber1 = 123456789013456L;
        Exception exception = assertThrows(SCSException.class, () -> {
            paymentMethodService.updatePaymentMethod(paymentId, cardNumber1, expiryMonth, expiryYear, securityCode, customerEmail);
        });

        // assert
        assertEquals("Card number must be 16 digits.", exception.getMessage());
    }

    @Test
    public void testUpdatePaymentMethodInvalidExpiryYear() {
        // set up
        long cardNumber = 1234567890123456L;
        int expiryMonth = 10;
        int expiryYear = 24;
        int securityCode = 123;
        int paymentId = 1;
        String customerEmail = "henry@test";

        PaymentMethod paymentMethod = new PaymentMethod(cardNumber, expiryMonth, expiryYear, securityCode, paymentId, customer);
        when(paymentMethodRepository.findPaymentMethodByPaymentId(paymentId)).thenReturn(paymentMethod);
        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        int expiryYear1 = 23;
        Exception exception = assertThrows(SCSException.class, () -> {
            paymentMethodService.updatePaymentMethod(paymentId, cardNumber, expiryMonth, expiryYear1, securityCode, customerEmail);
        });

        // assert
        assertEquals("Expiry year must not be expired.", exception.getMessage());
    }

    @Test
    public void testUpdatePaymentMethodInvalidExpiryMonth() {
        // set up
        long cardNumber = 1234567890123456L;
        int expiryMonth = 10;
        int expiryYear = 24;
        int securityCode = 123;
        int paymentId = 1;
        String customerEmail = "henry@test";

        PaymentMethod paymentMethod = new PaymentMethod(cardNumber, expiryMonth, expiryYear, securityCode, paymentId, customer);
        when(paymentMethodRepository.findPaymentMethodByPaymentId(paymentId)).thenReturn(paymentMethod);
        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        int expiryMonth1 = 13;
        Exception exception = assertThrows(SCSException.class, () -> {
            paymentMethodService.updatePaymentMethod(paymentId, cardNumber, expiryMonth1, expiryYear, securityCode, customerEmail);
        });

        // assert
        assertEquals("Expiry month must be in the range from 1 to 12.", exception.getMessage());
    }

    @Test
    public void testUpdatePaymentMethodInvalidSecurityCode() {
        // set up
        long cardNumber = 1234567890123456L;
        int expiryMonth = 10;
        int expiryYear = 24;
        int securityCode = 123;
        int paymentId = 1;
        String customerEmail = "henry@test";

        PaymentMethod paymentMethod = new PaymentMethod(cardNumber, expiryMonth, expiryYear, securityCode, paymentId, customer);
        when(paymentMethodRepository.findPaymentMethodByPaymentId(paymentId)).thenReturn(paymentMethod);
        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        int securityCode1 = 1239;
        Exception exception = assertThrows(SCSException.class, () -> {
            paymentMethodService.updatePaymentMethod(paymentId, cardNumber, expiryMonth, expiryYear, securityCode1, customerEmail);
        });

        // assert
        assertEquals("Security code must be 3 digits.", exception.getMessage());
    }

    @Test
    public void testGetAllPaymentMethod() {
        // set up
        long cardNumber = 1234567890123456L;
        int expiryMonth = 10;
        int expiryYear = 24;
        int securityCode = 123;
        int paymentId = 1;

        long cardNumber1 = 1234567890123457L;
        int expiryMonth1 = 11;
        int expiryYear1 = 25;
        int securityCode1 = 124;
        int paymentId1 = 2;

        int accountId2 = 2;
        Date creationDate2 = new Date(20240315);
        String name2 = "Qasim";
        String email2 = "Qasim@test";
        String password2 = "qsm123";
        Customer customer2 = new Customer(accountId2, creationDate2, name2, email2, password2);

        PaymentMethod paymentMethod1 = new PaymentMethod(cardNumber, expiryMonth, expiryYear, securityCode, paymentId, this.customer);
        PaymentMethod paymentMethod2 = new PaymentMethod(cardNumber1, expiryMonth1, expiryYear1, securityCode1, paymentId1, customer2);
        when(paymentMethodRepository.findAll()).thenReturn(List.of(paymentMethod1, paymentMethod2));

        // act
        List<PaymentMethod> paymentMethods = paymentMethodService.getAllPaymentMethods();

        // assert
        assertNotNull(paymentMethods);
        assertEquals(2, paymentMethods.size());
        assertTrue(paymentMethods.contains(paymentMethod1));
        assertTrue(paymentMethods.contains(paymentMethod2));
    }

    @Test
    public void testDeletePaymentMethod() {
        // set up
        long cardNumber = 1234567890123456L;
        int expiryMonth = 10;
        int expiryYear = 24;
        int securityCode = 123;
        int paymentId = 1;

        PaymentMethod paymentMethod = new PaymentMethod(cardNumber, expiryMonth, expiryYear, securityCode, paymentId, customer);
        when(paymentMethodRepository.findPaymentMethodByPaymentId(paymentId)).thenReturn(paymentMethod).thenReturn(null);

        // act
        paymentMethodService.deletePaymentMethod(paymentId);
        verify(paymentMethodRepository, times(1)).delete(paymentMethod);

        // act & assert exception
        Exception exception = assertThrows(SCSException.class, () -> {
            paymentMethodService.getPaymentMethod(paymentId);
        });

        // assert
        assertEquals("Payment method with ID " + paymentId + " does not exist.", exception.getMessage());
        verify(paymentMethodRepository, times(1)).delete(paymentMethod);
    }

    @Test
    public void testDeletePaymentMethodNull() {
        // set up
        int paymentId = 1;
        when(paymentMethodRepository.findPaymentMethodByPaymentId(paymentId)).thenReturn(null);

        // act & assert
        SCSException exception = assertThrows(SCSException.class, () -> {
            paymentMethodService.deletePaymentMethod(paymentId);
        });

        // assert
        assertEquals("Payment method with ID " + paymentId + " does not exist.", exception.getMessage());
        verify(paymentMethodRepository, times(0)).delete(any(PaymentMethod.class));
    }

    @Test
    public void testDeleteAllCustomHours() {
        // set up
        long cardNumber = 1234567890123456L;
        int expiryMonth = 10;
        int expiryYear = 24;
        int securityCode = 123;
        int paymentId = 1;

        long cardNumber1 = 1234567890123457L;
        int expiryMonth1 = 11;
        int expiryYear1 = 25;
        int securityCode1 = 124;
        int paymentId1 = 2;

        int accountId2 = 2;
        Date creationDate2 = new Date(20240315);
        String name2 = "Qasim";
        String email2 = "Qasim@test";
        String password2 = "qsm123";
        Customer customer2 = new Customer(accountId2, creationDate2, name2, email2, password2);

        PaymentMethod paymentMethod1 = new PaymentMethod(cardNumber, expiryMonth, expiryYear, securityCode, paymentId, this.customer);
        PaymentMethod paymentMethod2 = new PaymentMethod(cardNumber1, expiryMonth1, expiryYear1, securityCode1, paymentId1, customer2);

        // mock the initial state before deletion
        when(paymentMethodRepository.findAll()).thenReturn(List.of(paymentMethod1, paymentMethod2));

        // act: Delete all custom hours
        paymentMethodService.deleteAllPaymentMethods();
        when(paymentMethodRepository.findAll()).thenReturn(Collections.emptyList()); // adjust the mock to reflect the post-deletion state

        List<PaymentMethod> paymentMethods = paymentMethodService.getAllPaymentMethods();

        // assert
        assertNotNull(paymentMethods);
        assertEquals(0, paymentMethods.size());
        verify(paymentMethodRepository, times(1)).deleteAll();
    }
}
