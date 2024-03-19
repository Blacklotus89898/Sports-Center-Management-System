package ca.mcgill.ecse321.scs.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.scs.dao.OpeningHoursRepository;
import ca.mcgill.ecse321.scs.dao.ScheduleRepository;
import ca.mcgill.ecse321.scs.exception.SCSException;
import ca.mcgill.ecse321.scs.model.CustomHours;
import ca.mcgill.ecse321.scs.model.OpeningHours;
import ca.mcgill.ecse321.scs.model.Schedule;
import ca.mcgill.ecse321.scs.model.OpeningHours.DayOfWeek;

import java.time.LocalTime;
import java.sql.Time;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
    
@SpringBootTest
public class OpeningHoursServiceTests {
    @Mock
    private OpeningHoursRepository openingHoursRepository;
    @InjectMocks
    private OpeningHoursService openingHoursService;

    @Mock
    private ScheduleRepository scheduleRepository;
    @InjectMocks
    private ScheduleService scheduleService;
    
    private final String DAY = "MONDAY";
    private final LocalTime OPEN_TIME = LocalTime.of(9, 0);
    private final LocalTime CLOSE_TIME = LocalTime.of(17, 0);
    private final int YEAR = 2022;

    Schedule schedule23;
    Schedule schedule22;
    OpeningHours openingHours;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        openingHoursService.setScheduleService(scheduleService);

        schedule22 = new Schedule(YEAR);
        when(scheduleRepository.findScheduleByYear(YEAR)).thenReturn(schedule22);

        int YEAR23 = YEAR + 1;
        schedule23 = new Schedule(YEAR23);
        when(scheduleRepository.findScheduleByYear(YEAR23)).thenReturn(schedule23);

        when(openingHoursRepository.save(any(OpeningHours.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        openingHours = new OpeningHours(DayOfWeek.valueOf(DAY), Time.valueOf(OPEN_TIME), Time.valueOf(CLOSE_TIME), schedule22);
    }

    @Test
    public void testCreateOpeningHours() {
        //set up
        when(openingHoursRepository.findOpeningHoursByDayOfWeek(DayOfWeek.valueOf(DAY), YEAR)).thenReturn(null);
        when(openingHoursRepository.save(any(OpeningHours.class))).thenReturn(openingHours);
        
        // act
        openingHoursService.createOpeningHours(DAY, OPEN_TIME, CLOSE_TIME, YEAR);

        // assert
        assertNotNull(openingHours);
        assertEquals(DAY, openingHours.getDayOfWeek().toString());
        assertEquals(Time.valueOf(OPEN_TIME), openingHours.getOpenTime());
        assertEquals(Time.valueOf(CLOSE_TIME), openingHours.getCloseTime());
        assertEquals(YEAR, openingHours.getSchedule().getYear());
    }

    @Test
    public void testCreateOpeningHoursNullDay() {
        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            openingHoursService.createOpeningHours(null, OPEN_TIME, CLOSE_TIME, YEAR);
        });

        // assert
        assertEquals("Day or Time cannot be empty.", exception.getMessage());
    }

