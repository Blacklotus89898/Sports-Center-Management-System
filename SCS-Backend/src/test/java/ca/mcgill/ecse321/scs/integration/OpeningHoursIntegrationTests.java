package ca.mcgill.ecse321.scs.integration;

import ca.mcgill.ecse321.scs.dto.ErrorDto;
import ca.mcgill.ecse321.scs.dto.OpeningHoursDto;
import ca.mcgill.ecse321.scs.dto.ScheduleRequestDto;
import ca.mcgill.ecse321.scs.dto.ScheduleResponseDto;
import ca.mcgill.ecse321.scs.model.OpeningHours.DayOfWeek;
import ca.mcgill.ecse321.scs.model.Schedule;

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
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * This class contains integration tests for the OpeningHoursController.
 * It tests the creation, retrieval, and updating of opening hours.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class OpeningHoursIntegrationTests {
    @Autowired
    private TestRestTemplate restTemplate;

    private final int YEAR = 2022;
    private final String DAY = "MONDAY";
    private final LocalTime OPEN_TIME = LocalTime.of(9, 0);
    private final LocalTime CLOSE_TIME = LocalTime.of(17, 0);

    @AfterAll
    public void clearDatabase() {
        restTemplate.exchange("/schedules", HttpMethod.DELETE, null, Void.class);
    }

    @BeforeAll
    public void createSchedule() {
        ScheduleRequestDto request = new ScheduleRequestDto(new Schedule(YEAR));
        restTemplate.postForEntity("/schedule", request, ScheduleResponseDto.class);
    }

    @Test
    @Order(1)
    public void testCreateOpeningHours() {
        // set up
        OpeningHoursDto request = new OpeningHoursDto(DayOfWeek.valueOf(DAY), OPEN_TIME, CLOSE_TIME, YEAR);

        // act
        ResponseEntity<OpeningHoursDto> response = restTemplate.postForEntity("/schedules/" + YEAR + "/openingHours", request, OpeningHoursDto.class);

        // assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(DAY, response.getBody().getDayOfWeek().toString());
        assertEquals(OPEN_TIME, response.getBody().getOpenTime());
        assertEquals(CLOSE_TIME, response.getBody().getCloseTime());
        assertEquals(YEAR, response.getBody().getYear());
    }
    
    @Test
    @Order(2)
    public void testCreateOpeningHoursInvalidDay() {
        // set up
        OpeningHoursDto request = new OpeningHoursDto(null, OPEN_TIME, CLOSE_TIME, YEAR);

        // act
        ResponseEntity<ErrorDto> response = restTemplate.postForEntity("/schedules/" + YEAR + "/openingHours", request, ErrorDto.class);

        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Invalid day of week.", body.getErrors().get(0));
    }

    @Test
    @Order(3)
    public void testCreateOpeningHoursInvalidOpenTime() {
        // set up
        OpeningHoursDto request = new OpeningHoursDto(DayOfWeek.valueOf(DAY), null, CLOSE_TIME, YEAR);

        // act
        ResponseEntity<ErrorDto> response = restTemplate.postForEntity("/schedules/" + YEAR + "/openingHours", request, ErrorDto.class);

        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Time is invalid.", body.getErrors().get(0));
    }

    @Test
    @Order(4)
    public void testCreateOpeningHoursInvalidCloseTime() {
        // set up
        OpeningHoursDto request = new OpeningHoursDto(DayOfWeek.valueOf(DAY), OPEN_TIME, null, YEAR);

        // act
        ResponseEntity<ErrorDto> response = restTemplate.postForEntity("/schedules/" + YEAR + "/openingHours", request, ErrorDto.class);

        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Time is invalid.", body.getErrors().get(0));
    }

    @Test
    @Order(5)
    public void testCreateOpeningHoursInvalidYear() {
        // set up
        OpeningHoursDto request = new OpeningHoursDto(DayOfWeek.valueOf(DAY), OPEN_TIME, CLOSE_TIME, 0);

        // act
        ResponseEntity<ErrorDto> response = restTemplate.postForEntity("/schedules/" + YEAR + "/openingHours", request, ErrorDto.class);

        // assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Schedule for year 0 not found.", body.getErrors().get(0));
    }

    @Test
    @Order(6)
    public void testCreateOpeningHoursCloseTimeBeforeOpeningTime() {
        // set up
        OpeningHoursDto request = new OpeningHoursDto(DayOfWeek.valueOf(DAY), CLOSE_TIME, OPEN_TIME, YEAR);

        // act
        ResponseEntity<ErrorDto> response = restTemplate.postForEntity("/schedules/" + YEAR + "/openingHours", request, ErrorDto.class);

        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Close time cannot be before open time.", body.getErrors().get(0));
    }

    @Test
    @Order(7)
    public void testCreateOpeningHoursDuplicate() {
        // set up
        OpeningHoursDto request = new OpeningHoursDto(DayOfWeek.valueOf(DAY), OPEN_TIME, CLOSE_TIME, YEAR);

        // act
        ResponseEntity<ErrorDto> response = restTemplate.postForEntity("/schedules/" + YEAR + "/openingHours", request, ErrorDto.class);

        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Opening hours with day " + DAY + " already exists.", body.getErrors().get(0));
    }

    @Test
    @Order(8)
    public void testGetOpeningHoursByDay() {
        // act
        ResponseEntity<OpeningHoursDto> response = restTemplate.getForEntity("/schedules/" + YEAR + "/openingHours/" + DAY, OpeningHoursDto.class);

        // assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(DAY, response.getBody().getDayOfWeek().toString());
        assertEquals(OPEN_TIME, response.getBody().getOpenTime());
        assertEquals(CLOSE_TIME, response.getBody().getCloseTime());
        assertEquals(YEAR, response.getBody().getYear());
    }

    @Test
    @Order(9)
    public void testGetOpeningHoursByDayNotFound() {
        // act
        ResponseEntity<ErrorDto> response = restTemplate.getForEntity("/schedules/" + YEAR + "/openingHours/FRIDAY", ErrorDto.class);

        // assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Opening hours for day FRIDAY does not exist for the year " + YEAR + ".", body.getErrors().get(0));
    }

    @Test
    @Order(10)
    public void testGettingOpeningHoursInvalidDay() {
        // act
        ResponseEntity<ErrorDto> response = restTemplate.getForEntity("/schedules/" + YEAR + "/openingHours/invalid", ErrorDto.class);

        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Invalid day of week.", body.getErrors().get(0));
    }

    @Test
    @Order(11)
    public void testUpdateOpeningHours() {
        // set up
        OpeningHoursDto request = new OpeningHoursDto(DayOfWeek.valueOf(DAY), LocalTime.of(8, 0), LocalTime.of(16, 0), YEAR);

        // act
        ResponseEntity<OpeningHoursDto> response = restTemplate.exchange("/schedules/" + YEAR + "/openingHours/" + DAY, HttpMethod.PUT, new HttpEntity<>(request), OpeningHoursDto.class);

        // assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(DAY, response.getBody().getDayOfWeek().toString());
        assertEquals(LocalTime.of(8, 0), response.getBody().getOpenTime());
        assertEquals(LocalTime.of(16, 0), response.getBody().getCloseTime());
        assertEquals(YEAR, response.getBody().getYear());
    }

    @Test
    @Order(12)
    public void testUpdateOpeningHoursInvalidDay() {
        // set up
        OpeningHoursDto request = new OpeningHoursDto(null, LocalTime.of(8, 0), LocalTime.of(16, 0), YEAR);

        // act
        ResponseEntity<ErrorDto> response = restTemplate.exchange("/schedules/" + YEAR + "/openingHours/invalidDay", HttpMethod.PUT, new HttpEntity<>(request), ErrorDto.class);

        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Invalid day of week.", body.getErrors().get(0));
    }

    @Test
    @Order(13)
    public void testUpdateOpeningHoursInvalidOpenTime() {
        // set up
        OpeningHoursDto request = new OpeningHoursDto(DayOfWeek.valueOf(DAY), null, LocalTime.of(16, 0), YEAR);

        // act
        ResponseEntity<ErrorDto> response = restTemplate.exchange("/schedules/" + YEAR + "/openingHours/" + DAY, HttpMethod.PUT, new HttpEntity<>(request), ErrorDto.class);

        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Time cannot be empty.", body.getErrors().get(0));
    }

    @Test
    @Order(14)
    public void testUpdateOpeningHoursInvalidCloseTime() {
        // set up
        OpeningHoursDto request = new OpeningHoursDto(DayOfWeek.valueOf(DAY), LocalTime.of(8, 0), null, YEAR);

        // act
        ResponseEntity<ErrorDto> response = restTemplate.exchange("/schedules/" + YEAR + "/openingHours/" + DAY, HttpMethod.PUT, new HttpEntity<>(request), ErrorDto.class);

        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Time cannot be empty.", body.getErrors().get(0));
    }

    @Test
    @Order(15)
    public void testUpdateOpeningHoursInvalidYear() {
        // set up
        OpeningHoursDto request = new OpeningHoursDto(DayOfWeek.valueOf(DAY), LocalTime.of(8, 0), LocalTime.of(16, 0), 0);

        // act
        ResponseEntity<ErrorDto> response = restTemplate.exchange("/schedules/" + YEAR + "/openingHours/" + DAY, HttpMethod.PUT, new HttpEntity<>(request), ErrorDto.class);

        // assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Schedule for year 0 not found.", body.getErrors().get(0));
    }

    @Test
    @Order(16)
    public void testUpdateOpeningHoursCloseTimeBeforeOpeningTime() {
        // set up
        OpeningHoursDto request = new OpeningHoursDto(DayOfWeek.valueOf(DAY), LocalTime.of(16, 0), LocalTime.of(8, 0), YEAR);

        // act
        ResponseEntity<ErrorDto> response = restTemplate.exchange("/schedules/" + YEAR + "/openingHours/" + DAY, HttpMethod.PUT, new HttpEntity<>(request), ErrorDto.class);

        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Close time cannot be before open time.", body.getErrors().get(0));
    }

    @Test
    @Order(17)
    public void testUpdateOpeningHoursNotFound() {
        // set up
        OpeningHoursDto request = new OpeningHoursDto(DayOfWeek.valueOf("FRIDAY"), LocalTime.of(8, 0), LocalTime.of(16, 0), YEAR);

        // act
        ResponseEntity<ErrorDto> response = restTemplate.exchange("/schedules/" + YEAR + "/openingHours/FRIDAY", HttpMethod.PUT, new HttpEntity<>(request), ErrorDto.class);

        // assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Opening hours with day FRIDAY not found.", body.getErrors().get(0));
    }

    @Test
    @Order(18)
    public void testDeleteOpeningHours() {
        // act
        ResponseEntity<Void> response = restTemplate.exchange("/schedules/" + YEAR + "/openingHours/" + DAY, HttpMethod.DELETE, null, Void.class);

        // assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @Order(19)
    public void testDeleteOpeningHoursNotFound() {
        // act
        ResponseEntity<ErrorDto> response = restTemplate.exchange("/schedules/" + YEAR + "/openingHours/FRIDAY", HttpMethod.DELETE, null, ErrorDto.class);

        // assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Opening hours with day FRIDAY not found for the year " + YEAR + ".", body.getErrors().get(0));
    }

    @Test
    @Order(20)
    public void testDeleteOpeningHoursInvalidDay() {
        // act
        ResponseEntity<ErrorDto> response = restTemplate.exchange("/schedules/" + YEAR + "/openingHours/invalid", HttpMethod.DELETE, null, ErrorDto.class);

        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Invalid day of week.", body.getErrors().get(0));
    }

    @Test
    @Order(21)
    public void testDeleteAllAndGetAllOpeningHours() {
        // set up
        OpeningHoursDto request = new OpeningHoursDto(DayOfWeek.valueOf(DAY), OPEN_TIME, CLOSE_TIME, YEAR);
        restTemplate.postForEntity("/schedules/" + YEAR + "/openingHours", request, OpeningHoursDto.class);
        OpeningHoursDto request2 = new OpeningHoursDto(DayOfWeek.valueOf("SATURDAY"), OPEN_TIME, CLOSE_TIME, YEAR);
        restTemplate.postForEntity("/schedules/" + YEAR + "/openingHours", request2, OpeningHoursDto.class);

        // ensure that the opening hours were created
        ResponseEntity<OpeningHoursDto[]> response1 = restTemplate.getForEntity("/schedules/" + YEAR + "/openingHours", OpeningHoursDto[].class);
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        assertNotNull(response1.getBody());
        assertEquals(2, response1.getBody().length);

        // act
        ResponseEntity<Void> response = restTemplate.exchange("/schedules/" + YEAR + "/openingHours", HttpMethod.DELETE, null, Void.class);

        // assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        ResponseEntity<OpeningHoursDto[]> response2 = restTemplate.getForEntity("/schedules/" + YEAR + "/openingHours", OpeningHoursDto[].class);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertNotNull(response2.getBody());
        assertEquals(0, response2.getBody().length);
    }
}