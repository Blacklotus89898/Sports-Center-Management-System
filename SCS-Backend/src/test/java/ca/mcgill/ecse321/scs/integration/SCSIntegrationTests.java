package ca.mcgill.ecse321.scs.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

import ca.mcgill.ecse321.scs.dto.RoleDto.Role;
import ca.mcgill.ecse321.scs.dto.AccountRequestDto;
import ca.mcgill.ecse321.scs.dto.AccountResponseDto;
import ca.mcgill.ecse321.scs.dto.CustomerDto;
import ca.mcgill.ecse321.scs.dto.ErrorDto;
import ca.mcgill.ecse321.scs.dto.InstructorRequestDto;
import ca.mcgill.ecse321.scs.dto.InstructorResponseDto;
import ca.mcgill.ecse321.scs.dto.OwnerRequestDto;
import ca.mcgill.ecse321.scs.dto.OwnerResponseDto;

/**
 * This class contains integration tests for the SCS (Sports Center System) application.
 * It tests the login functionality for different roles: customer, instructor, and owner.
 * It also tests various scenarios such as empty email, empty password, and invalid login.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class SCSIntegrationTests {
    @Autowired
    private TestRestTemplate restTemplate;

    private int ownerId = -1;
    private final String CUSTOMER_NAME = "Customer Name";
    private final String INSTRUCTOR_NAME = "Instructor Name";
    private final String OWNER_NAME = "Owner Name";
    private final String CUSTOMER_EMAIL = "customer@sports.center";
    private final String INSTRUCTOR_EMAIL = "instructor@sports.center";
    private final String OWNER_EMAIL = "owner@sports.center";
    private final String PASSWORD = "password";

    @AfterAll
    public void cleanUp() {
        // Clear all data
        restTemplate.exchange("/customers", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        restTemplate.exchange("/owner/" + ownerId, HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        restTemplate.exchange("/instructors", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
    }

    @BeforeAll
    public void setUp() {
        // Create a customer
        CustomerDto customerDto = new CustomerDto(-1, CUSTOMER_NAME, CUSTOMER_EMAIL, PASSWORD);
        ResponseEntity<CustomerDto> responseCustomers = restTemplate.postForEntity("/customers", customerDto, CustomerDto.class);
        assertEquals(HttpStatus.CREATED, responseCustomers.getStatusCode());

        // Create an instructor
        InstructorRequestDto instructorDto = new InstructorRequestDto(-1, INSTRUCTOR_NAME, INSTRUCTOR_EMAIL, PASSWORD);
        ResponseEntity<InstructorResponseDto> responseInstructor = restTemplate.postForEntity("/instructors", instructorDto, InstructorResponseDto.class);
        assertEquals(HttpStatus.CREATED, responseInstructor.getStatusCode());

        // Create an owner
        OwnerRequestDto ownerDto = new OwnerRequestDto(-1, OWNER_NAME, OWNER_EMAIL, PASSWORD);
        ResponseEntity<OwnerResponseDto> responseOwner = restTemplate.postForEntity("/owner", ownerDto, OwnerResponseDto.class);
        assertEquals(HttpStatus.CREATED, responseOwner.getStatusCode());

        // get owner id
        OwnerResponseDto ownerResponseDto = responseOwner.getBody();
        assertNotNull(ownerResponseDto);
        ownerId = ownerResponseDto.getId();
    }

    @Test
    @Order(1)
    public void testLoginCustomer() {
        // set up
        AccountRequestDto accountRequestDto = new AccountRequestDto(CUSTOMER_EMAIL, PASSWORD);

        // act
        ResponseEntity<AccountResponseDto> response = restTemplate.postForEntity("/login", accountRequestDto, AccountResponseDto.class);

        // assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        AccountResponseDto accountResponseDto = response.getBody();
        assertNotNull(accountResponseDto);
        assertEquals(Role.CUSTOMER, accountResponseDto.getRole());
        assertEquals(CUSTOMER_NAME, accountResponseDto.getName());
        assertEquals(CUSTOMER_EMAIL, accountResponseDto.getEmail());
        assertEquals(PASSWORD, accountResponseDto.getPassword());
    }

    @Test
    @Order(2)
    public void testLoginInstructor() {
        // set up
        AccountRequestDto accountRequestDto = new AccountRequestDto(INSTRUCTOR_EMAIL, PASSWORD);

        // act
        ResponseEntity<AccountResponseDto> response = restTemplate.postForEntity("/login", accountRequestDto, AccountResponseDto.class);

        // assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        AccountResponseDto accountResponseDto = response.getBody();
        assertNotNull(accountResponseDto);
        assertEquals(Role.INSTRUCTOR, accountResponseDto.getRole());
        assertEquals(INSTRUCTOR_NAME, accountResponseDto.getName());
        assertEquals(INSTRUCTOR_EMAIL, accountResponseDto.getEmail());
        assertEquals(PASSWORD, accountResponseDto.getPassword());
    }

    @Test
    @Order(3)
    public void testLoginOwner() {
        // set up
        AccountRequestDto accountRequestDto = new AccountRequestDto(OWNER_EMAIL, PASSWORD);

        // act
        ResponseEntity<AccountResponseDto> response = restTemplate.postForEntity("/login", accountRequestDto, AccountResponseDto.class);

        // assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        AccountResponseDto accountResponseDto = response.getBody();
        assertNotNull(accountResponseDto);
        assertEquals(Role.OWNER, accountResponseDto.getRole());
        assertEquals(OWNER_NAME, accountResponseDto.getName());
        assertEquals(OWNER_EMAIL, accountResponseDto.getEmail());
        assertEquals(PASSWORD, accountResponseDto.getPassword());
    }

    @Test
    @Order(4)
    public void testLoginEmptyEmail() {
        // set up
        AccountRequestDto accountRequestDto = new AccountRequestDto("", PASSWORD);

        // act
        ResponseEntity<ErrorDto> response = restTemplate.postForEntity("/login", accountRequestDto, ErrorDto.class);

        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Email cannot be empty.", body.getErrors().get(0));
    }

    @Test
    @Order(5)
    public void testLoginEmptyPassword() {
        // set up
        AccountRequestDto accountRequestDto = new AccountRequestDto(CUSTOMER_EMAIL, "");

        // act
        ResponseEntity<ErrorDto> response = restTemplate.postForEntity("/login", accountRequestDto, ErrorDto.class);

        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Password cannot be empty.", body.getErrors().get(0));
    }

    @Test
    @Order(6)
    public void testLoginInvalidLogin() {
        // set up
        AccountRequestDto accountRequestDto = new AccountRequestDto("invalidemail@gmail.com", PASSWORD);

        // act
        ResponseEntity<ErrorDto> response = restTemplate.postForEntity("/login", accountRequestDto, ErrorDto.class);

        // assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        
        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Please verify that your email and password is correct.", body.getErrors().get(0));
    }
}