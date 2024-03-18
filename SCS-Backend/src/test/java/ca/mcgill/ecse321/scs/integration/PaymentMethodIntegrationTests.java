package ca.mcgill.ecse321.scs.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.time.LocalTime;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.scs.model.PaymentMethod;
import ca.mcgill.ecse321.scs.model.Customer;
import ca.mcgill.ecse321.scs.dto.ErrorDto;
import ca.mcgill.ecse321.scs.dto.CustomHoursListDto;
import ca.mcgill.ecse321.scs.dto.CustomHoursRequestDto;
import ca.mcgill.ecse321.scs.dto.CustomHoursResponseDto;
import ca.mcgill.ecse321.scs.dto.CustomerDto;
import ca.mcgill.ecse321.scs.dto.PaymentMethodRequestDto;
import ca.mcgill.ecse321.scs.dto.PaymentMethodResponseDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class PaymentMethodIntegrationTests {
    @Autowired
    private TestRestTemplate client;

    private final long CARDNUMBER = 1234567890123456L;
    private final int EXPIRYMONTH = 07;
    private final int EXPIRYYEAR = 24;
    private final int SECURITYCODE = 123;
    private static int PAYMENTID;

    private static int CID;
    private final String CUSTOMERNAME = "henry";
    private final String CUSTOMEREMAIL = "henry@test.com";
    private final String PASSWORD = "1234";
    private final Date CREATIONDATE = new Date(20240318);

    @AfterAll
    public void clearDatabase() {
        client.exchange("/customers", HttpMethod.DELETE, null, Void.class);
    }

    @BeforeAll
    public void createCustomer() {
        CustomerDto request = new CustomerDto(null, CUSTOMERNAME, CUSTOMEREMAIL, PASSWORD, CREATIONDATE);
        ResponseEntity<CustomerDto> responseCustomer = client.postForEntity("/customers", request, CustomerDto.class);
        CID = responseCustomer.getBody().getId();
    }

    @Test
    @Order(1)
    public void testCreatePaymentMethod() {
       // set up
       PaymentMethodRequestDto request = new PaymentMethodRequestDto(CARDNUMBER, EXPIRYMONTH, EXPIRYYEAR, SECURITYCODE, PAYMENTID, new Customer(CID, CREATIONDATE, CUSTOMERNAME, CUSTOMEREMAIL, PASSWORD));

       // act
       ResponseEntity<PaymentMethodResponseDto> response = client.postForEntity("/paymentMethod", request, PaymentMethodResponseDto.class);
       
       // assert
       assertNotNull(response);
       assertEquals(HttpStatus.CREATED, response.getStatusCode());
       
       PaymentMethodResponseDto body = response.getBody();
       PaymentMethodIntegrationTests.PAYMENTID = body.getPaymentId();
       assertNotNull(body);
       assertEquals(CARDNUMBER, body.getCardNumber());
       assertEquals(EXPIRYMONTH, body.getExpiryMonth());
       assertEquals(EXPIRYYEAR, body.getExpiryYear());
       assertEquals(SECURITYCODE, body.getSecurityCode());
       assertEquals(CID, body.getCustomer().getId());
    }
 
    @Test
    @Order(2)
    public void testCreatePaymentMethodWrongCardNumber() {

        // set up
        PaymentMethodRequestDto request = new PaymentMethodRequestDto(CARDNUMBER/10, EXPIRYMONTH, EXPIRYYEAR, SECURITYCODE, PAYMENTID, new Customer(CID, CREATIONDATE, CUSTOMERNAME, CUSTOMEREMAIL, PASSWORD));

        // act
        ResponseEntity<ErrorDto> response = client.postForEntity("/paymentMethod", request, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Card number must be 16 digits.", body.getErrors().get(0));
    }

    @Test
    @Order(3)
    public void testCreatePaymentMethodWrongExpiryMonthLength() {

        // set up
        PaymentMethodRequestDto request = new PaymentMethodRequestDto(CARDNUMBER, EXPIRYMONTH*100, EXPIRYYEAR, SECURITYCODE, PAYMENTID, new Customer(CID, CREATIONDATE, CUSTOMERNAME, CUSTOMEREMAIL, PASSWORD));

        // act
        ResponseEntity<ErrorDto> response = client.postForEntity("/paymentMethod", request, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Expiry month must be 2 digits.", body.getErrors().get(0));
    }

    @Test
    @Order(4)
    public void testCreatePaymentMethodWrongExpiryMonth() {

        // set up
        PaymentMethodRequestDto request = new PaymentMethodRequestDto(CARDNUMBER, 13, EXPIRYYEAR, SECURITYCODE, PAYMENTID, new Customer(CID, CREATIONDATE, CUSTOMERNAME, CUSTOMEREMAIL, PASSWORD));

        // act
        ResponseEntity<ErrorDto> response = client.postForEntity("/paymentMethod", request, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Expiry month must be in the range from 1 to 12.", body.getErrors().get(0));
    }

    @Test
    @Order(5)
    public void testCreatePaymentMethodWrongExpiryYearLength() {

        // set up
        PaymentMethodRequestDto request = new PaymentMethodRequestDto(CARDNUMBER, EXPIRYMONTH, EXPIRYYEAR/10, SECURITYCODE, PAYMENTID, new Customer(CID, CREATIONDATE, CUSTOMERNAME, CUSTOMEREMAIL, PASSWORD));

        // act
        ResponseEntity<ErrorDto> response = client.postForEntity("/paymentMethod", request, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Expiry year must be 2 digits.", body.getErrors().get(0));
    }

    @Test
    @Order(6)
    public void testCreatePaymentMethodWrongExpiryYear() {

        // set up
        PaymentMethodRequestDto request = new PaymentMethodRequestDto(CARDNUMBER, EXPIRYMONTH, 23, SECURITYCODE, PAYMENTID, new Customer(CID, CREATIONDATE, CUSTOMERNAME, CUSTOMEREMAIL, PASSWORD));

        // act
        ResponseEntity<ErrorDto> response = client.postForEntity("/paymentMethod", request, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Expiry year must not be expired.", body.getErrors().get(0));
    }

    @Test
    @Order(7)
    public void testCreatePaymentMethodWrongSecurityLength() {

        // set up
        PaymentMethodRequestDto request = new PaymentMethodRequestDto(CARDNUMBER, EXPIRYMONTH, EXPIRYYEAR, SECURITYCODE/10, PAYMENTID, new Customer(CID, CREATIONDATE, CUSTOMERNAME, CUSTOMEREMAIL, PASSWORD));

        // act
        ResponseEntity<ErrorDto> response = client.postForEntity("/paymentMethod", request, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Security code must be 3 digits.", body.getErrors().get(0));
    }

    @Test
    @Order(8)
    public void testCreatePaymentMethodPaymentIdExists() {

        // set up
        PaymentMethodRequestDto request = new PaymentMethodRequestDto(CARDNUMBER, EXPIRYMONTH, EXPIRYYEAR, SECURITYCODE, PAYMENTID, new Customer(CID, CREATIONDATE, CUSTOMERNAME, CUSTOMEREMAIL, PASSWORD));

        // act
        ResponseEntity<ErrorDto> response = client.postForEntity("/paymentMethod", request, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Payment method with id " + PAYMENTID + " already exists.", body.getErrors().get(0));
    }

    @Test
    @Order(9)
    public void testGetPaymentMethodByPaymentId() {
        // act
        ResponseEntity<PaymentMethodResponseDto> response = client.getForEntity("/paymentMethod/" + PAYMENTID, PaymentMethodResponseDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        PaymentMethodResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(CARDNUMBER, body.getCardNumber());
        assertEquals(EXPIRYMONTH, body.getExpiryMonth());
        assertEquals(EXPIRYYEAR, body.getExpiryYear());
        assertEquals(PAYMENTID, body.getPaymentId());
        assertEquals(SECURITYCODE, body.getSecurityCode());
        assertEquals(CID, body.getCustomer().getId());
    }

    @Test
    @Order(10)
    public void testGetCustomHoursByInvalidPaymentId() {
        // act
        ResponseEntity<ErrorDto> response = client.getForEntity("/paymentMethod/" + PAYMENTID + 1, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Payment method with ID " + PAYMENTID + 1 + " does not exist.", body.getErrors().get(0));
    }

    @Test
    @Order(11)
    public void testGetPaymentMethodByAccountId() {
        // act
        ResponseEntity<PaymentMethodResponseDto> response = client.getForEntity("/customers/" + CID + "/paymentMethod", PaymentMethodResponseDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        PaymentMethodResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(CARDNUMBER, body.getCardNumber());
        assertEquals(EXPIRYMONTH, body.getExpiryMonth());
        assertEquals(EXPIRYYEAR, body.getExpiryYear());
        assertEquals(PAYMENTID, body.getPaymentId());
        assertEquals(SECURITYCODE, body.getSecurityCode());
        assertEquals(CID, body.getCustomer().getId());
    }

    @Test
    @Order(12)
    public void testGetPaymentMethodByInvalidAccountId() {
        // act
        ResponseEntity<ErrorDto> response = client.getForEntity("/customers/" + CID + 1 + "/paymentMethod", ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Payment method with account id " + CID + 1 + " does not exist.", body.getErrors().get(0));
    }

    @Test
    @Order(13)
    public void testUpdatePaymentMethodCardNumber() {
        // set up
        long newCardNumber = 1234567890123457L;
        PaymentMethodRequestDto request = new PaymentMethodRequestDto(newCardNumber, EXPIRYMONTH, EXPIRYYEAR, SECURITYCODE, PAYMENTID, new Customer(CID, CREATIONDATE, CUSTOMERNAME, CUSTOMEREMAIL, PASSWORD));

        // wrap the request in an HttpEntity
        HttpEntity<PaymentMethodRequestDto> requestEntity = new HttpEntity<>(request);

        // act
        ResponseEntity<PaymentMethodResponseDto> response = client.exchange("/paymentMethod/" + PAYMENTID, HttpMethod.PUT, requestEntity, PaymentMethodResponseDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        PaymentMethodResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(newCardNumber, body.getCardNumber());
        assertEquals(EXPIRYMONTH, body.getExpiryMonth());
        assertEquals(EXPIRYYEAR, body.getExpiryYear());
        assertEquals(PAYMENTID, body.getPaymentId());
        assertEquals(SECURITYCODE, body.getSecurityCode());
        assertEquals(CID, body.getCustomer().getId());
    }

    @Test
    @Order(14)
    public void testUpdatePaymentMethodWrongCardNumber() {
        // set up
        long newCardNumber = 12345678901234578L;
        PaymentMethodRequestDto request = new PaymentMethodRequestDto(newCardNumber, EXPIRYMONTH, EXPIRYYEAR, SECURITYCODE, PAYMENTID, new Customer(CID, CREATIONDATE, CUSTOMERNAME, CUSTOMEREMAIL, PASSWORD));

        // wrap the request in an HttpEntity
        HttpEntity<PaymentMethodRequestDto> requestEntity = new HttpEntity<>(request);

        // act
        ResponseEntity<ErrorDto> response = client.exchange("/paymentMethod/" + PAYMENTID, HttpMethod.PUT, requestEntity, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Card number must be 16 digits.", body.getErrors().get(0));
    }

    @Test
    @Order(15)
    public void testUpdatePaymentMethodExpiryMonth() {
        // set up
        int newExpiryMonth = 3;
        PaymentMethodRequestDto request = new PaymentMethodRequestDto(CARDNUMBER, newExpiryMonth, EXPIRYYEAR, SECURITYCODE, PAYMENTID, new Customer(CID, CREATIONDATE, CUSTOMERNAME, CUSTOMEREMAIL, PASSWORD));

        // wrap the request in an HttpEntity
        HttpEntity<PaymentMethodRequestDto> requestEntity = new HttpEntity<>(request);

        // act
        ResponseEntity<PaymentMethodResponseDto> response = client.exchange("/paymentMethod/" + PAYMENTID, HttpMethod.PUT, requestEntity, PaymentMethodResponseDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        PaymentMethodResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(CARDNUMBER, body.getCardNumber());
        assertEquals(newExpiryMonth, body.getExpiryMonth());
        assertEquals(EXPIRYYEAR, body.getExpiryYear());
        assertEquals(PAYMENTID, body.getPaymentId());
        assertEquals(SECURITYCODE, body.getSecurityCode());
        assertEquals(CID, body.getCustomer().getId());
    }

    @Test
    @Order(16)
    public void testUpdatePaymentMethodWrongExpiryMonthLength() {
        // set up
        int newExpiryMonth = 345;
        PaymentMethodRequestDto request = new PaymentMethodRequestDto(CARDNUMBER, newExpiryMonth, EXPIRYYEAR, SECURITYCODE, PAYMENTID, new Customer(CID, CREATIONDATE, CUSTOMERNAME, CUSTOMEREMAIL, PASSWORD));

        // wrap the request in an HttpEntity
        HttpEntity<PaymentMethodRequestDto> requestEntity = new HttpEntity<>(request);

        // act
        ResponseEntity<ErrorDto> response = client.exchange("/paymentMethod/" + PAYMENTID, HttpMethod.PUT, requestEntity, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Expiry month must be 2 digits.", body.getErrors().get(0));
    }

    @Test
    @Order(17)
    public void testUpdatePaymentMethodWrongExpiryMonth() {
        // set up
        int newExpiryMonth = 14;
        PaymentMethodRequestDto request = new PaymentMethodRequestDto(CARDNUMBER, newExpiryMonth, EXPIRYYEAR, SECURITYCODE, PAYMENTID, new Customer(CID, CREATIONDATE, CUSTOMERNAME, CUSTOMEREMAIL, PASSWORD));

        // wrap the request in an HttpEntity
        HttpEntity<PaymentMethodRequestDto> requestEntity = new HttpEntity<>(request);

        // act
        ResponseEntity<ErrorDto> response = client.exchange("/paymentMethod/" + PAYMENTID, HttpMethod.PUT, requestEntity, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Expiry month must be in the range from 1 to 12.", body.getErrors().get(0));
    }

    @Test
    @Order(18)
    public void testUpdatePaymentMethodExpiryYear() {
        // set up
        int newExpiryYear = 26;
        PaymentMethodRequestDto request = new PaymentMethodRequestDto(CARDNUMBER, EXPIRYMONTH, newExpiryYear, SECURITYCODE, PAYMENTID, new Customer(CID, CREATIONDATE, CUSTOMERNAME, CUSTOMEREMAIL, PASSWORD));

        // wrap the request in an HttpEntity
        HttpEntity<PaymentMethodRequestDto> requestEntity = new HttpEntity<>(request);

        // act
        ResponseEntity<PaymentMethodResponseDto> response = client.exchange("/paymentMethod/" + PAYMENTID, HttpMethod.PUT, requestEntity, PaymentMethodResponseDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        PaymentMethodResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(CARDNUMBER, body.getCardNumber());
        assertEquals(EXPIRYMONTH, body.getExpiryMonth());
        assertEquals(newExpiryYear, body.getExpiryYear());
        assertEquals(PAYMENTID, body.getPaymentId());
        assertEquals(SECURITYCODE, body.getSecurityCode());
        assertEquals(CID, body.getCustomer().getId());
    }

    @Test
    @Order(19)
    public void testUpdatePaymentMethodWrongExpiryYearLength() {
        // set up
        int newExpiryYear = 345;
        PaymentMethodRequestDto request = new PaymentMethodRequestDto(CARDNUMBER, EXPIRYMONTH, newExpiryYear, SECURITYCODE, PAYMENTID, new Customer(CID, CREATIONDATE, CUSTOMERNAME, CUSTOMEREMAIL, PASSWORD));

        // wrap the request in an HttpEntity
        HttpEntity<PaymentMethodRequestDto> requestEntity = new HttpEntity<>(request);

        // act
        ResponseEntity<ErrorDto> response = client.exchange("/paymentMethod/" + PAYMENTID, HttpMethod.PUT, requestEntity, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Expiry year must be 2 digits.", body.getErrors().get(0));
    }

    @Test
    @Order(20)
    public void testUpdatePaymentMethodWrongExpiryYear() {
        // set up
        int newExpiryYear = 23;
        PaymentMethodRequestDto request = new PaymentMethodRequestDto(CARDNUMBER, EXPIRYMONTH, newExpiryYear, SECURITYCODE, PAYMENTID, new Customer(CID, CREATIONDATE, CUSTOMERNAME, CUSTOMEREMAIL, PASSWORD));

        // wrap the request in an HttpEntity
        HttpEntity<PaymentMethodRequestDto> requestEntity = new HttpEntity<>(request);

        // act
        ResponseEntity<ErrorDto> response = client.exchange("/paymentMethod/" + PAYMENTID, HttpMethod.PUT, requestEntity, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Expiry year must not be expired.", body.getErrors().get(0));
    }

    @Test
    @Order(21)
    public void testUpdatePaymentMethodSecurityCode() {
        // set up
        int newSecurityCode = 269;
        PaymentMethodRequestDto request = new PaymentMethodRequestDto(CARDNUMBER, EXPIRYMONTH, EXPIRYYEAR, newSecurityCode, PAYMENTID, new Customer(CID, CREATIONDATE, CUSTOMERNAME, CUSTOMEREMAIL, PASSWORD));

        // wrap the request in an HttpEntity
        HttpEntity<PaymentMethodRequestDto> requestEntity = new HttpEntity<>(request);

        // act
        ResponseEntity<PaymentMethodResponseDto> response = client.exchange("/paymentMethod/" + PAYMENTID, HttpMethod.PUT, requestEntity, PaymentMethodResponseDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        PaymentMethodResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(CARDNUMBER, body.getCardNumber());
        assertEquals(EXPIRYMONTH, body.getExpiryMonth());
        assertEquals(EXPIRYYEAR, body.getExpiryYear());
        assertEquals(PAYMENTID, body.getPaymentId());
        assertEquals(newSecurityCode, body.getSecurityCode());
        assertEquals(CID, body.getCustomer().getId());
    }

    @Test
    @Order(22)
    public void testUpdatePaymentMethodWrongSecurityCodeLength() {
        // set up
        int newSecurityCode = 3457;
        PaymentMethodRequestDto request = new PaymentMethodRequestDto(CARDNUMBER, EXPIRYMONTH, EXPIRYYEAR, newSecurityCode, PAYMENTID, new Customer(CID, CREATIONDATE, CUSTOMERNAME, CUSTOMEREMAIL, PASSWORD));

        // wrap the request in an HttpEntity
        HttpEntity<PaymentMethodRequestDto> requestEntity = new HttpEntity<>(request);

        // act
        ResponseEntity<ErrorDto> response = client.exchange("/paymentMethod/" + PAYMENTID, HttpMethod.PUT, requestEntity, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Security code must be 3 digits.", body.getErrors().get(0));
    }

    @SuppressWarnings("null")
    @Test
    @Order(23)
    public void testDeletePaymentMethod() {
        // act
        ResponseEntity<Void> response = client.exchange("/paymentMethod/" + PAYMENTID, HttpMethod.DELETE, null, Void.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // assert that the custom hours was deleted
        ResponseEntity<ErrorDto> getResponse = client.getForEntity("/paymentMethod/" + PAYMENTID, ErrorDto.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
        assertEquals("Payment method with ID " + PAYMENTID + " does not exist.", getResponse.getBody().getErrors().get(0));
    }

    @Test
    @Order(24)
    public void testDeletePaymentMethodPaymentIdDoesNotExist() {
        // act
        ResponseEntity<ErrorDto> response = client.exchange("/paymentMethod/" + PAYMENTID + 1, HttpMethod.DELETE, null, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Payment method with ID " + PAYMENTID + 1 + " does not exist.", body.getErrors().get(0));
    }

    @Test
    @Order(25)
    public void testDeleteAllPaymentMethods() {

        // set up, create another custom hours
        PaymentMethodRequestDto request = new PaymentMethodRequestDto(CARDNUMBER, EXPIRYMONTH, EXPIRYYEAR, SECURITYCODE, PAYMENTID, new Customer(CID, CREATIONDATE, CUSTOMERNAME, CUSTOMEREMAIL, PASSWORD));
        client.postForEntity("/paymentMethod", request, PaymentMethodResponseDto.class);

        // act
        ResponseEntity<Void> response = client.exchange("/paymentMethod", HttpMethod.DELETE, null, Void.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // assert that there are no custom hours
        ResponseEntity<ErrorDto> getResponse = client.getForEntity("/paymentMethod/" + PAYMENTID, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        ErrorDto body = getResponse.getBody(); 
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Payment method with ID " + PAYMENTID + " does not exist.", body.getErrors().get(0));
    }
}