    @Test
    public void testCreateOpeningHoursNullOpenTime() {
        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            openingHoursService.createOpeningHours(DAY, null, CLOSE_TIME, YEAR);
        });

        // assert
        assertEquals("Day or Time cannot be empty.", exception.getMessage());
    }

    @Test
    public void testCreateOpeningHoursNullCloseTime() {
        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            openingHoursService.createOpeningHours(DAY, OPEN_TIME, null, YEAR);
        });

        // assert
        assertEquals("Day or Time cannot be empty.", exception.getMessage());
    }

    @Test
    public void testCreateOpeningHoursCloseTimeBeforeOpenTime() {
        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            openingHoursService.createOpeningHours(DAY, CLOSE_TIME, OPEN_TIME, YEAR);
        });

        // assert
        assertEquals("Close time cannot be before open time.", exception.getMessage());
    }

    @Test
    public void testCreateOpeningHoursAlreadyExists() {
        // act
        OpeningHours openingHours = openingHoursService.createOpeningHours(DAY, OPEN_TIME, CLOSE_TIME, YEAR);
        when(openingHoursRepository.findOpeningHoursByDayOfWeek(DayOfWeek.valueOf(DAY), YEAR)).thenReturn(openingHours);

        Exception exception = assertThrows(SCSException.class, () -> {
            openingHoursService.createOpeningHours(DAY, OPEN_TIME, CLOSE_TIME, YEAR);
        });

        // assert
        assertEquals("Opening hours with day " + DAY + " already exists.", exception.getMessage());
    }

    @Test
    public void testCreateOpeningHoursNullSchedule() {
        // act
        when(scheduleRepository.findScheduleByYear(YEAR)).thenReturn(null);

        Exception exception = assertThrows(SCSException.class, () -> {
            openingHoursService.createOpeningHours(DAY, OPEN_TIME, CLOSE_TIME, YEAR);
        });

        // assert
        assertEquals("Schedule for year " + YEAR + " not found.", exception.getMessage());
    }

    @Test
    public void testGetOpeningHoursByDay() {
        // act
        when(openingHoursRepository.findOpeningHoursByDayOfWeek(DayOfWeek.valueOf(DAY), YEAR)).thenReturn(openingHours);

        // act
        OpeningHours foundOpeningHours = openingHoursService.getOpeningHoursByDay(DAY, YEAR);

        // assert
        assertNotNull(openingHours);
        assertEquals(DAY, foundOpeningHours.getDayOfWeek().toString());
        assertEquals(Time.valueOf(OPEN_TIME), foundOpeningHours.getOpenTime());
        assertEquals(Time.valueOf(CLOSE_TIME), foundOpeningHours.getCloseTime());
        assertEquals(YEAR, foundOpeningHours.getSchedule().getYear());
    }

    @Test
    public void testGetOpeningHoursByDayNotFound() {
        // set up
        when(openingHoursRepository.findOpeningHoursByDayOfWeek(DayOfWeek.valueOf(DAY), YEAR)).thenReturn(null);

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            openingHoursService.getOpeningHoursByDay(DAY, YEAR);
        });

        // assert
        assertEquals("Opening hours for day " + DAY + " does not exist for the year " + YEAR + ".", exception.getMessage());
    }

    @Test
    public void testUpdateOpeningHours() {
        // set up
        when(openingHoursRepository.findOpeningHoursByDayOfWeek(DayOfWeek.valueOf(DAY), YEAR)).thenReturn(openingHours);

        // act
        LocalTime newOpenTime = LocalTime.of(10, 0);
        LocalTime newCloseTime = LocalTime.of(16, 0);
        OpeningHours updatedOpeningHours = openingHoursService.updateOpeningHours(newOpenTime, newCloseTime, YEAR, DAY);

        // assert
        assertNotNull(updatedOpeningHours);
        assertEquals(DAY, updatedOpeningHours.getDayOfWeek().toString());
        assertEquals(Time.valueOf(newOpenTime), updatedOpeningHours.getOpenTime());
        assertEquals(Time.valueOf(newCloseTime), updatedOpeningHours.getCloseTime());
        assertEquals(YEAR, updatedOpeningHours.getSchedule().getYear());
    }

    @Test
    public void testUpdateOpeningHoursNullDay() {
        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            openingHoursService.updateOpeningHours(OPEN_TIME, CLOSE_TIME, YEAR, null);
        });

        // assert
        assertEquals("Date or Time cannot be empty.", exception.getMessage());
    }

    @Test
    public void testUpdateOpeningHoursNullOpenTime() {
        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            openingHoursService.updateOpeningHours(null, CLOSE_TIME, YEAR, DAY);
        });

        // assert
        assertEquals("Date or Time cannot be empty.", exception.getMessage());
    }

    @Test
    public void testUpdateOpeningHoursNullCloseTime() {
        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            openingHoursService.updateOpeningHours(OPEN_TIME, null, YEAR, DAY);
        });

        // assert
        assertEquals("Date or Time cannot be empty.", exception.getMessage());
    }

    @Test
    public void testUpdateOpeningHoursCloseTimeBeforeOpenTime() {
        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            openingHoursService.updateOpeningHours(CLOSE_TIME, OPEN_TIME, YEAR, DAY);
        });

        // assert
        assertEquals("Close time cannot be before open time.", exception.getMessage());
    }

    @Test
    public void testUpdateOpeningHoursNotFound() {
        // set up
        when(openingHoursRepository.findOpeningHoursByDayOfWeek(DayOfWeek.valueOf(DAY), YEAR)).thenReturn(null);

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            openingHoursService.updateOpeningHours(OPEN_TIME, CLOSE_TIME, YEAR, DAY);
        });

        // assert
        assertEquals("Opening hours with day " + DAY + " not found.", exception.getMessage());
    }

    @Test
    public void testGetAllOpeningHours() {
        // set up
        // create many opening hours
        OpeningHours openingHours1 = new OpeningHours(DayOfWeek.valueOf(DAY), Time.valueOf(OPEN_TIME), Time.valueOf(CLOSE_TIME), schedule22);
        OpeningHours openingHours2 = new OpeningHours(DayOfWeek.valueOf("TUESDAY"), Time.valueOf(LocalTime.of(8, 0)), Time.valueOf(LocalTime.of(16, 0)), schedule22);
        OpeningHours openingHours3 = new OpeningHours(DayOfWeek.valueOf("WEDNESDAY"), Time.valueOf(OPEN_TIME), Time.valueOf(CLOSE_TIME), schedule23);
        when(openingHoursRepository.findAll()).thenReturn(Arrays.asList(openingHours1, openingHours2, openingHours3));


        // act
        List<OpeningHours> openingHoursList = openingHoursService.getAllOpeningHours(YEAR);

        // assert
        assertNotNull(openingHoursList);
        assertEquals(2, openingHoursList.size());
        
        // assert day 1
        assertEquals(DAY, openingHoursList.get(0).getDayOfWeek().toString());
        assertEquals(Time.valueOf(OPEN_TIME), openingHoursList.get(0).getOpenTime());
        assertEquals(Time.valueOf(CLOSE_TIME), openingHoursList.get(0).getCloseTime());
        assertEquals(YEAR, openingHoursList.get(0).getSchedule().getYear());
        
        // assert day 2
        assertEquals("TUESDAY", openingHoursList.get(1).getDayOfWeek().toString());
        assertEquals(Time.valueOf(LocalTime.of(8, 0)), openingHoursList.get(1).getOpenTime());
        assertEquals(Time.valueOf(LocalTime.of(16, 0)), openingHoursList.get(1).getCloseTime());
        assertEquals(YEAR, openingHoursList.get(1).getSchedule().getYear());
    }

    @Test
    public void testDeleteOpeningHours() {
        // set up
        when(openingHoursRepository.findOpeningHoursByDayOfWeek(DayOfWeek.valueOf(DAY), YEAR)).thenReturn(openingHours);

        // act
        openingHoursService.deleteOpeningHours(DAY, YEAR);

        // assert
        verify(openingHoursRepository, times(1)).delete(openingHours);
    }

    @Test
    public void testDeleteOpeningHoursNotFound() {
        // set up
        when(openingHoursRepository.findOpeningHoursByDayOfWeek(DayOfWeek.valueOf(DAY), YEAR)).thenReturn(null);

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            openingHoursService.deleteOpeningHours(DAY, YEAR);
        });

        // assert
        assertEquals("Opening hours with day " + DAY + " not found for the year" + YEAR + ".", exception.getMessage());
    }

    @Test
    public void testDeleteAllOpeningHours() {
        // set up
        // create many opening hours
        OpeningHours openingHours1 = new OpeningHours(DayOfWeek.valueOf(DAY), Time.valueOf(OPEN_TIME), Time.valueOf(CLOSE_TIME), schedule22);
        OpeningHours openingHours2 = new OpeningHours(DayOfWeek.valueOf("TUESDAY"), Time.valueOf(LocalTime.of(8, 0)), Time.valueOf(LocalTime.of(16, 0)), schedule22);
        OpeningHours openingHours3 = new OpeningHours(DayOfWeek.valueOf("WEDNESDAY"), Time.valueOf(OPEN_TIME), Time.valueOf(CLOSE_TIME), schedule23);
        when(openingHoursRepository.findAll()).thenReturn(Arrays.asList(openingHours1, openingHours2, openingHours3));

        // act
        openingHoursService.deleteAllOpeningHours(YEAR);

        // assert
        verify(openingHoursRepository, times(1)).delete(openingHours1);
        verify(openingHoursRepository, times(1)).delete(openingHours2);
        verify(openingHoursRepository, times(0)).delete(openingHours3);
    }
}