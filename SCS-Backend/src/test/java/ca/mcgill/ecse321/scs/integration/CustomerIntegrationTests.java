package ca.mcgill.ecse321.scs.integration;

import ca.mcgill.ecse321.scs.dto.CustomerDto;
import ca.mcgill.ecse321.scs.dto.ErrorDto;
import ca.mcgill.ecse321.scs.dto.CustomerListDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class CustomerIntegrationTests {
    @Autowired
    private TestRestTemplate client;

    private int customerId = -1;
    private final String NAME = "John Doe";
    private final String EMAIL = "john.doe@gmail.com";
    private final String PASSWORD = "password";

    @Test
    @Order(1)
    public void testCreateCustomer() {
        // set up
        CustomerDto customerDto = new CustomerDto(-1, NAME, EMAIL, PASSWORD);

        // act
        ResponseEntity<CustomerDto> response = client.postForEntity("/customers", customerDto, CustomerDto.class);
        
        // assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        customerId = response.getBody().getId();
        assertEquals(NAME, response.getBody().getName());
        assertEquals(EMAIL, response.getBody().getEmail());
        assertEquals(PASSWORD, response.getBody().getPassword());
    }

    @Test
    @Order(2)
    public void testCreateCustomerInvalidEmail() {
        // set up
        CustomerDto customerDto = new CustomerDto(-1, NAME, "invalid email", PASSWORD);

        // act
        ResponseEntity<ErrorDto> response = client.postForEntity("/customers", customerDto, ErrorDto.class);
        
        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertEquals(1, body.getErrors().size());
        assertEquals("Email is not valid.", body.getErrors().get(0));
    }

    @Test
    @Order(3)
    public void testCreateCustomerInvalidPassword() {
        // set up
        CustomerDto customerDto = new CustomerDto(-1, NAME, EMAIL, "");

        // act
        ResponseEntity<ErrorDto> response = client.postForEntity("/customers", customerDto, ErrorDto.class);
        
        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertEquals(1, body.getErrors().size());
        assertEquals("Password cannot be empty.", body.getErrors().get(0));
    }

    @Test
    @Order(4)
    public void testCreateCustomerInvalidName() {
        // set up
        CustomerDto customerDto = new CustomerDto(-1, "", EMAIL, PASSWORD);

        // act
        ResponseEntity<ErrorDto> response = client.postForEntity("/customers", customerDto, ErrorDto.class);
        
        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertEquals(1, body.getErrors().size());
        assertEquals("Name cannot be empty.", body.getErrors().get(0));
    }

    @Test
    @Order(4)
    public void testCreateCustomerDuplicateEmail() {
        // set up
        CustomerDto customerDto = new CustomerDto(-1, "any name", EMAIL, PASSWORD);

        // act
        ResponseEntity<ErrorDto> response = client.postForEntity("/customers", customerDto, ErrorDto.class);
        
        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertEquals(1, body.getErrors().size());
        assertEquals("An account with this email already exists.", body.getErrors().get(0));
    }

    @Test
    @Order(5)
    public void testGetCustomerById() {
        // set up
        ResponseEntity<CustomerDto> response = client.getForEntity("/customers/" + customerId, CustomerDto.class);
        
        // assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(NAME, response.getBody().getName());
        assertEquals(EMAIL, response.getBody().getEmail());
    }

    @Test
    @Order(6)
    public void testGetCustomerByIdNotFound() {
        // set up
        ResponseEntity<ErrorDto> response = client.getForEntity("/customers/12083", ErrorDto.class);
        
        // assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorDto body = response.getBody(); 
        assertEquals(1, body.getErrors().size());
        assertEquals("Customer not found.", body.getErrors().get(0));
    }

    @Test
    @Order(7)
    public void testUpdateCustomer() {
        // set up
        CustomerDto customerDto = new CustomerDto(-1, "Jane Doe", "newemail@gmail.com", "newpassword");

        // act
        ResponseEntity<CustomerDto> response = client.exchange("/customers/" + customerId, HttpMethod.PUT, new HttpEntity<>(customerDto), CustomerDto.class);

        // assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(customerId, response.getBody().getId());
        assertEquals("Jane Doe", response.getBody().getName());
        assertEquals("newemail@gmail.com", response.getBody().getEmail());
        assertEquals("newpassword", response.getBody().getPassword());
    }

    @Test
    @Order(8)
    public void testUpdateCustomerInvalidEmail() {
        // set up
        CustomerDto customerDto = new CustomerDto(-1, "Jane Doe", "invalid email", "newpassword");

        // act
        ResponseEntity<ErrorDto> response = client.exchange("/customers/" + customerId, HttpMethod.PUT, new HttpEntity<>(customerDto), ErrorDto.class);
        
        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertEquals(1, body.getErrors().size());
        assertEquals("Email is not valid.", body.getErrors().get(0));
    }

    @Test
    @Order(9)
    public void testUpdateCustomerInvalidPassword() {
        // set up
        CustomerDto customerDto = new CustomerDto(-1, "Jane Doe", EMAIL, "");

        // act
        ResponseEntity<ErrorDto> response = client.exchange("/customers/" + customerId, HttpMethod.PUT, new HttpEntity<>(customerDto), ErrorDto.class);

        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertEquals(1, body.getErrors().size());
        assertEquals("Password cannot be empty.", body.getErrors().get(0));

    }

    @Test
    @Order(10)
    public void testUpdateCustomerInvalidName() {
        // set up
        CustomerDto customerDto = new CustomerDto(-1, "", EMAIL, PASSWORD);

        // act
        ResponseEntity<ErrorDto> response = client.exchange("/customers/" + customerId, HttpMethod.PUT, new HttpEntity<>(customerDto), ErrorDto.class);
        
        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertEquals(1, body.getErrors().size());
        assertEquals("Name cannot be empty.", body.getErrors().get(0));
    }

    @Test
    @Order(11)
    public void testUpdateCustomerEmailExists() {
        // set up
        CustomerDto existingCustomerDto = new CustomerDto(-1, "Jane Doe", "newnewemail@gmail.com", "random password");
        ResponseEntity<CustomerDto> existingCustomerResponse = client.postForEntity("/customers", existingCustomerDto, CustomerDto.class);

        CustomerDto customerDto = new CustomerDto(-1, "Jane Doe", existingCustomerResponse.getBody().getEmail(), "newpassword");

        // act
        ResponseEntity<ErrorDto> response = client.exchange("/customers/" + customerId, HttpMethod.PUT, new HttpEntity<>(customerDto), ErrorDto.class);

        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertEquals(1, body.getErrors().size());
        assertEquals("An account with this email already exists.", body.getErrors().get(0));
    }

    @Test
    @Order(12)
    public void testDeleteCustomer() {
        // act
        ResponseEntity<Void> response = client.exchange("/customers/" + customerId, HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        
        // assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // check if customer was deleted
        ResponseEntity<ErrorDto> response2 = client.getForEntity("/customers/" + customerId, ErrorDto.class);
        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
        ErrorDto body = response2.getBody();
        assertEquals(1, body.getErrors().size());
        assertEquals("Customer not found.", body.getErrors().get(0));
    }

    @Test
    @Order(13)
    public void testDeleteCustomerNotFound() {
        // act
        ResponseEntity<ErrorDto> response = client.getForEntity("/customers/12083", ErrorDto.class);
        
        // assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorDto body = response.getBody(); 
        assertEquals(1, body.getErrors().size());
        assertEquals("Customer not found.", body.getErrors().get(0));
    }

    @Test
    @Order(14)
    public void testDeleteAllCustomersAndGetAllCustomers() {
        // set up
        CustomerDto customerDto = new CustomerDto(-1, NAME, EMAIL, PASSWORD);
        client.postForEntity("/customers", customerDto, CustomerDto.class);
        customerDto = new CustomerDto(-1, NAME, "ranodom@gmail.com", PASSWORD);
        client.postForEntity("/customers", customerDto, CustomerDto.class);
        customerDto = new CustomerDto(-1, NAME, "raaornstm@gmail.com", PASSWORD);
        client.postForEntity("/customers", customerDto, CustomerDto.class);

        // assert that there are >= 3 customers
        ResponseEntity<CustomerListDto> response = client.getForEntity("/customers", CustomerListDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getCustomers().size() >= 3);

        // act
        ResponseEntity<Void> response2 = client.exchange("/customers", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        
        // assert
        assertEquals(HttpStatus.NO_CONTENT, response2.getStatusCode());

        // assert that there are no customers
        ResponseEntity<CustomerListDto> response3 = client.getForEntity("/customers", CustomerListDto.class);
        assertEquals(HttpStatus.OK, response3.getStatusCode());
        assertNotNull(response3.getBody());
        assertEquals(0, response3.getBody().getCustomers().size());
    }
}