package ca.mcgill.ecse321.scs.integration;

import ca.mcgill.ecse321.scs.dto.CustomerDto;
import ca.mcgill.ecse321.scs.model.Customer;
import ca.mcgill.ecse321.scs.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
        ResponseEntity<CustomerDto[]> response = restTemplate.getForEntity("/customers", CustomerDto[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetCustomerById() {
        Customer customer = customerService.createCustomer("Test", "test@test.com", "password");
        ResponseEntity<CustomerDto> response = restTemplate.getForEntity("/customers/" + customer.getAccountId(),
                CustomerDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testCreateCustomer() {
        CustomerDto request = new CustomerDto(null, "Test", "test@test.com", "password", null);
        ResponseEntity<CustomerDto> response = restTemplate.postForEntity("/customers", request, CustomerDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(5)
    public void testUpdateCustomerById() {
        Customer customer = customerService.createCustomer("Test", "test@test.com", "password");
        CustomerDto request = new CustomerDto(customer.getAccountId(), "Updated", "updated@test.com", "updated", null);
        restTemplate.put("/customers/" + customer.getAccountId(), request);
        ResponseEntity<CustomerDto> response = restTemplate.getForEntity("/customers/" + customer.getAccountId(),
                CustomerDto.class);
        assertEquals("Updated", response.getBody().getName());
    }

    @Test
    public void testDeleteCustomerById() {
        Customer customer = customerService.createCustomer("Test", "test@test.com", "password");
        restTemplate.delete("/api/customers/" + customer.getAccountId());
        ResponseEntity<CustomerDto> response = restTemplate.getForEntity("/customers/" + customer.getAccountId(),
                CustomerDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetCustomerByIdNotFound() {
        Integer nonExistentId = 9999; // Assuming this ID does not exist in the database
        ResponseEntity<CustomerDto> response = restTemplate.getForEntity("/customers/" + nonExistentId,
                CustomerDto.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}