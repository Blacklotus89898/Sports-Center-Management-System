package ca.mcgill.ecse321.scs.integration;

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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class OpeningHoursIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    private final int YEAR = 2022;

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
        OpeningHoursDto openingHoursDto = new OpeningHoursDto();
        openingHoursDto.setDayOfWeek(parseDayOfWeekFromString("Monday"));
        openingHoursDto.setOpenTime(LocalTime.of(9, 0));
        openingHoursDto.setCloseTime(LocalTime.of(17, 0));
        openingHoursDto.setYear(2022);

        ResponseEntity<OpeningHoursDto> response = restTemplate.postForEntity("/openingHours", openingHoursDto,
                OpeningHoursDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @Order(2)
    public void testGetOpeningHoursByDay() {
        String day = "Monday";
        ResponseEntity<OpeningHoursDto> response = restTemplate.getForEntity("/openingHours/" + day,
                OpeningHoursDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @Order(3)
    public void testUpdateOpeningHours() {
        OpeningHoursDto openingHoursDto = new OpeningHoursDto();
        openingHoursDto.setDayOfWeek(parseDayOfWeekFromString("Monday"));
        openingHoursDto.setOpenTime(LocalTime.of(10, 0));
        openingHoursDto.setCloseTime(LocalTime.of(18, 0));
        openingHoursDto.setYear(YEAR);

        HttpEntity<OpeningHoursDto> request = new HttpEntity<>(openingHoursDto);
        ResponseEntity<OpeningHoursDto> response = restTemplate.exchange("/openingHours/Monday", HttpMethod.PUT, request,
                OpeningHoursDto.class);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    @Order(4)
    public void testDeleteOpeningHours() {
        restTemplate.delete("/openingHours/Monday");

        ResponseEntity<OpeningHoursDto> response = restTemplate.getForEntity("/openingHours/Monday",
                OpeningHoursDto.class);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    @Order(5)
    public void testGetOpeningHoursByDayNotFound() {
        ResponseEntity<OpeningHoursDto> response = restTemplate.getForEntity("/openingHours/NonExistentDay",
                OpeningHoursDto.class);

        assertEquals(404, response.getStatusCode().value());
    }

    private DayOfWeek parseDayOfWeekFromString(String day) {
        return DayOfWeek.valueOf(day.toUpperCase());
    }
}