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
import ca.mcgill.ecse321.scs.dto.InstructorRequestDto;
import ca.mcgill.ecse321.scs.dto.InstructorResponseDto;
import ca.mcgill.ecse321.scs.dto.ClassTypeRequestDto;
import ca.mcgill.ecse321.scs.dto.ClassTypeResponseDto;
import ca.mcgill.ecse321.scs.dto.SpecificClassRequestDto;
import ca.mcgill.ecse321.scs.dto.SpecificClassResponseDto;
import ca.mcgill.ecse321.scs.dto.TeachingInfoRequestDto;
import ca.mcgill.ecse321.scs.dto.TeachingInfoResponseDto;
import ca.mcgill.ecse321.scs.dto.ScheduleRequestDto;
import ca.mcgill.ecse321.scs.dto.ScheduleResponseDto;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class TeachingInfoIntegrationTests {
    @Autowired
    private TestRestTemplate restTemplate;

    private int TEACHING_INFO_ID = -1;
    private int ACCOUNT_ID = 1;
    private int ACCOUNT_ID2 = 2;
    private int CLASS_ID = 1;

    private final String NAME = "John Doe";
    private final String EMAIL = "john@gmail.com";
    private final String EMAIL2 = "john2@gmail.com";
    private final String PASSWORD = "password";

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

    private TeachingInfoRequestDto teachingInfoRequestDto;

    @AfterAll
    public void clearDatabase() {
        restTemplate.exchange("/teachingInfos", HttpMethod.DELETE, null, ErrorDto.class);
        restTemplate.exchange("/specificClass", HttpMethod.DELETE, null, ErrorDto.class);
        restTemplate.exchange("/instructors", HttpMethod.DELETE, null, ErrorDto.class);
    }

    @BeforeAll
    public void createInstructorAndSpecificClass() {
        InstructorRequestDto instructorRequestDto = new InstructorRequestDto(ACCOUNT_ID, NAME, EMAIL, PASSWORD);

        InstructorRequestDto instructorRequestDto2 = new InstructorRequestDto();
        instructorRequestDto2.setId(ACCOUNT_ID2);
        instructorRequestDto2.setName(NAME);
        instructorRequestDto2.setEmail(EMAIL2);
        instructorRequestDto2.setPassword(PASSWORD);
        
        ResponseEntity<InstructorResponseDto> instructorResponseDto = restTemplate.postForEntity("/instructors", instructorRequestDto, InstructorResponseDto.class);
        assertEquals(HttpStatus.CREATED, instructorResponseDto.getStatusCode());
        ResponseEntity<InstructorResponseDto> instructorResponseDto2 = restTemplate.postForEntity("/instructors", instructorRequestDto2, InstructorResponseDto.class);
        assertEquals(HttpStatus.CREATED, instructorResponseDto2.getStatusCode());

        // get the instructor account id
        InstructorResponseDto instructorResponseDtoBody = instructorResponseDto.getBody();
        assertNotNull(instructorResponseDtoBody);
        ACCOUNT_ID = instructorResponseDtoBody.getId();

        // get the instructor2 account id
        InstructorResponseDto instructorResponseDtoBody2 = instructorResponseDto2.getBody();
        assertNotNull(instructorResponseDtoBody2);
        ACCOUNT_ID2 = instructorResponseDtoBody2.getId();

        ClassTypeRequestDto classTypeRequestDto = new ClassTypeRequestDto();
        classTypeRequestDto = new ClassTypeRequestDto(CLASS_TYPE, CLASS_TYPE_DESCRIPTION, IS_APPROVED);

        ResponseEntity<ClassTypeResponseDto> classTypeResponseDto = restTemplate.postForEntity("/classType", classTypeRequestDto, ClassTypeResponseDto.class);
        assertEquals(HttpStatus.CREATED, classTypeResponseDto.getStatusCode());

        ScheduleRequestDto scheduleRequestDto = new ScheduleRequestDto(YEAR);

        ResponseEntity<ScheduleResponseDto> scheduleResponseDto = restTemplate.postForEntity("/schedule", scheduleRequestDto, ScheduleResponseDto.class);
        assertEquals(HttpStatus.CREATED, scheduleResponseDto.getStatusCode());

        SpecificClassRequestDto specificClassRequestDto = new SpecificClassRequestDto(CLASS_ID, CLASS_TYPE, YEAR, SPECIFIC_CLASS_NAME, DESCRIPTION, DATE, START_TIME, HOUR_DURATION, MAX_CAPACITY, CURRENT_CAPACITY, REGISTRATION_FEE);
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
    public void testCreateTeachingInfo() {
        // set up
        teachingInfoRequestDto = new TeachingInfoRequestDto(TEACHING_INFO_ID, ACCOUNT_ID, CLASS_ID);

        // act
        ResponseEntity<TeachingInfoResponseDto> response = restTemplate.postForEntity("/teachingInfo", teachingInfoRequestDto, TeachingInfoResponseDto.class);
        TEACHING_INFO_ID = response.getBody().getTeachingInfoId();

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        
        TeachingInfoResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(ACCOUNT_ID, body.getInstructor().getId());
        assertEquals(CLASS_ID, body.getSpecificClass().getClassId());
    }

    @Test
    @Order(2)
    public void testCreateTeachingInfoInvalidInstructorId() {
        // tests that a teaching info cannot be created with an invalid instructor id
        teachingInfoRequestDto.setAccountId(-1);

        ResponseEntity<ErrorDto> response = restTemplate.postForEntity("/teachingInfo", teachingInfoRequestDto, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Instructor not found.", body.getErrors().get(0));

        teachingInfoRequestDto.setAccountId(ACCOUNT_ID);
    }

    @Test
    @Order(3)
    public void testCreateTeachingInfoInvalidClassId() {
        // tests that a teaching info cannot be created with an invalid class id
        teachingInfoRequestDto.setClassId(-1);

        ResponseEntity<ErrorDto> response = restTemplate.postForEntity("/teachingInfo", teachingInfoRequestDto, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Specific class with id -1 not found.", body.getErrors().get(0));

        teachingInfoRequestDto.setClassId(CLASS_ID);
    }

    @Test
    @Order(4)
    public void testGetTeachingInfoById() {
        // act
        ResponseEntity<TeachingInfoResponseDto> response = restTemplate.getForEntity("/teachingInfo/" + TEACHING_INFO_ID, TeachingInfoResponseDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        TeachingInfoResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(ACCOUNT_ID, body.getInstructor().getId());
        assertEquals(CLASS_ID, body.getSpecificClass().getClassId());
    }

    @Test
    @Order(5)
    public void testGetTeachingInfoByIdInvalidId() {
        // act
        ResponseEntity<ErrorDto> response = restTemplate.getForEntity("/teachingInfo/" + -1, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        
        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Teaching info with id -1 not found.", body.getErrors().get(0));
    }

    @Test
    @Order(6)
    public void testUpdateTeachingInfo() {
        // set up
        teachingInfoRequestDto.setAccountId(ACCOUNT_ID2);
        teachingInfoRequestDto.setClassId(CLASS_ID);

        // act
        ResponseEntity<TeachingInfoResponseDto> response = restTemplate.exchange("/teachingInfo/" + TEACHING_INFO_ID, HttpMethod.PUT, new HttpEntity<>(teachingInfoRequestDto), TeachingInfoResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        TeachingInfoResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(ACCOUNT_ID2, body.getInstructor().getId());
        assertEquals(CLASS_ID, body.getSpecificClass().getClassId());
    }

    @Test
    @Order(7)
    public void testUpdateTeachingInfoInvalidInstructorId() {
        // tests that a teaching info cannot be updated with an invalid instructor id
        teachingInfoRequestDto.setAccountId(-1);

        ResponseEntity<ErrorDto> response = restTemplate.exchange("/teachingInfo/" + TEACHING_INFO_ID, HttpMethod.PUT, new HttpEntity<>(teachingInfoRequestDto), ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Instructor not found.", body.getErrors().get(0));

        teachingInfoRequestDto.setAccountId(ACCOUNT_ID2);
    }

    @Test
    @Order(8)
    //test get teaching info by class id
    public void testGetTeachingInfoByClassId() {
        // act
        ResponseEntity<TeachingInfoResponseDto> response = restTemplate.getForEntity("/specificClass/" + CLASS_ID + "/teachingInfo", TeachingInfoResponseDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        TeachingInfoResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(ACCOUNT_ID2, body.getInstructor().getId());
        assertEquals(CLASS_ID, body.getSpecificClass().getClassId());
    }

    @Test
    @Order(9)
    // test to delete a teaching info
    public void testDeleteTeachingInfo() {
        // act
        ResponseEntity<Void> response = restTemplate.exchange("/teachingInfo/" + TEACHING_INFO_ID, HttpMethod.DELETE, null, Void.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }    
}