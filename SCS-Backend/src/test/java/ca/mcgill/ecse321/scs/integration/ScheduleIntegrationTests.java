package ca.mcgill.ecse321.scs.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.scs.model.Schedule;
import ca.mcgill.ecse321.scs.dto.ErrorDto;
import ca.mcgill.ecse321.scs.dto.ScheduleListDto;
import ca.mcgill.ecse321.scs.dto.ScheduleRequestDto;
import ca.mcgill.ecse321.scs.dto.ScheduleResponseDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ScheduleIntegrationTests {
    @Autowired
    private TestRestTemplate client;

    private final int YEAR = 2023;

    @Test
    @Order(1)
    public void testCreateSchedule() {
        // set up
        ScheduleRequestDto request = new ScheduleRequestDto(new Schedule(YEAR));

        // act
        ResponseEntity<ScheduleResponseDto> response = client.postForEntity("/schedule", request, ScheduleResponseDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        
        ScheduleResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(YEAR, body.getYear());
    }

    @Test
    @Order(2)
    public void testCreateScheduleYearExists() {
        // tests that a schedule cannot be created if the year already exists

        // set up
        ScheduleRequestDto request = new ScheduleRequestDto(YEAR);

        // act
        ResponseEntity<ErrorDto> response = client.postForEntity("/schedule", request, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Schedule for year " + YEAR + " already exists.", body.getErrors().get(0));
    }

    @Test
    @Order(3)
    public void testCreateScheduleInvalidYear() {
        // tests that a schedule cannot be created with an invalid year

        // set up
        ScheduleRequestDto request = new ScheduleRequestDto(-2000);

        // act
        ResponseEntity<ErrorDto> response = client.postForEntity("/schedule", request, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Year cannot be negative.", body.getErrors().get(0));
    }

    @Test
    @Order(4)
    public void testGetSchedule() {
        // act
        ResponseEntity<ScheduleResponseDto> response = client.getForEntity("/schedule/" + YEAR, ScheduleResponseDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        ScheduleResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(YEAR, body.getYear());
    }

    @Test
    @Order(5)
    public void testGetScheduleYearDoesNotExist() {
        // tests that a schedule cannot be retrieved if the year does not exist

        // act
        int unknownYear = YEAR + 1;
        ResponseEntity<ErrorDto> response = client.getForEntity("/schedule/" + unknownYear, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Schedule for year " + unknownYear + " not found.", body.getErrors().get(0));
    }

    @Test
    @Order(6)
    public void testGetAllSchedules() {
        // act
        ResponseEntity<ScheduleListDto> response = client.getForEntity("/schedules", ScheduleListDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ScheduleListDto body = response.getBody();
        assertNotNull(body);
        assertTrue(body.getSchedules().size() > 0);
        assertEquals(YEAR, body.getSchedules().get(0).getYear());
    }

    @SuppressWarnings("null")
    @Test
    @Order(7)
    public void testDeleteSchedule() {
        // Perform DELETE request
        ResponseEntity<Void> response = client.exchange("/schedule/{year}", HttpMethod.DELETE, null, Void.class, YEAR);

        // Assert the response status code
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Assert that the schedule was deleted
        ResponseEntity<ErrorDto> getResponse = client.getForEntity("/schedule/" + YEAR, ErrorDto.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
        assertEquals("Schedule for year " + YEAR + " not found.", getResponse.getBody().getErrors().get(0));
    }

    @Test
    @Order(8)
    public void testDeleteScheduleYearDoesNotExist() {
        // tests that a schedule cannot be deleted if the year does not exist

        // act
        int unknownYear = YEAR + 1;
        ResponseEntity<ErrorDto> response = client.exchange("/schedule/{year}", HttpMethod.DELETE, null, ErrorDto.class, unknownYear);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Schedule for year " + unknownYear + " not found.", body.getErrors().get(0));
    }

    @SuppressWarnings("null")
    @Test
    @Order(9)
    public void testDeleteAllSchedules() {
        // create schedules
        client.postForEntity("/schedule", new ScheduleRequestDto(YEAR), ScheduleResponseDto.class);
        client.postForEntity("/schedule", new ScheduleRequestDto(YEAR + 1), ScheduleResponseDto.class);

        // Perform DELETE request
        ResponseEntity<Void> response = client.exchange("/schedules", HttpMethod.DELETE, null, Void.class);

        // Assert the response status code
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Assert that all schedules were deleted
        ResponseEntity<ScheduleListDto> getResponse = client.getForEntity("/schedules", ScheduleListDto.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals(0, getResponse.getBody().getSchedules().size());
    }
}