package ca.mcgill.ecse321.scs.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.scs.dao.OpeningHoursRepository;
import ca.mcgill.ecse321.scs.exception.SCSException;
import ca.mcgill.ecse321.scs.model.OpeningHours;
import ca.mcgill.ecse321.scs.model.Schedule;
import ca.mcgill.ecse321.scs.model.OpeningHours.DayOfWeek;

import java.time.LocalTime;
import java.sql.Time;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
    

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class OpeningHoursServiceTests {

    @InjectMocks
    private OpeningHoursService openingHoursService;

    @Mock
    private OpeningHoursRepository openingHoursRepository;

    @Mock
    private ScheduleService scheduleService;

@Test
public void testCreateOpeningHours() {
    // Arrange
    String day = "MONDAY";
    LocalTime openTime = LocalTime.of(9, 0);
    LocalTime closeTime = LocalTime.of(17, 0);
    int year = 2022;
    Schedule schedule = new Schedule();
    OpeningHours openingHours = new OpeningHours();

    when(openingHoursRepository.findOpeningHoursByDayOfWeek(DayOfWeek.valueOf(day))).thenReturn(null);
    when(scheduleService.getSchedule(year)).thenReturn(schedule);
    when(openingHoursRepository.save(any(OpeningHours.class))).thenReturn(openingHours);

    // Act
    OpeningHours result = openingHoursService.createOpeningHours(day, openTime, closeTime, year);

    // Assert
    assertNotNull(result);
    assertEquals(DayOfWeek.valueOf(day), result.getDayOfWeek());
    assertEquals(Time.valueOf(openTime), result.getOpenTime());
    assertEquals(Time.valueOf(closeTime), result.getCloseTime());
    assertEquals(schedule, result.getSchedule());

    verify(openingHoursRepository, times(1)).save(any(OpeningHours.class));
}

@Test
public void testGetOpeningHoursByDay() {
    String day = "MONDAY";
    DayOfWeek dayOfWeek = DayOfWeek.valueOf(day);
    OpeningHours mondayHours = new OpeningHours(dayOfWeek, Time.valueOf(LocalTime.of(9, 0)), Time.valueOf(LocalTime.of(17, 0)), new Schedule());
    when(openingHoursRepository.findAll()).thenReturn(Arrays.asList(mondayHours));

    OpeningHours result = openingHoursService.getOpeningHoursByDay(day);

    assertNotNull(result);
    assertEquals(day, result.getDayOfWeek().toString());
}

    @Test
    public void testUpdateOpeningHours() {
        String day = "MONDAY";
        LocalTime openTime = LocalTime.of(9, 0);
        LocalTime closeTime = LocalTime.of(17, 0);
        int year = 2022;

        when(openingHoursRepository.findOpeningHoursByDayOfWeek(any())).thenReturn(new OpeningHours());
        when(scheduleService.getSchedule(anyInt())).thenReturn(new Schedule());

        OpeningHours result = openingHoursService.updateOpeningHours(openTime, closeTime, year, day);

        assertNotNull(result);
        assertEquals(day, result.getDayOfWeek().toString());
        assertEquals(Time.valueOf(openTime), result.getOpenTime());
        assertEquals(Time.valueOf(closeTime), result.getCloseTime());

        verify(openingHoursRepository, times(1)).save(any(OpeningHours.class));
    }

    @Test
    public void testGetAllOpeningHours() {
        when(openingHoursRepository.findAll()).thenReturn(Arrays.asList(new OpeningHours()));

        List<OpeningHours> result = openingHoursService.getAllOpeningHours();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testDeleteOpeningHours() {
        String day = "MONDAY";
        when(openingHoursRepository.findOpeningHoursByDayOfWeek(any())).thenReturn(new OpeningHours());

        openingHoursService.deleteOpeningHours(day);

        verify(openingHoursRepository, times(1)).delete(any(OpeningHours.class));
    }

    @Test
    public void testDeleteAllOpeningHours() {
        openingHoursService.deleteAllOpeningHours();

        verify(openingHoursRepository, times(1)).deleteAll();
    }

    @Test
    public void testCreateOpeningHoursWithNullDay() {
        LocalTime openTime = LocalTime.of(10, 0);
        LocalTime closeTime = LocalTime.of(11, 0);
        int year = 2022;

        assertThrows(SCSException.class, () -> {
            openingHoursService.createOpeningHours(null, openTime, closeTime, year);
        }, "Day or Time cannot be empty.");
    }

    @Test
    public void testCreateOpeningHoursWithNullOpenTime() {
        String day = "MONDAY";
        LocalTime closeTime = LocalTime.of(11, 0);
        int year = 2022;

        assertThrows(SCSException.class, () -> {
            openingHoursService.createOpeningHours(day, null, closeTime, year);
        }, "Day or Time cannot be empty.");
    }

    @Test
    public void testCreateOpeningHoursWithNullCloseTime() {
        String day = "MONDAY";
        LocalTime openTime = LocalTime.of(10, 0);
        int year = 2022;

        assertThrows(SCSException.class, () -> {
            openingHoursService.createOpeningHours(day, openTime, null, year);
        }, "Day or Time cannot be empty.");
    }

    @Test
    public void testCreateOpeningHoursWithClosingTimeBeforeOpeningTime() {
        String day = "MONDAY";
        LocalTime openTime = LocalTime.of(10, 0);
        LocalTime closeTime = LocalTime.of(9, 0);
        int year = 2022;

        assertThrows(SCSException.class, () -> {
            openingHoursService.createOpeningHours(day, openTime, closeTime, year);
        }, "Close time cannot be before open time.");
    }

    @Test
    public void testCreateOpeningHoursWithExistingDay() {
        String day = "MONDAY";
        LocalTime openTime = LocalTime.of(10, 0);
        LocalTime closeTime = LocalTime.of(11, 0);
        int year = 2022;

        when(openingHoursRepository.findOpeningHoursByDayOfWeek(openingHoursService.parseDayOfWeekFromString(day))).thenReturn(new OpeningHours());

        assertThrows(SCSException.class, () -> {
            openingHoursService.createOpeningHours(day, openTime, closeTime, year);
        }, "Opening hours with day " + day + " already exists.");
    }

    @Test
    public void testCreateOpeningHoursWithNonexistentSchedule() {
        String day = "MONDAY";
        LocalTime openTime = LocalTime.of(10, 0);
        LocalTime closeTime = LocalTime.of(11, 0);
        int year = 2022;

        when(openingHoursRepository.findOpeningHoursByDayOfWeek(openingHoursService.parseDayOfWeekFromString(day))).thenReturn(null);
        when(scheduleService.getSchedule(year)).thenReturn(null);

        assertThrows(SCSException.class, () -> {
            openingHoursService.createOpeningHours(day, openTime, closeTime, year);
        }, "Schedule with year " + year + " not found.");
    }
}