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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.scs.model.Schedule;
import ca.mcgill.ecse321.scs.dto.ErrorDto;
import ca.mcgill.ecse321.scs.dto.ScheduleRequestDto;
import ca.mcgill.ecse321.scs.dto.ScheduleResponseDto;
import ca.mcgill.ecse321.scs.dto.CustomHoursListDto;
import ca.mcgill.ecse321.scs.dto.CustomHoursRequestDto;
import ca.mcgill.ecse321.scs.dto.CustomHoursResponseDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class CustomHoursIntegrationTests {
    @Autowired
    private TestRestTemplate client;

    private final String NAME = "cny";
    private final String DESCRIPTION = "chinese new year";
    private final LocalDate DATE = LocalDate.of(2023, 1, 1);
    private final LocalTime OPEN_TIME = LocalTime.of(9, 0);
    private final LocalTime CLOSE_TIME = LocalTime.of(17, 0);
    private final int YEAR = 2023;

    @AfterAll
    public void clearDatabase() {
        client.exchange("/schedules", HttpMethod.DELETE, null, Void.class);
    }

    @BeforeAll
    public void createSchedule() {
        // create a schedule, which is required for creating custom hours
        // no need to test the creation of a schedule since it is already tested in ScheduleIntegrationTests
        ScheduleRequestDto request = new ScheduleRequestDto(new Schedule(YEAR));
        client.postForEntity("/schedule", request, ScheduleResponseDto.class);
    }

    @Test
    @Order(1)
    public void testCreateCustomHours() {
       // set up
       CustomHoursRequestDto request = new CustomHoursRequestDto(NAME, DESCRIPTION, DATE, OPEN_TIME, CLOSE_TIME, YEAR);

       // act
       ResponseEntity<CustomHoursResponseDto> response = client.postForEntity("/customHours", request, CustomHoursResponseDto.class);
       
       // assert
       assertNotNull(response);
       assertEquals(HttpStatus.CREATED, response.getStatusCode());
       
       CustomHoursResponseDto body = response.getBody();
       assertNotNull(body);
       assertEquals(NAME, body.getName());
       assertEquals(DESCRIPTION, body.getDescription());
       assertEquals(DATE, body.getDate());
       assertEquals(OPEN_TIME, body.getOpenTime());
       assertEquals(CLOSE_TIME, body.getCloseTime());
       assertEquals(YEAR, body.getSchedule().getYear());
    }

    @Test
    @Order(2)
    public void testCreateCustomHoursNullDate() {
        // tests that a custom hours cannot be created with a null date

        // set up
        CustomHoursRequestDto request = new CustomHoursRequestDto(NAME + "1", DESCRIPTION, null, OPEN_TIME, CLOSE_TIME, YEAR);

        // act
        ResponseEntity<ErrorDto> response = client.postForEntity("/customHours", request, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Date or Time cannot be empty.", body.getErrors().get(0));
    }

    @Test
    @Order(3)
    public void testCreateCustomHoursNullOpenTime() {
        // tests that a custom hours cannot be created with a null date

        // set up
        CustomHoursRequestDto request = new CustomHoursRequestDto(NAME + "1", DESCRIPTION, DATE, null, CLOSE_TIME, YEAR);

        // act
        ResponseEntity<ErrorDto> response = client.postForEntity("/customHours", request, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Date or Time cannot be empty.", body.getErrors().get(0));
    }

    @Test
    @Order(4)
    public void testCreateCustomHoursNullCloseTime() {
        // tests that a custom hours cannot be created with a null date

        // set up
        CustomHoursRequestDto request = new CustomHoursRequestDto(NAME + "1", DESCRIPTION, DATE, OPEN_TIME, null, YEAR);

        // act
        ResponseEntity<ErrorDto> response = client.postForEntity("/customHours", request, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Date or Time cannot be empty.", body.getErrors().get(0));
    }

    @Test
    @Order(5)
    public void testCreateCustomHoursNameExists() {
        // tests that a custom hours cannot be created if the name already exists

        // set up
        CustomHoursRequestDto request = new CustomHoursRequestDto(NAME, DESCRIPTION, DATE, OPEN_TIME, CLOSE_TIME, YEAR);

        // act
        ResponseEntity<ErrorDto> response = client.postForEntity("/customHours", request, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Custom hours with name " + NAME + " already exists.", body.getErrors().get(0));
    }

    @Test
    @Order(6)
    public void testCreateCustomHoursInvalidName() {
        // tests that a custom hours cannot be created with an invalid name

        // set up
        CustomHoursRequestDto request = new CustomHoursRequestDto("", DESCRIPTION, DATE, OPEN_TIME, CLOSE_TIME, YEAR);

        // act
        ResponseEntity<ErrorDto> response = client.postForEntity("/customHours", request, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Name cannot be empty.", body.getErrors().get(0));
    }

    @Test
    @Order(7)
    public void testCreateCustomHoursInvalidDescription() {
        // tests that a custom hours cannot be created with an invalid description

        // set up
        CustomHoursRequestDto request = new CustomHoursRequestDto(NAME + "1", "", DATE, OPEN_TIME, CLOSE_TIME, YEAR);

        // act
        ResponseEntity<ErrorDto> response = client.postForEntity("/customHours", request, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Description cannot be empty.", body.getErrors().get(0));
    }

    @Test
    @Order(8)
    public void testCreateCustomHoursInvalidYear() {
        // tests that a custom hours cannot be created with a year mismatch

        // set up
        CustomHoursRequestDto request = new CustomHoursRequestDto(NAME + "2", DESCRIPTION, LocalDate.of(2022, 1, 1), OPEN_TIME, CLOSE_TIME, YEAR);

        // act
        ResponseEntity<ErrorDto> response = client.postForEntity("/customHours", request, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Year of date does not match year of schedule.", body.getErrors().get(0));
    }

    @Test
    @Order(9)
    public void testCreateCustomHoursInvalidDate() {
        // test that the closeTime cannot be before the openTime
        // set up
        CustomHoursRequestDto request = new CustomHoursRequestDto(NAME + "3", DESCRIPTION, DATE, CLOSE_TIME, OPEN_TIME, YEAR);

        // act
        ResponseEntity<ErrorDto> response = client.postForEntity("/customHours", request, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Close time cannot be before open time.", body.getErrors().get(0));

    }

    @Test
    @Order(10)
    public void testGetCustomHoursByName() {
        // act
        ResponseEntity<CustomHoursResponseDto> response = client.getForEntity("/customHours/" + NAME, CustomHoursResponseDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        CustomHoursResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(NAME, body.getName());
        assertEquals(DESCRIPTION, body.getDescription());
        assertEquals(DATE, body.getDate());
        assertEquals(OPEN_TIME, body.getOpenTime());
        assertEquals(CLOSE_TIME, body.getCloseTime());
        assertEquals(YEAR, body.getSchedule().getYear());
    }

    @Test
    @Order(11)
    public void testGetCustomHoursByInvalidName() {
        // act
        ResponseEntity<ErrorDto> response = client.getForEntity("/customHours/" + NAME + "1", ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Custom hours with name " + NAME + "1 does not exist.", body.getErrors().get(0));
    }

    @Test
    @Order(12)
    public void testGetCustomHoursByDate() {
        // act
        ResponseEntity<CustomHoursResponseDto> response = client.getForEntity("/customHours/date/" + DATE, CustomHoursResponseDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        CustomHoursResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(NAME, body.getName());
        assertEquals(DESCRIPTION, body.getDescription());
        assertEquals(DATE, body.getDate());
        assertEquals(OPEN_TIME, body.getOpenTime());
        assertEquals(CLOSE_TIME, body.getCloseTime());
        assertEquals(YEAR, body.getSchedule().getYear());
    }

    @Test
    @Order(13)
    public void testGetCustomHoursByInvalidDate() {
        // act
        ResponseEntity<ErrorDto> response = client.getForEntity("/customHours/date/" + LocalDate.of(2023, 1, 2), ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Custom hours for date 2023-01-02 does not exist.", body.getErrors().get(0));
    }

    @Test
    @Order(14)
    public void testUpdateCustomHoursDescription() {
        // set up
        String newDescription = "new description";
        CustomHoursRequestDto request = new CustomHoursRequestDto(NAME, newDescription, DATE, OPEN_TIME, CLOSE_TIME, YEAR);

        // wrap the request in an HttpEntity
        HttpEntity<CustomHoursRequestDto> requestEntity = new HttpEntity<>(request);

        // act
        ResponseEntity<CustomHoursResponseDto> response = client.exchange("/customHours/" + NAME, HttpMethod.PUT, requestEntity, CustomHoursResponseDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        CustomHoursResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(NAME, body.getName());
        assertEquals(newDescription, body.getDescription());
        assertEquals(DATE, body.getDate());
        assertEquals(OPEN_TIME, body.getOpenTime());
        assertEquals(CLOSE_TIME, body.getCloseTime());
        assertEquals(YEAR, body.getSchedule().getYear());
    }

    @Test
    @Order(15)
    public void testUpdateCustomHoursInvalidDescription() {
        // set up
        CustomHoursRequestDto request = new CustomHoursRequestDto(NAME, "", DATE, OPEN_TIME, CLOSE_TIME, YEAR);

        // wrap the request in an HttpEntity
        HttpEntity<CustomHoursRequestDto> requestEntity = new HttpEntity<>(request);

        // act
        ResponseEntity<ErrorDto> response = client.exchange("/customHours/" + NAME, HttpMethod.PUT, requestEntity, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Description cannot be empty.", body.getErrors().get(0));
    }

    @Test
    @Order(16)
    public void testUpdateCustomHoursOpeningTime() {
        // set up
        LocalTime newOpenTime = LocalTime.of(10, 0);
        CustomHoursRequestDto request = new CustomHoursRequestDto(NAME, DESCRIPTION, DATE, newOpenTime, CLOSE_TIME, YEAR);

        // wrap the request in an HttpEntity
        HttpEntity<CustomHoursRequestDto> requestEntity = new HttpEntity<>(request);

        // act
        ResponseEntity<CustomHoursResponseDto> response = client.exchange("/customHours/" + NAME, HttpMethod.PUT, requestEntity, CustomHoursResponseDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        CustomHoursResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(NAME, body.getName());
        assertEquals(DESCRIPTION, body.getDescription());
        assertEquals(DATE, body.getDate());
        assertEquals(newOpenTime, body.getOpenTime());
        assertEquals(CLOSE_TIME, body.getCloseTime());
        assertEquals(YEAR, body.getSchedule().getYear());
    }

    @Test
    @Order(17)
    public void testUpdateCustomHoursClosingTime() {
        // set up
        LocalTime newCloseTime = LocalTime.of(16, 0);
        CustomHoursRequestDto request = new CustomHoursRequestDto(NAME, DESCRIPTION, DATE, OPEN_TIME, newCloseTime, YEAR);

        // wrap the request in an HttpEntity
        HttpEntity<CustomHoursRequestDto> requestEntity = new HttpEntity<>(request);

        // act
        ResponseEntity<CustomHoursResponseDto> response = client.exchange("/customHours/" + NAME, HttpMethod.PUT, requestEntity, CustomHoursResponseDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        CustomHoursResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(NAME, body.getName());
        assertEquals(DESCRIPTION, body.getDescription());
        assertEquals(DATE, body.getDate());
        assertEquals(OPEN_TIME, body.getOpenTime());
        assertEquals(newCloseTime, body.getCloseTime());
        assertEquals(YEAR, body.getSchedule().getYear());
    }

    @Test
    @Order(18)
    public void testUpdateCustomHoursInvalidTime() {
        // set up
        CustomHoursRequestDto request = new CustomHoursRequestDto(NAME, DESCRIPTION, DATE, CLOSE_TIME, OPEN_TIME, YEAR);

        // wrap the request in an HttpEntity
        HttpEntity<CustomHoursRequestDto> requestEntity = new HttpEntity<>(request);

        // act
        ResponseEntity<ErrorDto> response = client.exchange("/customHours/" + NAME, HttpMethod.PUT, requestEntity, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Close time cannot be before open time.", body.getErrors().get(0));
    }

    @Test
    @Order(19)
    public void testUpdateCustomHoursYear() {
        // set up
        int newYear = 2024;
        LocalDate newDate = LocalDate.of(newYear, 1, 1);
        CustomHoursRequestDto request = new CustomHoursRequestDto(NAME, DESCRIPTION, newDate, OPEN_TIME, CLOSE_TIME, newYear);

        // create a new year schedule
        ScheduleRequestDto requestSchedule = new ScheduleRequestDto(new Schedule(newYear));
        client.postForEntity("/schedule", requestSchedule, ScheduleResponseDto.class);

        // wrap the request in an HttpEntity
        HttpEntity<CustomHoursRequestDto> requestEntity = new HttpEntity<>(request);

        // act
        ResponseEntity<CustomHoursResponseDto> response = client.exchange("/customHours/" + NAME, HttpMethod.PUT, requestEntity, CustomHoursResponseDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        CustomHoursResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(NAME, body.getName());
        assertEquals(DESCRIPTION, body.getDescription());
        assertEquals(newDate, body.getDate());
        assertEquals(OPEN_TIME, body.getOpenTime());
        assertEquals(CLOSE_TIME, body.getCloseTime());
        assertEquals(newYear, body.getSchedule().getYear());
    }

    @Test
    @Order(20)
    public void testUpdateCustomHoursNullDate() {
        // tests that a custom hours cannot be updated with a null date

        // set up
        CustomHoursRequestDto request = new CustomHoursRequestDto(NAME, DESCRIPTION, null, OPEN_TIME, CLOSE_TIME, YEAR);

        // wrap the request in an HttpEntity
        HttpEntity<CustomHoursRequestDto> requestEntity = new HttpEntity<>(request);

        // act
        ResponseEntity<ErrorDto> response = client.exchange("/customHours/" + NAME, HttpMethod.PUT, requestEntity, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Date or Time cannot be empty.", body.getErrors().get(0));
    }

    @Test
    @Order(21)
    public void testUpdateCustomHoursNullOpenTime() {
        // tests that a custom hours cannot be updated with a null date

        // set up
        CustomHoursRequestDto request = new CustomHoursRequestDto(NAME, DESCRIPTION, DATE, null, CLOSE_TIME, YEAR);

        // wrap the request in an HttpEntity
        HttpEntity<CustomHoursRequestDto> requestEntity = new HttpEntity<>(request);

        // act
        ResponseEntity<ErrorDto> response = client.exchange("/customHours/" + NAME, HttpMethod.PUT, requestEntity, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Date or Time cannot be empty.", body.getErrors().get(0));
    }

    @Test
    @Order(22)
    public void testUpdateCustomHoursNullCloseTime() {
        // tests that a custom hours cannot be updated with a null date

        // set up
        CustomHoursRequestDto request = new CustomHoursRequestDto(NAME, DESCRIPTION, DATE, OPEN_TIME, null, YEAR);

        // wrap the request in an HttpEntity
        HttpEntity<CustomHoursRequestDto> requestEntity = new HttpEntity<>(request);

        // act
        ResponseEntity<ErrorDto> response = client.exchange("/customHours/" + NAME, HttpMethod.PUT, requestEntity, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Date or Time cannot be empty.", body.getErrors().get(0));
    }

    @Test
    @Order(23)
    public void testGetAllCustomHours() {
        // act
        ResponseEntity<CustomHoursListDto> response = client.getForEntity("/customHours", CustomHoursListDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        CustomHoursListDto body = response.getBody();
        assertNotNull(body);

        assertEquals(1, body.getCustomHours().size());

        CustomHoursResponseDto customHours = body.getCustomHours().get(0);
        assertEquals(NAME, customHours.getName());  // only need to check name since we have already tested the other fields
    }

    @SuppressWarnings("null")
    @Test
    @Order(24)
    public void testDeleteCustomHours() {
        // act
        ResponseEntity<Void> response = client.exchange("/customHours/" + NAME, HttpMethod.DELETE, null, Void.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // assert that the custom hours was deleted
        ResponseEntity<ErrorDto> getResponse = client.getForEntity("/customHours/" + NAME, ErrorDto.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
        assertEquals("Custom hours with name " + NAME + " does not exist.", getResponse.getBody().getErrors().get(0));
    }

    @Test
    @Order(25)
    public void testDeleteCustomHoursNameDoesNotExist() {
        // tests that a custom hours cannot be deleted if the name does not exist

        // act
        ResponseEntity<ErrorDto> response = client.exchange("/customHours/{name}", HttpMethod.DELETE, null, ErrorDto.class, NAME);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Custom hours with name " + NAME + " not found.", body.getErrors().get(0));
    }

    @Test
    @Order(26)
    public void testDeleteAllCustomHours() {
        // test that all custom hours are deleted

        // set up, create another custom hours
        CustomHoursRequestDto request = new CustomHoursRequestDto(NAME + "1", DESCRIPTION, DATE, OPEN_TIME, CLOSE_TIME, YEAR);
        client.postForEntity("/customHours", request, CustomHoursResponseDto.class);

        // act
        ResponseEntity<Void> response = client.exchange("/customHours", HttpMethod.DELETE, null, Void.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // assert that there are no custom hours
        ResponseEntity<CustomHoursListDto> getResponse = client.getForEntity("/customHours", CustomHoursListDto.class);

        assertNotNull(getResponse);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());

        CustomHoursListDto body = getResponse.getBody();
        assertNotNull(body);
        assertEquals(0, body.getCustomHours().size());
    }
}
