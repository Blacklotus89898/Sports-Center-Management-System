package ca.mcgill.ecse321.scs.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Time;
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
import ca.mcgill.ecse321.scs.dto.ClassTypeRequestDto;
import ca.mcgill.ecse321.scs.dto.ClassTypeResponseDto;
import ca.mcgill.ecse321.scs.dto.SpecificClassListDto;
import ca.mcgill.ecse321.scs.dto.SpecificClassRequestDto;
import ca.mcgill.ecse321.scs.dto.SpecificClassResponseDto;
import ca.mcgill.ecse321.scs.dto.ScheduleRequestDto;
import ca.mcgill.ecse321.scs.dto.ScheduleResponseDto;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class SpecificClassIntegrationTests {
    @Autowired
    private TestRestTemplate restTemplate;

    private int CLASS_ID = -1;
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

    private SpecificClassRequestDto specificClassRequestDto;

    @AfterAll
    public void clearDatabase() {
        restTemplate.exchange("/specificClass", HttpMethod.DELETE, null, ErrorDto.class);
        restTemplate.exchange("/classTypes", HttpMethod.DELETE, null, ErrorDto.class);
        restTemplate.exchange("/schedules", HttpMethod.DELETE, null, ErrorDto.class);
    }

    @BeforeAll
    public void createClassTypeAndSchedule() {
        ClassTypeRequestDto classTypeRequestDto = new ClassTypeRequestDto();
        classTypeRequestDto.setClassName(CLASS_TYPE);
        classTypeRequestDto.setDescription(DESCRIPTION);
        classTypeRequestDto.setIsApproved(true);
        
        ResponseEntity<ClassTypeResponseDto> classTypeResponseDto = restTemplate.postForEntity("/classType", classTypeRequestDto, ClassTypeResponseDto.class);
        assertEquals(HttpStatus.CREATED, classTypeResponseDto.getStatusCode());

        ScheduleRequestDto scheduleRequestDto = new ScheduleRequestDto(YEAR);
        ResponseEntity<ScheduleResponseDto> scheduleResponseDto = restTemplate.postForEntity("/schedule", scheduleRequestDto, ScheduleResponseDto.class);
        assertEquals(HttpStatus.CREATED, scheduleResponseDto.getStatusCode());

        ScheduleRequestDto scheduleRequestDto1 = new ScheduleRequestDto(YEAR + 1);
        restTemplate.postForEntity("/schedule", scheduleRequestDto1, ScheduleResponseDto.class);
        assertEquals(HttpStatus.CREATED, scheduleResponseDto.getStatusCode());
    }

    @Test
    @Order(1)
    @Commit
    public void testCreateSpecificClass() {
        // set up
        specificClassRequestDto = new SpecificClassRequestDto();
        specificClassRequestDto.setClassId(CLASS_ID);
        specificClassRequestDto.setClassType(CLASS_TYPE);
        specificClassRequestDto.setYear(YEAR);
        specificClassRequestDto.setSpecificClassName(SPECIFIC_CLASS_NAME);
        specificClassRequestDto.setDescription(DESCRIPTION);
        specificClassRequestDto.setDate(DATE);
        specificClassRequestDto.setStartTime(START_TIME);
        specificClassRequestDto.setHourDuration(HOUR_DURATION);
        specificClassRequestDto.setMaxCapacity(MAX_CAPACITY);
        specificClassRequestDto.setCurrentCapacity(CURRENT_CAPACITY);
        specificClassRequestDto.setRegistrationFee(REGISTRATION_FEE);

        // act
        ResponseEntity<SpecificClassResponseDto> response = restTemplate.postForEntity("/specificClass", specificClassRequestDto, SpecificClassResponseDto.class);
        CLASS_ID = response.getBody().getClassId();

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        
        SpecificClassResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(CLASS_TYPE, body.getClassType().getClassName());
        assertEquals(YEAR, body.getSchedule().getYear());
        assertEquals(SPECIFIC_CLASS_NAME, body.getSpecificClassName());
        assertEquals(DESCRIPTION, body.getDescription());
        assertEquals(DATE.toString(), body.getDate().toString());
        assertEquals(Time.valueOf(START_TIME), Time.valueOf(body.getStartTime()));
        assertEquals(MAX_CAPACITY, body.getMaxCapacity());
        assertEquals(CURRENT_CAPACITY, body.getCurrentCapacity());
        assertEquals(REGISTRATION_FEE, body.getRegistrationFee());
    }

    @Test
    @Order(2)
    public void testCreateSpecificClassInvalidClassType() {
        // tests that a specific class cannot be created with an invalid class type
        specificClassRequestDto.setClassType(null);

        ResponseEntity<ErrorDto> response = restTemplate.postForEntity("/specificClass", specificClassRequestDto, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Class type cannot be empty.", body.getErrors().get(0));

        specificClassRequestDto.setClassType(CLASS_TYPE);
    }

    @Test
    @Order(3)
    public void testCreateSpecificClassInvalidDescription() {
        // tests that a specific class cannot be created with an invalid description
        specificClassRequestDto.setDescription("");

        ResponseEntity<ErrorDto> response = restTemplate.postForEntity("/specificClass", specificClassRequestDto, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Description cannot be empty.", body.getErrors().get(0));

        specificClassRequestDto.setDescription(DESCRIPTION);
    }

    @Test
    @Order(4)
    public void testCreateSpecificClassInvalidDate() {
        // tests that a specific class cannot be created with an invalid date
        specificClassRequestDto.setDate(null);

        ResponseEntity<ErrorDto> response = restTemplate.postForEntity("/specificClass", specificClassRequestDto, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Invalid date.", body.getErrors().get(0));

        specificClassRequestDto.setDate(DATE);
    }

    @Test
    @Order(5)
    public void testCreateSpecificClassInvalidStartTime() {
        // tests that a specific class cannot be created with an invalid start time
        specificClassRequestDto.setStartTime(null);

        ResponseEntity<ErrorDto> response = restTemplate.postForEntity("/specificClass", specificClassRequestDto, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Invalid start time.", body.getErrors().get(0));

        specificClassRequestDto.setStartTime(START_TIME);
    }

    @Test
    @Order(6)
    public void testCreateSpecificClassInvalidHourDuration() {
        // tests that a specific class cannot be created with an invalid hour duration
        specificClassRequestDto.setHourDuration(-1);

        ResponseEntity<ErrorDto> response = restTemplate.postForEntity("/specificClass", specificClassRequestDto, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("The duration cannot be negative.", body.getErrors().get(0));

        specificClassRequestDto.setHourDuration(HOUR_DURATION);
    }

    @Test
    @Order(7)
    public void testCreateSpecificClassInvalidMaxCapacity() {
        // tests that a specific class cannot be created with an invalid max capacity
        specificClassRequestDto.setMaxCapacity(-1);

        ResponseEntity<ErrorDto> response = restTemplate.postForEntity("/specificClass", specificClassRequestDto, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Maximum capacity must be greater than 0.", body.getErrors().get(0));

        specificClassRequestDto.setMaxCapacity(MAX_CAPACITY);
    }

    @Test
    @Order(8)
    public void testCreateSpecificClassInvalidCurrentCapacity() {
        // tests that a specific class cannot be created with an invalid current capacity
        specificClassRequestDto.setCurrentCapacity(-1);

        ResponseEntity<ErrorDto> response = restTemplate.postForEntity("/specificClass", specificClassRequestDto, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Current capacity cannot be smaller than 0.", body.getErrors().get(0));

        specificClassRequestDto.setCurrentCapacity(CURRENT_CAPACITY);
    }

    @Test
    @Order(9)
    public void testCreateSpecificClassInvalidRegistrationFee() {
        // tests that a specific class cannot be created with an invalid registration fee
        specificClassRequestDto.setRegistrationFee(-1);

        ResponseEntity<ErrorDto> response = restTemplate.postForEntity("/specificClass", specificClassRequestDto, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Registration fee cannot be negative.", body.getErrors().get(0));

        specificClassRequestDto.setRegistrationFee(REGISTRATION_FEE);
    }

    @Test
    @Order(10)
    public void testCreateSpecificClassInvalidYear() {
        // tests that a specific class cannot be created with an invalid year
        specificClassRequestDto.setYear(2023);

        ResponseEntity<ErrorDto> response = restTemplate.postForEntity("/specificClass", specificClassRequestDto, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Schedule year does not match the date.", body.getErrors().get(0));

        specificClassRequestDto.setYear(YEAR);
    }

    @Test
    @Order(11)
    public void testCreateSpecificClassInvalidClassTypeApproval() {
        // tests that a specific class cannot be created with an unapproved class type
        ClassTypeRequestDto badClassTypeRequestDto = new ClassTypeRequestDto();
        badClassTypeRequestDto.setClassName("UnapprovedClassType");
        badClassTypeRequestDto.setDescription(DESCRIPTION);
        badClassTypeRequestDto.setIsApproved(false);
        
        ResponseEntity<ClassTypeResponseDto> badClassTypeResponseDto = restTemplate.postForEntity("/classType", badClassTypeRequestDto, ClassTypeResponseDto.class);
        assertEquals(HttpStatus.CREATED, badClassTypeResponseDto.getStatusCode());

        specificClassRequestDto.setClassType("UnapprovedClassType");

        ResponseEntity<ErrorDto> response = restTemplate.postForEntity("/specificClass", specificClassRequestDto, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Class type UnapprovedClassType is not approved.", body.getErrors().get(0));

        specificClassRequestDto.setClassType(CLASS_TYPE);
    }

    @Test
    @Order(11)
    public void testCreateSpecificClassTimeCollision() {
        // tests that a specific class cannot be created with a time collision
        SpecificClassRequestDto specificClassRequestDto2 = new SpecificClassRequestDto();
        specificClassRequestDto2.setClassId(CLASS_ID);
        specificClassRequestDto2.setClassType(CLASS_TYPE);
        specificClassRequestDto2.setYear(YEAR);
        specificClassRequestDto2.setSpecificClassName(SPECIFIC_CLASS_NAME + "Collision");
        specificClassRequestDto2.setDescription(DESCRIPTION);
        specificClassRequestDto2.setDate(DATE);
        specificClassRequestDto2.setStartTime(START_TIME.plusMinutes(30));
        specificClassRequestDto2.setHourDuration(HOUR_DURATION);
        specificClassRequestDto2.setMaxCapacity(MAX_CAPACITY);
        specificClassRequestDto2.setCurrentCapacity(CURRENT_CAPACITY);
        specificClassRequestDto2.setRegistrationFee(REGISTRATION_FEE);

        ResponseEntity<ErrorDto> response = restTemplate.postForEntity("/specificClass", specificClassRequestDto2, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("There is already a specific class at this time.", body.getErrors().get(0));
    }

    @Test
    @Order(12)
    public void testGetSpecificClassById() {
        // act
        // create a specific class
        specificClassRequestDto = new SpecificClassRequestDto();
        specificClassRequestDto.setClassId(CLASS_ID);
        specificClassRequestDto.setClassType(CLASS_TYPE);
        specificClassRequestDto.setYear(YEAR);
        specificClassRequestDto.setSpecificClassName(SPECIFIC_CLASS_NAME + "2");
        specificClassRequestDto.setDescription(DESCRIPTION);
        specificClassRequestDto.setDate(DATE);
        specificClassRequestDto.setStartTime(START_TIME);
        specificClassRequestDto.setHourDuration(HOUR_DURATION);
        specificClassRequestDto.setMaxCapacity(MAX_CAPACITY);
        specificClassRequestDto.setCurrentCapacity(CURRENT_CAPACITY);
        specificClassRequestDto.setRegistrationFee(REGISTRATION_FEE);

        ResponseEntity<SpecificClassResponseDto> response = restTemplate.postForEntity("/specificClass", specificClassRequestDto, SpecificClassResponseDto.class);

        int newClassId = response.getBody().getClassId();
        
        ResponseEntity<SpecificClassResponseDto> response2 = restTemplate.getForEntity("/specificClass/" + newClassId, SpecificClassResponseDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        
        SpecificClassResponseDto body = response2.getBody();
        assertNotNull(body);
        assertEquals(CLASS_TYPE, body.getClassType().getClassName());
        assertEquals(YEAR, body.getSchedule().getYear());
        assertEquals(SPECIFIC_CLASS_NAME + "2", body.getSpecificClassName());
        assertEquals(DESCRIPTION, body.getDescription());
        assertEquals(DATE.toString(), body.getDate().toString());
        assertEquals(Time.valueOf(START_TIME), Time.valueOf(body.getStartTime()));
        assertEquals(MAX_CAPACITY, body.getMaxCapacity());
        assertEquals(CURRENT_CAPACITY, body.getCurrentCapacity());
        assertEquals(REGISTRATION_FEE, body.getRegistrationFee());
    }

    @Test
    @Order(13)
    public void testGetSpecificClassByIdInvalidId() {
        // act
        ResponseEntity<ErrorDto> response = restTemplate.getForEntity("/specificClass/" + -1, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        
        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Specific class with id -1 not found.", body.getErrors().get(0));
    }

    @Test
    @Order(14)
    public void testUpdateSpecificClass() {
        // set up
        specificClassRequestDto.setSpecificClassName(SPECIFIC_CLASS_NAME + "Updated");
        specificClassRequestDto.setDescription(DESCRIPTION + "Updated");
        specificClassRequestDto.setDate(DATE.plusDays(1));
        specificClassRequestDto.setStartTime(START_TIME.plusHours(1));
        specificClassRequestDto.setHourDuration(HOUR_DURATION + 1);
        specificClassRequestDto.setMaxCapacity(MAX_CAPACITY + 1);
        specificClassRequestDto.setCurrentCapacity(CURRENT_CAPACITY + 1);
        specificClassRequestDto.setRegistrationFee(REGISTRATION_FEE + 1);

        // act
        ResponseEntity<SpecificClassResponseDto> response = restTemplate.exchange("/specificClass/" + CLASS_ID, HttpMethod.PUT, new HttpEntity<>(specificClassRequestDto), SpecificClassResponseDto.class);

        // assume there's an error
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        SpecificClassResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(CLASS_TYPE, body.getClassType().getClassName());
        assertEquals(YEAR, body.getSchedule().getYear());
        assertEquals(SPECIFIC_CLASS_NAME + "Updated", body.getSpecificClassName());
        assertEquals(DESCRIPTION + "Updated", body.getDescription());
        assertEquals(DATE.plusDays(1).toString(), body.getDate().toString());
        assertEquals(Time.valueOf(START_TIME.plusHours(1)), Time.valueOf(body.getStartTime()));
        assertEquals(MAX_CAPACITY + 1, body.getMaxCapacity());
        assertEquals(CURRENT_CAPACITY + 1, body.getCurrentCapacity());
        assertEquals(REGISTRATION_FEE + 1, body.getRegistrationFee());
    }

    @Test
    @Order(15)
    public void testUpdateSpecificClassInvalidClassType() {
        // tests that a specific class cannot be updated with an invalid class type
        specificClassRequestDto.setClassType("");

        ResponseEntity<ErrorDto> response = restTemplate.exchange("/specificClass/" + CLASS_ID, HttpMethod.PUT, new HttpEntity<>(specificClassRequestDto), ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Class type cannot be empty.", body.getErrors().get(0));

        specificClassRequestDto.setClassType(CLASS_TYPE);
    }

    @Test
    @Order(16)
    public void testUpdateSpecificClassInvalidDescription() {
        // tests that a specific class cannot be updated with an invalid description
        specificClassRequestDto.setDescription("");

        ResponseEntity<ErrorDto> response = restTemplate.exchange("/specificClass/" + CLASS_ID, HttpMethod.PUT, new HttpEntity<>(specificClassRequestDto), ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Description cannot be empty.", body.getErrors().get(0));

        specificClassRequestDto.setDescription(DESCRIPTION);
    }

    @Test
    @Order(17)
    public void testUpdateSpecificClassInvalidDate() {
        // tests that a specific class cannot be updated with an invalid date
        specificClassRequestDto.setDate(null);

        ResponseEntity<ErrorDto> response = restTemplate.exchange("/specificClass/" + CLASS_ID, HttpMethod.PUT, new HttpEntity<>(specificClassRequestDto), ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Invalid date.", body.getErrors().get(0));

        specificClassRequestDto.setDate(DATE);
    }

    @Test
    @Order(18)
    public void testUpdateSpecificClassInvalidStartTime() {
        // tests that a specific class cannot be updated with an invalid start time
        specificClassRequestDto.setStartTime(null);

        ResponseEntity<ErrorDto> response = restTemplate.exchange("/specificClass/" + CLASS_ID, HttpMethod.PUT, new HttpEntity<>(specificClassRequestDto), ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Invalid start time.", body.getErrors().get(0));

        specificClassRequestDto.setStartTime(START_TIME);
    }

    @Test
    @Order(19)
    public void testUpdateSpecificClassInvalidHourDuration() {
        // tests that a specific class cannot be updated with an invalid hour duration
        specificClassRequestDto.setHourDuration(-1);

        ResponseEntity<ErrorDto> response = restTemplate.exchange("/specificClass/" + CLASS_ID, HttpMethod.PUT, new HttpEntity<>(specificClassRequestDto), ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("The duration cannot be negative.", body.getErrors().get(0));

        specificClassRequestDto.setHourDuration(HOUR_DURATION);
    }

    @Test
    @Order(20)
    public void testUpdateSpecificClassInvalidMaxCapacity() {
        // tests that a specific class cannot be updated with an invalid max capacity
        specificClassRequestDto.setMaxCapacity(-1);

        ResponseEntity<ErrorDto> response = restTemplate.exchange("/specificClass/" + CLASS_ID, HttpMethod.PUT, new HttpEntity<>(specificClassRequestDto), ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Maximum capacity must be greater than 0.", body.getErrors().get(0));

        specificClassRequestDto.setMaxCapacity(MAX_CAPACITY);
    }

    @Test
    @Order(21)
    public void testUpdateSpecificClassInvalidCurrentCapacity() {
        // tests that a specific class cannot be updated with an invalid current capacity
        specificClassRequestDto.setCurrentCapacity(-1);

        ResponseEntity<ErrorDto> response = restTemplate.exchange("/specificClass/" + CLASS_ID, HttpMethod.PUT, new HttpEntity<>(specificClassRequestDto), ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Current capacity cannot be smaller than 0.", body.getErrors().get(0));

        specificClassRequestDto.setCurrentCapacity(CURRENT_CAPACITY);
    }

    @Test
    @Order(22)
    public void testUpdateSpecificClassInvalidRegistrationFee() {
        // tests that a specific class cannot be updated with an invalid registration fee
        specificClassRequestDto.setRegistrationFee(-1);

        ResponseEntity<ErrorDto> response = restTemplate.exchange("/specificClass/" + CLASS_ID, HttpMethod.PUT, new HttpEntity<>(specificClassRequestDto), ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Registration fee cannot be negative.", body.getErrors().get(0));

        specificClassRequestDto.setRegistrationFee(REGISTRATION_FEE);
    }

    @Test
    @Order(23)
    public void testUpdateSpecificClassInvalidYear() {
        // tests that a specific class cannot be updated with an invalid year
        specificClassRequestDto.setYear(2023);

        ResponseEntity<ErrorDto> response = restTemplate.exchange("/specificClass/" + CLASS_ID, HttpMethod.PUT, new HttpEntity<>(specificClassRequestDto), ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Schedule year does not match the date.", body.getErrors().get(0));

        specificClassRequestDto.setYear(YEAR);
    }

    @Test
    @Order(24)
    public void testUpdateSpecificClassCurrentCapacityGreaterThanMaxCapacity() {
        // tests that a specific class cannot be updated with a current capacity greater than the max capacity
        specificClassRequestDto.setCurrentCapacity(MAX_CAPACITY + 1);

        ResponseEntity<ErrorDto> response = restTemplate.exchange("/specificClass/" + CLASS_ID, HttpMethod.PUT, new HttpEntity<>(specificClassRequestDto), ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Current capacity must be less than or equal to the max capacity.", body.getErrors().get(0));

        specificClassRequestDto.setCurrentCapacity(CURRENT_CAPACITY);
    }

    @Test
    @Order(25)
    public void testUpdateSpecificClassInvalidClassTypeApproval() {
        // tests that a specific class cannot be updated with an unapproved class type
        ClassTypeRequestDto badClassTypeRequestDto = new ClassTypeRequestDto();
        badClassTypeRequestDto.setClassName("UnapprovedClassType2");
        badClassTypeRequestDto.setDescription(DESCRIPTION);
        badClassTypeRequestDto.setIsApproved(false);
        
        ResponseEntity<ClassTypeResponseDto> badClassTypeResponseDto = restTemplate.postForEntity("/classType", badClassTypeRequestDto, ClassTypeResponseDto.class);
        assertEquals(HttpStatus.CREATED, badClassTypeResponseDto.getStatusCode());

        specificClassRequestDto.setClassType("UnapprovedClassType2");

        ResponseEntity<ErrorDto> response = restTemplate.exchange("/specificClass/" + CLASS_ID, HttpMethod.PUT, new HttpEntity<>(specificClassRequestDto), ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Class type UnapprovedClassType2 is not approved.", body.getErrors().get(0));

        specificClassRequestDto.setClassType(CLASS_TYPE);
    }

    @Test
    @Order(26)
    public void testUpdateSpecificClassIdNotFound() {
        // tests that a specific class cannot be updated with an invalid id
        ResponseEntity<ErrorDto> response = restTemplate.exchange("/specificClass/" + -1, HttpMethod.PUT, new HttpEntity<>(specificClassRequestDto), ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        
        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Specific class with id -1 not found.", body.getErrors().get(0));
    }

    @Test
    @Order(26)
    public void testUpdateSpecificClassTimeCollision() {
        // tests that a specific class cannot be updated to with a time collision
        specificClassRequestDto.setStartTime(START_TIME.plusMinutes(30));

        ResponseEntity<ErrorDto> response = restTemplate.exchange("/specificClass/" + CLASS_ID, HttpMethod.PUT, new HttpEntity<>(specificClassRequestDto), ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("There is already a specific class at this time.", body.getErrors().get(0));

        specificClassRequestDto.setStartTime(START_TIME);
    }

    @Test
    @Order(27)
    public void testDeleteAllSpecificClasses() {
        // act
        restTemplate.exchange("/specificClass", HttpMethod.DELETE, null, ErrorDto.class);

        // assert
        ResponseEntity<SpecificClassListDto> response = restTemplate.getForEntity("/specificClass/year/" + YEAR, SpecificClassListDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        SpecificClassListDto body = response.getBody();
        assertNotNull(body);
        assertEquals(0, body.getSpecificClasses().size());
    }

    @Test
    @Order(28)
    public void testGetAllSpecificClasses() {
        // set up
        // create 3 specific classes
        SpecificClassRequestDto specificClassRequestDto1 = new SpecificClassRequestDto(-1, CLASS_TYPE, YEAR, SPECIFIC_CLASS_NAME + "1", DESCRIPTION, DATE, START_TIME, HOUR_DURATION, MAX_CAPACITY, CURRENT_CAPACITY, REGISTRATION_FEE);
        SpecificClassRequestDto specificClassRequestDto2 = new SpecificClassRequestDto(-1, CLASS_TYPE, YEAR, SPECIFIC_CLASS_NAME + "1", DESCRIPTION, DATE, START_TIME, HOUR_DURATION, MAX_CAPACITY, CURRENT_CAPACITY, REGISTRATION_FEE);
        SpecificClassRequestDto specificClassRequestDto3 = new SpecificClassRequestDto(-1, CLASS_TYPE, YEAR + 1, SPECIFIC_CLASS_NAME + "1", DESCRIPTION, LocalDate.of(YEAR + 1, 1, 1), START_TIME, HOUR_DURATION, MAX_CAPACITY, CURRENT_CAPACITY, REGISTRATION_FEE);

        restTemplate.postForEntity("/specificClass", specificClassRequestDto1, SpecificClassResponseDto.class);
        restTemplate.postForEntity("/specificClass", specificClassRequestDto2, SpecificClassResponseDto.class);
        ResponseEntity<SpecificClassResponseDto> response3 = restTemplate.postForEntity("/specificClass", specificClassRequestDto3, SpecificClassResponseDto.class);
        CLASS_ID = response3.getBody().getClassId();

        // act
        ResponseEntity<SpecificClassListDto> response = restTemplate.getForEntity("/specificClass/year/" + YEAR, SpecificClassListDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        SpecificClassListDto body = response.getBody();
        assertNotNull(body);
        assertEquals(2, body.getSpecificClasses().size());
        assertEquals(YEAR, body.getSpecificClasses().get(0).getSchedule().getYear());
        assertEquals(YEAR, body.getSpecificClasses().get(1).getSchedule().getYear());

        ResponseEntity<SpecificClassListDto> response1 = restTemplate.getForEntity("/specificClass/year/" + (YEAR + 1), SpecificClassListDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        SpecificClassListDto body1 = response1.getBody();
        assertNotNull(body1);
        assertEquals(1, body1.getSpecificClasses().size());
        assertEquals(YEAR + 1, body1.getSpecificClasses().get(0).getSchedule().getYear());
    }

    @Test
    @Order(29)
    public void testDeleteAllSpecificClassesByYear() {
        // act
        restTemplate.exchange("/specificClass/year/" + YEAR, HttpMethod.DELETE, null, ErrorDto.class);

        // assert
        ResponseEntity<SpecificClassListDto> response = restTemplate.getForEntity("/specificClass/year/" + YEAR, SpecificClassListDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        SpecificClassListDto body = response.getBody();
        assertNotNull(body);
        assertEquals(0, body.getSpecificClasses().size());
    }

    @Test
    @Order(30)
    public void testDeleteSpecificClassById() {
        // act
        restTemplate.exchange("/specificClass/" + CLASS_ID, HttpMethod.DELETE, null, ErrorDto.class);

        // assert
        ResponseEntity<SpecificClassListDto> response = restTemplate.getForEntity("/specificClass/year/" + (YEAR + 1), SpecificClassListDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        SpecificClassListDto body = response.getBody();
        assertNotNull(body);
        assertEquals(0, body.getSpecificClasses().size());
    }

    @Test
    @Order(31)
    public void testDeleteAllExistingSpecificClasses() {
        // act
        // create 3 specific classes
        SpecificClassRequestDto specificClassRequestDto1 = new SpecificClassRequestDto(-1, CLASS_TYPE, YEAR, SPECIFIC_CLASS_NAME + "1", DESCRIPTION, DATE, START_TIME, HOUR_DURATION, MAX_CAPACITY, CURRENT_CAPACITY, REGISTRATION_FEE);
        SpecificClassRequestDto specificClassRequestDto2 = new SpecificClassRequestDto(-1, CLASS_TYPE, YEAR, SPECIFIC_CLASS_NAME + "1", DESCRIPTION, DATE, START_TIME, HOUR_DURATION, MAX_CAPACITY, CURRENT_CAPACITY, REGISTRATION_FEE);
        SpecificClassRequestDto specificClassRequestDto3 = new SpecificClassRequestDto(-1, CLASS_TYPE, YEAR + 1, SPECIFIC_CLASS_NAME + "1", DESCRIPTION, LocalDate.of(YEAR + 1, 1, 1), START_TIME, HOUR_DURATION, MAX_CAPACITY, CURRENT_CAPACITY, REGISTRATION_FEE);

        restTemplate.postForEntity("/specificClass", specificClassRequestDto1, SpecificClassResponseDto.class);
        restTemplate.postForEntity("/specificClass", specificClassRequestDto2, SpecificClassResponseDto.class);
        restTemplate.postForEntity("/specificClass", specificClassRequestDto3, SpecificClassResponseDto.class);

        // act
        restTemplate.exchange("/specificClass", HttpMethod.DELETE, null, ErrorDto.class);

        // assert
        // check YEAR
        ResponseEntity<SpecificClassListDto> response = restTemplate.getForEntity("/specificClass/year/" + YEAR, SpecificClassListDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        SpecificClassListDto body = response.getBody();
        assertNotNull(body);
        assertEquals(0, body.getSpecificClasses().size());

        // check YEAR + 1
        ResponseEntity<SpecificClassListDto> response1 = restTemplate.getForEntity("/specificClass/year/" + (YEAR + 1), SpecificClassListDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        SpecificClassListDto body1 = response1.getBody();
        assertNotNull(body1);
        assertEquals(0, body1.getSpecificClasses().size());
    }
}