package ca.mcgill.ecse321.scs.service;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import ca.mcgill.ecse321.scs.dao.OpeningHoursRepository;
// import ca.mcgill.ecse321.scs.exception.SCSException;
// import ca.mcgill.ecse321.scs.model.OpeningHours;
// import ca.mcgill.ecse321.scs.model.OpeningHours.DayOfWeek;

// import java.time.LocalTime;
// import java.util.Arrays;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;


// @ExtendWith(MockitoExtension.class)
// public class OpeningHoursServiceTests {

//     @InjectMocks
//     private OpeningHoursService openingHoursService;

//     @Mock
//     private OpeningHoursRepository openingHoursRepository;

//     @Test
//     public void testCreateOpeningHours() {
//         OpeningHours openingHours = new OpeningHours();
//         when(openingHoursRepository.save(any())).thenReturn(openingHours);
//         assertEquals(openingHours, openingHoursService.createOpeningHours("test", LocalTime.now(), LocalTime.now().plusHours(1), 2022));
//     }

//     private DayOfWeek parseDayOfWeekFromString(String dayOfWeekString) {
//         return DayOfWeek.valueOf(dayOfWeekString.toUpperCase());
//     }

//     @Test
//     public void testGetOpeningHoursByDay() {
//         OpeningHours openingHours = new OpeningHours();
//         openingHours.setDayOfWeek(parseDayOfWeekFromString("MONDAY"));
//         when(openingHoursRepository.findAll()).thenReturn(Arrays.asList(openingHours));
//         assertEquals(openingHours, openingHoursService.getOpeningHoursByDay("Monday"));
//     }

//     @Test
//     public void testGetOpeningHoursByDayNotFound() {
//         when(openingHoursRepository.findAll()).thenReturn(Arrays.asList());
//         assertThrows(SCSException.class, () -> openingHoursService.getOpeningHoursByDay("Monday"));
//     }

//     @Test
//     public void testUpdateOpeningHours() {
//         OpeningHours openingHours = new OpeningHours();
//         openingHours.setDayOfWeek(parseDayOfWeekFromString("Monday"));
//         when(openingHoursRepository.findOpeningHoursByDayOfWeek(parseDayOfWeekFromString("monday"))).thenReturn(openingHours);
//         when(openingHoursRepository.save(any())).thenReturn(openingHours);
//         assertEquals(openingHours, openingHoursService.updateOpeningHours(LocalTime.now(), LocalTime.now().plusHours(1), 2022, "Monday"));
//     }

//     // @Test
//     // public void testUpdateOpeningHoursNotFound() {
//     //     when(openingHoursRepository.findOpeningHoursByDayOfWeek(parseDayOfWeekFromString("monday"))).thenReturn(null);
//     //     assertThrows(SCSException.class, () -> openingHoursService.updateOpeningHours(LocalTime.now(), LocalTime.now().plusHours(1), 2022, "Monday"));
//     // }
// }
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.scs.dao.OpeningHoursRepository;
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

    // ... existing testCreateOpeningHours method ...

    // @Test
    // public void testGetOpeningHoursByDay() {
    //     String day = "MONDAY";
    //     when(openingHoursRepository.findAll()).thenReturn(Arrays.asList(new OpeningHours(DayOfWeek.MONDAY, Time.valueOf(LocalTime.of(9, 0)), Time.valueOf(LocalTime.of(17, 0)), new Schedule())));
        
    //     OpeningHours result = openingHoursService.getOpeningHoursByDay(day);

    //     assertNotNull(result);
    //     assertEquals(day, result.getDayOfWeek().toString());
    // }
//     @Test
// public void testGetOpeningHoursByDay() {
//     String day = "MONDAY";
//     OpeningHours mondayHours = new OpeningHours(DayOfWeek.MONDAY, Time.valueOf(LocalTime.of(9, 0)), Time.valueOf(LocalTime.of(17, 0)), new Schedule());
//     when(openingHoursRepository.findOpeningHoursByDayOfWeek(any())).thenReturn(mondayHours);

//     OpeningHours result = openingHoursService.getOpeningHoursByDay(day);

//     assertNotNull(result);
//     assertEquals(day, result.getDayOfWeek().toString());
// }

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
// public void testCreateOpeningHours() {
//     // Arrange
//     DayOfWeek dayOfWeek = DayOfWeek.MONDAY;
//     Time openingTime = Time.valueOf(LocalTime.of(9, 0));
//     Time closingTime = Time.valueOf(LocalTime.of(17, 0));
//     OpeningHours openingHours = new OpeningHours(dayOfWeek, openingTime, closingTime, new Schedule(2000));


//     when(openingHoursRepository.save(any(OpeningHours.class))).thenReturn(openingHours);

//     // Act
//     OpeningHours result = openingHoursService.createOpeningHours(dayOfWeek.toString(), openingTime.toLocalTime(), closingTime.toLocalTime(), 2000);

//     // Assert
//     assertNotNull(result);
//     assertEquals(dayOfWeek, result.getDayOfWeek());
//     assertEquals(openingTime, result.getOpenTime());
//     assertEquals(closingTime, result.getCloseTime());

//     verify(openingHoursRepository, times(1)).save(any(OpeningHours.class));
// }

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
}