package ca.mcgill.ecse321.scs.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
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
import org.springframework.test.annotation.Commit;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.scs.dto.ErrorDto;
import ca.mcgill.ecse321.scs.dto.CustomerDto;
import ca.mcgill.ecse321.scs.dto.ClassTypeRequestDto;
import ca.mcgill.ecse321.scs.dto.ClassTypeResponseDto;
import ca.mcgill.ecse321.scs.dto.SpecificClassRequestDto;
import ca.mcgill.ecse321.scs.dto.SpecificClassResponseDto;
import ca.mcgill.ecse321.scs.dto.ClassRegistrationListDto;
import ca.mcgill.ecse321.scs.dto.ClassRegistrationRequestDto;
import ca.mcgill.ecse321.scs.dto.ClassRegistrationResponseDto;
import ca.mcgill.ecse321.scs.dto.ScheduleRequestDto;
import ca.mcgill.ecse321.scs.dto.ScheduleResponseDto;

/**
 * This class contains integration tests for the ClassRegistrationController.
 * It tests the functionality of creating, retrieving, and updating class registrations.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ClassRegistrationIntegrationTests {
    @Autowired
    private TestRestTemplate restTemplate;

    private int REGISTRATION_ID = -1;
    private int ACCOUNT_ID = 1;
    private int ACCOUNT_ID2 = 2;
    private int CLASS_ID = 1;

    private final String NAME = "John Doe";
    private final String EMAIL = "john@gmail.com";
    private final String EMAIL2 = "john2@gmail.com";
    private final String PASSWORD = "password";
    private final byte[] IMAGE = new byte[1024];


    private final String CLASS_TYPE = "ClassType";
    private final int YEAR = 2022;
    private final String SPECIFIC_CLASS_NAME = "SpecificClassName";
    private final String DESCRIPTION = "Description";
    private final LocalDate DATE = LocalDate.of(2022, 12, 15);
    private final LocalTime START_TIME = LocalTime.of(12, 0);
    private final int HOUR_DURATION = 1;
    private final int MAX_CAPACITY = 10;
    private final int CURRENT_CAPACITY = 5;
    private final double REGISTRATION_FEE = 10.0;

    private final String CLASS_TYPE_DESCRIPTION = "ClassTypeDescription";
    private final boolean IS_APPROVED = true;

    private ClassRegistrationRequestDto classRegistrationRequestDto;

    @AfterAll
    public void clearDatabase() {
        restTemplate.exchange("/classRegistrations", HttpMethod.DELETE, null, ErrorDto.class);
        restTemplate.exchange("/specificClass", HttpMethod.DELETE, null, ErrorDto.class);
        restTemplate.exchange("/customers", HttpMethod.DELETE, null, ErrorDto.class);
        restTemplate.exchange("/classTypes", HttpMethod.DELETE, null, ErrorDto.class);
    }

    @BeforeAll
    public void createCustomerAndSpecificClass() {
        CustomerDto customerRequestDto = new CustomerDto(ACCOUNT_ID, NAME, EMAIL, PASSWORD, IMAGE);

        CustomerDto customerRequestDto2 = new CustomerDto(ACCOUNT_ID2, NAME, EMAIL2, PASSWORD, IMAGE);
        // customerRequestDto2.setId(ACCOUNT_ID2);
        // customerRequestDto2.setName(NAME);
        // customerRequestDto2.setEmail(EMAIL2);
        // customerRequestDto2.setPassword(PASSWORD);
        
        ResponseEntity<CustomerDto> customerResponseDto = restTemplate.postForEntity("/customers", customerRequestDto, CustomerDto.class);
        assertEquals(HttpStatus.CREATED, customerResponseDto.getStatusCode());
        ResponseEntity<CustomerDto> customerResponseDto2 = restTemplate.postForEntity("/customers", customerRequestDto2, CustomerDto.class);
        assertEquals(HttpStatus.CREATED, customerResponseDto2.getStatusCode());

        // get the customer account id
        CustomerDto customerResponseDtoBody = customerResponseDto.getBody();
        assertNotNull(customerResponseDtoBody);
        ACCOUNT_ID = customerResponseDtoBody.getId();

        // get the customer2 account id
        CustomerDto customerResponseDtoBody2 = customerResponseDto2.getBody();
        assertNotNull(customerResponseDtoBody2);
        ACCOUNT_ID2 = customerResponseDtoBody2.getId();

        ClassTypeRequestDto classTypeRequestDto = new ClassTypeRequestDto();
        classTypeRequestDto = new ClassTypeRequestDto(CLASS_TYPE, CLASS_TYPE_DESCRIPTION, IS_APPROVED, "icon.png");

        ResponseEntity<ClassTypeResponseDto> classTypeResponseDto = restTemplate.postForEntity("/classType", classTypeRequestDto, ClassTypeResponseDto.class);
        assertEquals(HttpStatus.CREATED, classTypeResponseDto.getStatusCode());

        ScheduleRequestDto scheduleRequestDto = new ScheduleRequestDto(YEAR);

        ResponseEntity<ScheduleResponseDto> scheduleResponseDto = restTemplate.postForEntity("/schedule", scheduleRequestDto, ScheduleResponseDto.class);
        assertEquals(HttpStatus.CREATED, scheduleResponseDto.getStatusCode());

        SpecificClassRequestDto specificClassRequestDto = new SpecificClassRequestDto(CLASS_ID, CLASS_TYPE, YEAR, SPECIFIC_CLASS_NAME, DESCRIPTION, DATE, START_TIME, HOUR_DURATION, MAX_CAPACITY, CURRENT_CAPACITY, REGISTRATION_FEE, null);
        ResponseEntity<SpecificClassResponseDto> specificClassResponseDto = restTemplate.postForEntity("/specificClass", specificClassRequestDto, SpecificClassResponseDto.class);
        assertEquals(HttpStatus.CREATED, specificClassResponseDto.getStatusCode());

        // get the class id
        SpecificClassResponseDto specificClassResponseDtoBody = specificClassResponseDto.getBody();
        assertNotNull(specificClassResponseDtoBody);
        CLASS_ID = specificClassResponseDtoBody.getClassId();
    }

    @Test
    @Order(1)
    @Commit
    public void testCreateClassRegistration() {
        // set up
        classRegistrationRequestDto = new ClassRegistrationRequestDto(REGISTRATION_ID, ACCOUNT_ID, CLASS_ID);

        // act
        ResponseEntity<ClassRegistrationResponseDto> response = restTemplate.postForEntity("/classRegistration", classRegistrationRequestDto, ClassRegistrationResponseDto.class);
        REGISTRATION_ID = response.getBody().getRegistrationId();

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        
        ClassRegistrationResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(ACCOUNT_ID, body.getCustomer().getId());
        assertEquals(CLASS_ID, body.getSpecificClass().getClassId());
    }

    @Test
    @Order(3)
    public void testCreateClassRegistrationInvalidCustomerId() {
        // tests that a Class registration cannot be created with an invalid customer id
        classRegistrationRequestDto.setAccountId(-1);

        ResponseEntity<ErrorDto> response = restTemplate.postForEntity("/classRegistration", classRegistrationRequestDto, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Customer not found.", body.getErrors().get(0));

        classRegistrationRequestDto.setAccountId(ACCOUNT_ID);
    }

    @Test
    @Order(4)
    public void testCreateClassRegistrationInvalidClassId() {
        // tests that a Class registration cannot be created with an invalid class id
        classRegistrationRequestDto.setClassId(-1);

        ResponseEntity<ErrorDto> response = restTemplate.postForEntity("/classRegistration", classRegistrationRequestDto, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Specific class with id -1 not found.", body.getErrors().get(0));

        classRegistrationRequestDto.setClassId(CLASS_ID);
    }

    @Test
    @Order(5)
    public void testGetClassRegistrationById() {
        // act
        ResponseEntity<ClassRegistrationResponseDto> response = restTemplate.getForEntity("/classRegistration/" + REGISTRATION_ID, ClassRegistrationResponseDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        ClassRegistrationResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(ACCOUNT_ID, body.getCustomer().getId());
        assertEquals(CLASS_ID, body.getSpecificClass().getClassId());
    }

    @Test
    @Order(6)
    public void testGetClassRegistrationByIdInvalidId() {
        // act
        ResponseEntity<ErrorDto> response = restTemplate.getForEntity("/classRegistration/" + -1, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        
        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Registration info with id -1 not found.", body.getErrors().get(0));
    }

    @Test
    @Order(7)
    public void testUpdateClassRegistration() {
        // set up
        classRegistrationRequestDto.setAccountId(ACCOUNT_ID2);
        classRegistrationRequestDto.setClassId(CLASS_ID);

        // act
        ResponseEntity<ClassRegistrationResponseDto> response = restTemplate.exchange("/classRegistration/" + REGISTRATION_ID, HttpMethod.PUT, new HttpEntity<>(classRegistrationRequestDto), ClassRegistrationResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        ClassRegistrationResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(ACCOUNT_ID2, body.getCustomer().getId());
        assertEquals(CLASS_ID, body.getSpecificClass().getClassId());
    }

    @Test
    @Order(8)
    public void testUpdateClassRegistrationInvalidCustomerId() {
        // tests that a Class registration cannot be updated with an invalid customer id
        classRegistrationRequestDto.setAccountId(-1);

        ResponseEntity<ErrorDto> response = restTemplate.exchange("/classRegistration/" + REGISTRATION_ID, HttpMethod.PUT, new HttpEntity<>(classRegistrationRequestDto), ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Customer not found.", body.getErrors().get(0));

        classRegistrationRequestDto.setAccountId(ACCOUNT_ID2);
    }

    @Test
    @Order(9)
    //test get Class registration by class id
    public void testGetClassRegistrationsByClassId() {
        // act
        ResponseEntity<ClassRegistrationListDto> response = restTemplate.getForEntity("/specificClass/" + CLASS_ID + "/classRegistration", ClassRegistrationListDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        // check that it returns a list of class registrations
        ClassRegistrationListDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getClassRegistrations().size());
        assertEquals(ACCOUNT_ID2, body.getClassRegistrations().get(0).getCustomer().getId());
        assertEquals(CLASS_ID, body.getClassRegistrations().get(0).getSpecificClass().getClassId());
    }

    @Test
    @Order(10)
    // test to delete a Class registration
    public void testDeleteClassRegistration() {
        // act
        ResponseEntity<Void> response = restTemplate.exchange("/classRegistration/" + REGISTRATION_ID, HttpMethod.DELETE, null, Void.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}