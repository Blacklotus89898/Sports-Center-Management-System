package ca.mcgill.ecse321.scs.integration;

import ca.mcgill.ecse321.scs.dto.CustomerDto;
import ca.mcgill.ecse321.scs.model.Customer;
import ca.mcgill.ecse321.scs.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerService customerService;

    @BeforeEach
    public void cleanUp() {
        customerService.deleteAllCustomers();
    }

    @Test
    public void testGetAllCustomers() {
        ResponseEntity<CustomerDto[]> response = restTemplate.getForEntity("/api/customers", CustomerDto[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetCustomerById() {
        Customer customer = customerService.createCustomer("Test", "test@test.com", "password");
        ResponseEntity<CustomerDto> response = restTemplate.getForEntity("/api/customers/" + customer.getAccountId(),
                CustomerDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetCustomerByEmail() { // same here
        // Customer existingCustomer =
        // customerService.getCustomerByEmail("test@test.com");
        // if (existingCustomer != null) {
        // customerService.deleteCustomerById(existingCustomer.getAccountId());
        // }

        Customer customer = customerService.createCustomer("Test", "test@test.com", "password");
        ResponseEntity<CustomerDto> response = restTemplate.getForEntity("/api/customers/email/" + customer.getEmail(),
                CustomerDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testCreateCustomer() {// here
        // Customer existingCustomer =
        // customerService.getCustomerByEmail("test@test.com");
        // if (existingCustomer != null) {
        // customerService.deleteCustomerById(existingCustomer.getAccountId());
        // }
        CustomerDto request = new CustomerDto(null, "Test", "test@test.com", "password", null);
        ResponseEntity<CustomerDto> response = restTemplate.postForEntity("/api/customers", request, CustomerDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdateCustomerById() { // can have a delete database after each test
        // Customer existingCustomer =
        // customerService.getCustomerByEmail("test@test.com");
        // if (existingCustomer != null) {
        // customerService.deleteCustomerById(existingCustomer.getAccountId());
        // }
        Customer customer = customerService.createCustomer("Test", "test@test.com", "password");
        CustomerDto request = new CustomerDto(customer.getAccountId(), "Updated", "updated@test.com", "updated", null);
        restTemplate.put("/api/customers/" + customer.getAccountId(), request);
        ResponseEntity<CustomerDto> response = restTemplate.getForEntity("/api/customers/" + customer.getAccountId(),
                CustomerDto.class);
        assertEquals("Updated", response.getBody().getName());
    }
    // @Test
    // public void testUpdateCustomerById() {
    // // Delete the customer if it already exists
    // // Customer existingCustomer =
    // customerService.getCustomerByEmail("test@test.com");
    // // if (existingCustomer != null) {
    // // customerService.deleteCustomerById(existingCustomer.getAccountId());
    // // }

    // // Create the customer
    // Customer customer = customerService.createCustomer("Test", "test@test.com",
    // "password");

    // // Prepare the request parameters
    // MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
    // parameters.add("name", "Updated");
    // parameters.add("email", "updated@test.com");
    // parameters.add("password", "updated");

    // // Send the PUT request
    // HttpEntity<MultiValueMap<String, String>> request = new
    // HttpEntity<>(parameters, new HttpHeaders());
    // restTemplate.exchange("/api/customers/" + customer.getAccountId(),
    // HttpMethod.PUT, request, Void.class);

    // // Fetch the updated customer and check if the name has been updated
    // ResponseEntity<CustomerDto> response =
    // restTemplate.getForEntity("/api/customers/" + customer.getAccountId(),
    // CustomerDto.class);
    // assertEquals("Updated", response.getBody().getName());
    // }

    @Test
    public void testDeleteCustomerById() {
        Customer customer = customerService.createCustomer("Test", "test@test.com", "password");
        restTemplate.delete("/api/customers/" + customer.getAccountId());
        ResponseEntity<CustomerDto> response = restTemplate.getForEntity("/api/customers/" + customer.getAccountId(),
                CustomerDto.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetCustomerByIdNotFound() {
        Integer nonExistentId = 9999; // Assuming this ID does not exist in the database
        ResponseEntity<CustomerDto> response = restTemplate.getForEntity("/api/customers/" + nonExistentId,
                CustomerDto.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}