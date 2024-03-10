package ca.mcgill.ecse321.scs.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.time.LocalDate;

import java.util.List;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.scs.exception.SCSException;
import ca.mcgill.ecse321.scs.model.Schedule;
import ca.mcgill.ecse321.scs.dao.ScheduleRepository;

@SuppressWarnings("null")
@SpringBootTest
public class ScheduleServiceTests {
    @Mock
    private ScheduleRepository scheduleRepository;
    @InjectMocks
    private ScheduleService scheduleService;

    @Test
    public void testCreateValidSchedule() {
        // set up
        int year = 2003;
        when(scheduleRepository.save(any(Schedule.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        Schedule schedule = scheduleService.createSchedule(year);

        // assert
        assertNotNull(schedule);
        assertEquals(year, schedule.getYear());
        verify(scheduleRepository, times(1)).save(any(Schedule.class));
    }

    @Test
    public void testCreateInvalidSchedule() {
        // test for negative year

        // set up
        int year = -2003;
        when(scheduleRepository.save(any(Schedule.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            scheduleService.createSchedule(year);
        });

        // assert
        assertEquals("Year cannot be negative.", exception.getMessage());
    }

    @Test
    public void testCreateScheduleExists() {
        // schedule already exists

        // set up
        int year = 2003;
        Schedule existingSchedule = new Schedule(year);
        when(scheduleRepository.findScheduleByYear(year)).thenReturn(existingSchedule);

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            scheduleService.createSchedule(year);
        });

        // assert
        assertEquals("Schedule for year " + year + " already exists.", exception.getMessage());
    }

    @Test
    public void testGetSchedule() {
        // set up
        int year = 2003;
        Schedule schedule = new Schedule(year);
        when(scheduleRepository.findScheduleByYear(year)).thenReturn(schedule);

        // act
        Schedule foundSchedule = scheduleService.getSchedule(year);

        // assert
        assertNotNull(foundSchedule);
        assertEquals(year, foundSchedule.getYear());
        verify(scheduleRepository, times(1)).findScheduleByYear(year);
    }

    @Test
    public void testGetScheduleNull() {
        // set up
        int year = 2003;

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            scheduleService.getSchedule(year);
        });

        // assert
        assertEquals("Schedule for year " + year + " not found.", exception.getMessage());
    }

    @Test
    public void testGetAllSchedules() {
        // set up
        when(scheduleRepository.findAll()).thenReturn(Collections.emptyList());

        // act
        List<Schedule> schedules = scheduleService.getAllSchedules();

        // assert
        assertNotNull(schedules);
        verify(scheduleRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteSchedule() {
        // set up
        int year = 2003;
        Schedule schedule = new Schedule(year);
        when(scheduleRepository.findScheduleByYear(year)).thenReturn(schedule).thenReturn(null); // adjusted to return null on subsequent calls

        // act
        scheduleService.deleteSchedule(year);

        // assert deletion
        verify(scheduleRepository, times(1)).delete(schedule);

        // act & assert exception
        Exception exception = assertThrows(SCSException.class, () -> {
            scheduleService.getSchedule(year);
        });

        // assert
        assertEquals("Schedule for year " + year + " not found.", exception.getMessage());
    }

    @Test
    public void testDeleteScheduleNull() {
        // set up
        int nonExistentYear = 2003;
        when(scheduleRepository.findScheduleByYear(nonExistentYear)).thenReturn(null);

        // act & assert
        SCSException exception = assertThrows(SCSException.class, () -> {
            scheduleService.deleteSchedule(nonExistentYear);
        });

        // assert
        assertEquals("Schedule for year " + nonExistentYear + " not found.", exception.getMessage());
        verify(scheduleRepository, times(0)).delete(any(Schedule.class));
    }

    @Test
    public void testDeleteAllSchedules() {
        // set up
        when(scheduleRepository.findAll()).thenReturn(Collections.emptyList());
        
        // act
        scheduleService.deleteAllSchedules();
        
        // assert
        verify(scheduleRepository, times(1)).deleteAll(); // verifies deleteAll was called
        assertTrue(scheduleService.getAllSchedules().isEmpty());
    }
}
