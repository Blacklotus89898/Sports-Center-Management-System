package ca.mcgill.ecse321.scs.integration;

import ca.mcgill.ecse321.scs.dao.OpeningHoursRepository;
import ca.mcgill.ecse321.scs.dao.ScheduleRepository;
import ca.mcgill.ecse321.scs.dto.OpeningHoursDto;
import ca.mcgill.ecse321.scs.dto.ScheduleRequestDto;
import ca.mcgill.ecse321.scs.dto.ScheduleResponseDto;
import ca.mcgill.ecse321.scs.model.OpeningHours;
import ca.mcgill.ecse321.scs.model.OpeningHours.DayOfWeek;
import net.bytebuddy.dynamic.loading.PackageDefinitionStrategy.ManifestReading.SealBaseLocator;
import ca.mcgill.ecse321.scs.model.Schedule;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
        // restTemplate.exchange("/api/openingHours", HttpMethod.DELETE, null, Void.class);
    }

    @BeforeAll
    public void createSchedule() {
        // create a schedule, which is required for creating custom hours
        // no need to test the creation of a schedule since it is already tested in ScheduleIntegrationTests
        ScheduleRequestDto request = new ScheduleRequestDto(new Schedule(YEAR));
        restTemplate.postForEntity("/schedule", request, ScheduleResponseDto.class);
    }

    // @BeforeEach
    // public void clearOpeningHours() {
    //     restTemplate.exchange("/api/openinghours", HttpMethod.DELETE, null, Void.class);
    // }

    @Test
    @Order(1)
    public void testCreateOpeningHours() {
        OpeningHoursDto openingHoursDto = new OpeningHoursDto();
        openingHoursDto.setDayOfWeek(parseDayOfWeekFromString("Monday"));
        openingHoursDto.setOpenTime(LocalTime.of(9, 0));
        openingHoursDto.setCloseTime(LocalTime.of(17, 0));
        openingHoursDto.setYear(2022);

        ResponseEntity<OpeningHoursDto> response = restTemplate.postForEntity("/api/openingHours", openingHoursDto, OpeningHoursDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        // assertEquals(2022, response.getHeaders());
        // assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    // @Test
    // public void testGetAllOpeningHours() {
    //     ResponseEntity<OpeningHoursDto[]> response = restTemplate.getForEntity("/api/openingHours", OpeningHoursDto[].class);

    //     assertEquals(200, response.getStatusCode().value());
    //     assertNotNull(response.getBody());
    // }

    // @Test
    // public void testGetOpeningHoursByDay() {
    //     ResponseEntity<OpeningHoursDto> response = restTemplate.getForEntity("/api/openingHours/Monday", OpeningHoursDto.class);

    //     assertEquals(200, response.getStatusCode().value());
    //     assertNotNull(response.getBody());
    // }
    @Test
    @Order(2)
public void testGetOpeningHoursByDay() {
    String day = "Monday";
    ResponseEntity<OpeningHoursDto> response = restTemplate.getForEntity("/api/openingHours/Monday",  OpeningHoursDto.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
}


// public void createOpeningHours() {
//     // Create an OpeningHours object for Monday if it doesn't exist
//     OpeningHours mondayOpeningHours = OpeningHoursRepository.findOpeningHoursByDayOfWeek(parseDayOfWeekFromString("Monday"));
//     if (mondayOpeningHours == null) {
//         mondayOpeningHours = new OpeningHours();
//         mondayOpeningHours.setDayOfWeek(parseDayOfWeekFromString("Monday"));
//         OpeningHoursRepository.save(mondayOpeningHours);
//     }

//     // Create a Schedule object for the given year if it doesn't exist
//     Schedule schedule = scheduleService.getSchedule(YEAR);
//     if (schedule == null) {
//         schedule = new Schedule();
//         schedule.setYear(YEAR);
//         scheduleService.save(schedule);
//     }
    
// }
// @Autowired
// OpeningHoursRepository op;
// @Autowired
// ScheduleRepository sp;
    @Test
    @Order(3)
    public void testUpdateOpeningHours() {
    //     OpeningHours mondayOpeningHours = op.findOpeningHoursByDayOfWeek(parseDayOfWeekFromString("Monday"));
    // if (mondayOpeningHours == null) {
    //     mondayOpeningHours = new OpeningHours();
    //     mondayOpeningHours.setDayOfWeek(parseDayOfWeekFromString("Monday"));
    //     op.save(mondayOpeningHours);
    // }
    // Schedule schedule = sp.getSchedule(YEAR);
    // if (schedule == null) {
    //     schedule = new Schedule();
    //     schedule.setYear(YEAR);
    //     sp.save(schedule);
    // }
    

        OpeningHoursDto openingHoursDto = new OpeningHoursDto();
        openingHoursDto.setDayOfWeek(parseDayOfWeekFromString("Monday"));
        openingHoursDto.setOpenTime(LocalTime.of(10, 0));
        openingHoursDto.setCloseTime(LocalTime.of(18, 0));
        openingHoursDto.setYear(YEAR);

        HttpEntity<OpeningHoursDto> request = new HttpEntity<>(openingHoursDto);
        ResponseEntity<OpeningHoursDto> response = restTemplate.exchange("/api/openingHours", HttpMethod.PUT, request, OpeningHoursDto.class);
        
        
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    @Order(4)
    public void testDeleteOpeningHours() {
        restTemplate.delete("/api/openingHours/Monday");

        ResponseEntity<OpeningHoursDto> response = restTemplate.getForEntity("/api/openingHours/Monday", OpeningHoursDto.class);

        assertEquals(404, response.getStatusCode().value());
    }

    private DayOfWeek parseDayOfWeekFromString(String day) {
        // TODO Auto-generated method stub
        return DayOfWeek.valueOf(day.toUpperCase());
    }
}