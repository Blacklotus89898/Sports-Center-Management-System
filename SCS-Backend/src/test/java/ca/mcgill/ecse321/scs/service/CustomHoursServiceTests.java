package ca.mcgill.ecse321.scs.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Collections;

import java.time.LocalDate;
import java.time.LocalTime;

import java.sql.Date;
import java.sql.Time;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.scs.exception.SCSException;
import ca.mcgill.ecse321.scs.model.Schedule;
import ca.mcgill.ecse321.scs.dao.ScheduleRepository;
import ca.mcgill.ecse321.scs.model.CustomHours;
import ca.mcgill.ecse321.scs.dao.CustomHoursRepository;

@SuppressWarnings("null")
@SpringBootTest
public class CustomHoursServiceTests {
    @Mock
    private CustomHoursRepository customHoursRepository;
    @InjectMocks
    private CustomHoursService customHoursService;

    @Mock
    private ScheduleRepository scheduleRepository;
    @InjectMocks
    private ScheduleService scheduleService;

    private Schedule schedule;
    private Schedule schedule2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);                     // initializes mocks and injects them

        // manually inject the scheduleService into customHoursService if required
        customHoursService.setScheduleService(scheduleService);

        // mock the behavior of scheduleService.getSchedule to return a schedule for the year 2023
        int year2023 = 2023;
        Schedule schedule = new Schedule(year2023);
        this.schedule = schedule;

        int year2022 = 2022;
        Schedule schedule2 = new Schedule(year2022);
        this.schedule2 = schedule2;

        when(scheduleRepository.findScheduleByYear(year2023)).thenReturn(schedule);
        when(scheduleRepository.findScheduleByYear(year2022)).thenReturn(schedule2);
    }

    @Test
    public void testCreateValidCustomHours() {
        // set up
        String name = "cny";    // chinese new year
        String description = "chinese new year";
        LocalDate date = LocalDate.of(2023, 1, 28);
        LocalTime openTime = LocalTime.of(0, 0);
        LocalTime closeTime = LocalTime.of(23, 59);
        int year = 2023;

        when(customHoursRepository.save(any(CustomHours.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        CustomHours customHours = customHoursService.createCustomHours(name, description, date, openTime, closeTime, year);

        // assert
        assertNotNull(customHours);
        assertEquals(name, customHours.getName());
        assertEquals(description, customHours.getDescription());
        assertEquals(date, customHours.getDate().toLocalDate());
        assertEquals(openTime, customHours.getOpenTime().toLocalTime());
        assertEquals(closeTime, customHours.getCloseTime().toLocalTime());
        assertEquals(year, customHours.getSchedule().getYear());
        verify(customHoursRepository, times(1)).save(any(CustomHours.class));
    }

    @Test
    public void testCreateInvalidCustomHoursNullName() {
        // test for null name

        // set up
        String name = null;
        String description = "chinese new year";
        LocalDate date = LocalDate.of(2023, 1, 28);
        LocalTime openTime = LocalTime.of(0, 0);
        LocalTime closeTime = LocalTime.of(23, 59);
        int year = 2023;

        when(customHoursRepository.save(any(CustomHours.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            customHoursService.createCustomHours(name, description, date, openTime, closeTime, year);
        });

        // assert
        assertEquals("Name cannot be empty.", exception.getMessage());
    }

    @Test
    public void testCreateInvalidCustomHoursEmptyDescription() {
        // description is empty

        // set up
        String name = "cny";
        String description = "";
        LocalDate date = LocalDate.of(2023, 1, 28);
        LocalTime openTime = LocalTime.of(0, 0);
        LocalTime closeTime = LocalTime.of(23, 59);
        int year = 2023;

        when(customHoursRepository.save(any(CustomHours.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            customHoursService.createCustomHours(name, description, date, openTime, closeTime, year);
        });

        // assert
        assertEquals("Description cannot be empty.", exception.getMessage());
    }

    @Test
    public void testCreateInvalidCustomHoursExists() {
        // custom hours already exists

        // set up
        String name = "cny";
        String description = "chinese new year";
        LocalDate date = LocalDate.of(2023, 1, 28);
        LocalTime openTime = LocalTime.of(0, 0);
        LocalTime closeTime = LocalTime.of(23, 59);
        int year = 2023;

        CustomHours existingCustomHours = new CustomHours(name, description, Date.valueOf(date), Time.valueOf(openTime), Time.valueOf(closeTime), this.schedule);
        when(customHoursRepository.findCustomHoursByName(name)).thenReturn(existingCustomHours);

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            customHoursService.createCustomHours(name, description, date, openTime, closeTime, year);
        });

        // assert
        assertEquals("Custom hours with name " + name + " already exists.", exception.getMessage());
    }

    @Test
    public void testCreateInvalidCustomHoursInvalidTime() {
        // invalid time

        // set up
        String name = "cny";
        String description = "chinese new year";
        LocalDate date = LocalDate.of(2023, 1, 28);
        LocalTime openTime = LocalTime.of(15, 0);
        LocalTime closeTime = LocalTime.of(3, 59);
        int year = 2023;

        when(customHoursRepository.save(any(CustomHours.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            customHoursService.createCustomHours(name, description, date, openTime, closeTime, year);
        });

        // assert
        assertEquals("Close time cannot be before open time.", exception.getMessage());
    }

    @Test
    public void testCreateInvalidCustomHoursInvalidYear() {
        // invalid year

        // set up
        String name = "cny";
        String description = "chinese new year";
        LocalDate date = LocalDate.of(2023, 1, 28);
        LocalTime openTime = LocalTime.of(15, 0);
        LocalTime closeTime = LocalTime.of(23, 59);
        int year = 2022;

        when(customHoursRepository.save(any(CustomHours.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            customHoursService.createCustomHours(name, description, date, openTime, closeTime, year);
        });

        // assert
        assertEquals("Year of date does not match year of schedule.", exception.getMessage());
    }

    @Test
    public void testGetCustomHours() {
        // set up
        String name = "cny";
        String description = "chinese new year";
        LocalDate date = LocalDate.of(2023, 1, 28);
        LocalTime openTime = LocalTime.of(0, 0);
        LocalTime closeTime = LocalTime.of(23, 59);
        int year = 2023;

        CustomHours customHours = new CustomHours(name, description, Date.valueOf(date), Time.valueOf(openTime), Time.valueOf(closeTime), this.schedule);
        when(customHoursRepository.findCustomHoursByName(name)).thenReturn(customHours);

        // act
        CustomHours returnedCustomHours = customHoursService.getCustomHours(name);

        // assert
        assertNotNull(returnedCustomHours);
        assertEquals(name, returnedCustomHours.getName());
        assertEquals(description, returnedCustomHours.getDescription());
        assertEquals(date, returnedCustomHours.getDate().toLocalDate());
        assertEquals(openTime, returnedCustomHours.getOpenTime().toLocalTime());
        assertEquals(closeTime, returnedCustomHours.getCloseTime().toLocalTime());
        assertEquals(year, returnedCustomHours.getSchedule().getYear());
    }

    @Test
    public void testGetCustomHoursNull() {
        // set up
        String name = "cny";

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            customHoursService.getCustomHours(name);
        });

        // assert
        assertEquals("Custom hours with name " + name + " does not exist.", exception.getMessage());
    }

    @Test
    public void testGetCustomHoursByDate() {
        // set up
        String name = "cny";
        String description = "chinese new year";
        LocalDate date = LocalDate.of(2023, 1, 28);
        LocalTime openTime = LocalTime.of(0, 0);
        LocalTime closeTime = LocalTime.of(23, 59);
        int year = 2023;

        CustomHours customHours = new CustomHours(name, description, Date.valueOf(date), Time.valueOf(openTime), Time.valueOf(closeTime), this.schedule);
        when(customHoursRepository.findAll()).thenReturn(Collections.singletonList(customHours));

        // act
        CustomHours returnedCustomHours = customHoursService.getCustomHoursByDate(date);

        // assert
        assertNotNull(returnedCustomHours);
        assertEquals(name, returnedCustomHours.getName());
        assertEquals(description, returnedCustomHours.getDescription());
        assertEquals(date, returnedCustomHours.getDate().toLocalDate());
        assertEquals(openTime, returnedCustomHours.getOpenTime().toLocalTime());
        assertEquals(closeTime, returnedCustomHours.getCloseTime().toLocalTime());
        assertEquals(year, returnedCustomHours.getSchedule().getYear());
    }

    @Test
    public void testGetCustomHoursByDateNull() {
        // set up
        LocalDate date = LocalDate.of(2023, 1, 28);
        when(customHoursRepository.findAll()).thenReturn(Collections.emptyList());

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            customHoursService.getCustomHoursByDate(date);
        });

        // assert
        assertEquals("Custom hours for date " + date + " does not exist.", exception.getMessage());
    }

    @Test
    public void testUpdateCustomHoursDescription() {
        // set up
        String name = "cny";
        String description = "chinese new year";
        LocalDate date = LocalDate.of(2023, 1, 28);
        LocalTime openTime = LocalTime.of(0, 0);
        LocalTime closeTime = LocalTime.of(23, 59);
        int year = 2023;

        CustomHours customHours = new CustomHours(name, description, Date.valueOf(date), Time.valueOf(openTime), Time.valueOf(closeTime), this.schedule);
        when(customHoursRepository.findCustomHoursByName(name)).thenReturn(customHours);
        when(customHoursRepository.save(any(CustomHours.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        description = "chinese new year celebration";
        CustomHours updatedCustomHours = customHoursService.updateCustomHours(name, description, date, openTime, closeTime, year);

        // assert
        assertNotNull(updatedCustomHours);
        assertEquals(name, updatedCustomHours.getName());
        assertEquals(description, updatedCustomHours.getDescription());
        assertEquals(date, updatedCustomHours.getDate().toLocalDate());
        assertEquals(openTime, updatedCustomHours.getOpenTime().toLocalTime());
        assertEquals(closeTime, updatedCustomHours.getCloseTime().toLocalTime());
        assertEquals(year, updatedCustomHours.getSchedule().getYear());
        verify(customHoursRepository, times(1)).save(any(CustomHours.class));
    }

    @Test
    public void testUpdateCustomHoursDate() {
        // set up
        String name = "cny";
        String description = "chinese new year";
        LocalDate date = LocalDate.of(2023, 1, 28);
        LocalTime openTime = LocalTime.of(0, 0);
        LocalTime closeTime = LocalTime.of(23, 59);
        int year = 2023;

        CustomHours customHours = new CustomHours(name, description, Date.valueOf(date), Time.valueOf(openTime), Time.valueOf(closeTime), this.schedule);
        when(customHoursRepository.findCustomHoursByName(name)).thenReturn(customHours);
        when(customHoursRepository.save(any(CustomHours.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        date = LocalDate.of(2023, 1, 29);
        CustomHours updatedCustomHours = customHoursService.updateCustomHours(name, description, date, openTime, closeTime, year);

        // assert
        assertNotNull(updatedCustomHours);
        assertEquals(name, updatedCustomHours.getName());
        assertEquals(description, updatedCustomHours.getDescription());
        assertEquals(date, updatedCustomHours.getDate().toLocalDate());
        assertEquals(openTime, updatedCustomHours.getOpenTime().toLocalTime());
        assertEquals(closeTime, updatedCustomHours.getCloseTime().toLocalTime());
        assertEquals(year, updatedCustomHours.getSchedule().getYear());
        verify(customHoursRepository, times(1)).save(any(CustomHours.class));
    }

    @Test
    public void testUpdateCustomHoursOpenTime() {
        // set up
        String name = "cny";
        String description = "chinese new year";
        LocalDate date = LocalDate.of(2023, 1, 28);
        LocalTime openTime = LocalTime.of(0, 0);
        LocalTime closeTime = LocalTime.of(23, 59);
        int year = 2023;

        CustomHours customHours = new CustomHours(name, description, Date.valueOf(date), Time.valueOf(openTime), Time.valueOf(closeTime), this.schedule);
        when(customHoursRepository.findCustomHoursByName(name)).thenReturn(customHours);
        when(customHoursRepository.save(any(CustomHours.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        openTime = LocalTime.of(1, 0);
        CustomHours updatedCustomHours = customHoursService.updateCustomHours(name, description, date, openTime, closeTime, year);

        // assert
        assertNotNull(updatedCustomHours);
        assertEquals(name, updatedCustomHours.getName());
        assertEquals(description, updatedCustomHours.getDescription());
        assertEquals(date, updatedCustomHours.getDate().toLocalDate());
        assertEquals(openTime, updatedCustomHours.getOpenTime().toLocalTime());
        assertEquals(closeTime, updatedCustomHours.getCloseTime().toLocalTime());
        assertEquals(year, updatedCustomHours.getSchedule().getYear());
        verify(customHoursRepository, times(1)).save(any(CustomHours.class));
    }

    @Test
    public void testUpdateCustomHoursCloseTime() {
        // set up
        String name = "cny";
        String description = "chinese new year";
        LocalDate date = LocalDate.of(2023, 1, 28);
        LocalTime openTime = LocalTime.of(0, 0);
        LocalTime closeTime = LocalTime.of(23, 59);
        int year = 2023;

        CustomHours customHours = new CustomHours(name, description, Date.valueOf(date), Time.valueOf(openTime), Time.valueOf(closeTime), this.schedule);
        when(customHoursRepository.findCustomHoursByName(name)).thenReturn(customHours);
        when(customHoursRepository.save(any(CustomHours.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        closeTime = LocalTime.of(23, 58);
        CustomHours updatedCustomHours = customHoursService.updateCustomHours(name, description, date, openTime, closeTime, year);

        // assert
        assertNotNull(updatedCustomHours);
        assertEquals(name, updatedCustomHours.getName());
        assertEquals(description, updatedCustomHours.getDescription());
        assertEquals(date, updatedCustomHours.getDate().toLocalDate());
        assertEquals(openTime, updatedCustomHours.getOpenTime().toLocalTime());
        assertEquals(closeTime, updatedCustomHours.getCloseTime().toLocalTime());
        assertEquals(year, updatedCustomHours.getSchedule().getYear());
        verify(customHoursRepository, times(1)).save(any(CustomHours.class));
    }

    @Test
    public void testUpdateCustomHoursYear() {
        // set up
        String name = "cny";
        String description = "chinese new year";
        LocalDate date = LocalDate.of(2023, 1, 28);
        LocalTime openTime = LocalTime.of(0, 0);
        LocalTime closeTime = LocalTime.of(23, 59);
        int year = 2023;

        CustomHours customHours = new CustomHours(name, description, Date.valueOf(date), Time.valueOf(openTime), Time.valueOf(closeTime), this.schedule);
        when(customHoursRepository.findCustomHoursByName(name)).thenReturn(customHours);
        when(customHoursRepository.save(any(CustomHours.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        year = 2022;
        date = LocalDate.of(2022, 1, 28);
        CustomHours updatedCustomHours = customHoursService.updateCustomHours(name, description, date, openTime, closeTime, year);

        // assert
        assertNotNull(updatedCustomHours);
        assertEquals(name, updatedCustomHours.getName());
        assertEquals(description, updatedCustomHours.getDescription());
        assertEquals(date, updatedCustomHours.getDate().toLocalDate());
        assertEquals(openTime, updatedCustomHours.getOpenTime().toLocalTime());
        assertEquals(closeTime, updatedCustomHours.getCloseTime().toLocalTime());
        assertEquals(year, updatedCustomHours.getSchedule().getYear());
        verify(customHoursRepository, times(1)).save(any(CustomHours.class));
    }

    @Test
    public void testUpdateCustomHoursInvalidTime() {
        // set up
        String name = "cny";
        String description = "chinese new year";
        LocalDate date = LocalDate.of(2023, 1, 28);
        LocalTime openTime = LocalTime.of(0, 0);
        LocalTime closeTime = LocalTime.of(23, 59);
        int year = 2023;

        CustomHours customHours = new CustomHours(name, description, Date.valueOf(date), Time.valueOf(openTime), Time.valueOf(closeTime), this.schedule);
        when(customHoursRepository.findCustomHoursByName(name)).thenReturn(customHours);
        when(customHoursRepository.save(any(CustomHours.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        LocalTime newOpenTime = LocalTime.of(15, 0);
        LocalTime newCloseTime = LocalTime.of(3, 59);
        Exception exception = assertThrows(SCSException.class, () -> {
            customHoursService.updateCustomHours(name, description, date, newOpenTime, newCloseTime, year);
        });

        // assert
        assertEquals("Close time cannot be before open time.", exception.getMessage());
    }

    @Test
    public void testUpdateCustomHoursInvalidName() {
        // set up
        String name = "cny";
        String description = "chinese new year";
        LocalDate date = LocalDate.of(2023, 1, 28);
        LocalTime openTime = LocalTime.of(0, 0);
        LocalTime closeTime = LocalTime.of(23, 59);
        int year = 2023;

        CustomHours customHours = new CustomHours(name, description, Date.valueOf(date), Time.valueOf(openTime), Time.valueOf(closeTime), this.schedule);
        when(customHoursRepository.findCustomHoursByName(name)).thenReturn(customHours);
        when(customHoursRepository.save(any(CustomHours.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            customHoursService.updateCustomHours("", description, date, openTime, closeTime, year);
        });

        // assert
        assertEquals("Name cannot be empty.", exception.getMessage());
    }

    @Test
    public void testUpdateCustomHoursInvalidDescription() {
        // set up
        String name = "cny";
        String description = "chinese new year";
        LocalDate date = LocalDate.of(2023, 1, 28);
        LocalTime openTime = LocalTime.of(0, 0);
        LocalTime closeTime = LocalTime.of(23, 59);
        int year = 2023;

        CustomHours customHours = new CustomHours(name, description, Date.valueOf(date), Time.valueOf(openTime), Time.valueOf(closeTime), this.schedule);
        when(customHoursRepository.findCustomHoursByName(name)).thenReturn(customHours);
        when(customHoursRepository.save(any(CustomHours.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            customHoursService.updateCustomHours(name, null, date, openTime, closeTime, year);
        });

        // assert
        assertEquals("Description cannot be empty.", exception.getMessage());
    }

    @Test
    public void testUpdateCustomHoursInvalidYear() {
        // set up
        String name = "cny";
        String description = "chinese new year";
        LocalDate date = LocalDate.of(2023, 1, 28);
        LocalTime openTime = LocalTime.of(0, 0);
        LocalTime closeTime = LocalTime.of(23, 59);

        CustomHours customHours = new CustomHours(name, description, Date.valueOf(date), Time.valueOf(openTime), Time.valueOf(closeTime), this.schedule);
        when(customHoursRepository.findCustomHoursByName(name)).thenReturn(customHours);
        when(customHoursRepository.save(any(CustomHours.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            customHoursService.updateCustomHours(name, description, date, openTime, closeTime, 2022);
        });

        // assert
        assertEquals("Year of date does not match year of schedule.", exception.getMessage());
    }

    @Test
    public void testGetAllCustomHours() {
        // set up
        String name = "cny";
        String description = "chinese new year";
        LocalDate date = LocalDate.of(2023, 1, 28);
        LocalDate date2 = LocalDate.of(2023, 1, 29);
        LocalTime openTime = LocalTime.of(0, 0);
        LocalTime closeTime = LocalTime.of(23, 59);

        CustomHours customHours = new CustomHours(name, description, Date.valueOf(date), Time.valueOf(openTime), Time.valueOf(closeTime), this.schedule);
        CustomHours customHours2 = new CustomHours(name, description, Date.valueOf(date2), Time.valueOf(openTime), Time.valueOf(closeTime), this.schedule);
        when(customHoursRepository.findAll()).thenReturn(List.of(customHours, customHours2));

        // act
        List<CustomHours> customHoursList = customHoursService.getAllCustomHours();

        // assert
        assertNotNull(customHoursList);
        assertEquals(2, customHoursList.size());
        assertTrue(customHoursList.contains(customHours));
        assertTrue(customHoursList.contains(customHours2));
    }

    @Test
    public void testDeleteCustomHours() {
        // set up
        String name = "cny";
        String description = "chinese new year";
        LocalDate date = LocalDate.of(2023, 1, 28);
        LocalTime openTime = LocalTime.of(0, 0);
        LocalTime closeTime = LocalTime.of(23, 59);

        CustomHours customHours = new CustomHours(name, description, Date.valueOf(date), Time.valueOf(openTime), Time.valueOf(closeTime), this.schedule);
        when(customHoursRepository.findCustomHoursByName(name)).thenReturn(customHours).thenReturn(null);

        // act
        customHoursService.deleteCustomHours(name);
        verify(customHoursRepository, times(1)).delete(customHours);

        // act & assert exception
        Exception exception = assertThrows(SCSException.class, () -> {
            customHoursService.getCustomHours(name);
        });

        // assert
        assertEquals("Custom hours with name " + name + " does not exist.", exception.getMessage());
        verify(customHoursRepository, times(1)).delete(customHours);
    }

    @Test
    public void testDeleteCustomHoursNull() {
        // set up
        String nonExistentName = "cny";
        when(customHoursRepository.findCustomHoursByName(nonExistentName)).thenReturn(null);

        // act & assert
        SCSException exception = assertThrows(SCSException.class, () -> {
            customHoursService.deleteCustomHours(nonExistentName);
        });

        // assert
        assertEquals("Custom hours with name " + nonExistentName + " not found.", exception.getMessage());
        verify(customHoursRepository, times(0)).delete(any(CustomHours.class));
    }

    @Test
    public void testDeleteAllCustomHours() {
        // set up
        String name = "cny";
        String description = "chinese new year";
        LocalDate date = LocalDate.of(2023, 1, 28);
        LocalDate date2 = LocalDate.of(2023, 1, 29);
        LocalTime openTime = LocalTime.of(0, 0);
        LocalTime closeTime = LocalTime.of(23, 59);
        
        CustomHours customHours = new CustomHours(name, description, Date.valueOf(date), Time.valueOf(openTime), Time.valueOf(closeTime), this.schedule);
        CustomHours customHours2 = new CustomHours(name, description, Date.valueOf(date2), Time.valueOf(openTime), Time.valueOf(closeTime), this.schedule);

        // mock the initial state before deletion
        when(customHoursRepository.findAll()).thenReturn(List.of(customHours, customHours2));

        // act: Delete all custom hours
        customHoursService.deleteAllCustomHours();
        when(customHoursRepository.findAll()).thenReturn(Collections.emptyList()); // adjust the mock to reflect the post-deletion state

        List<CustomHours> customHoursList = customHoursService.getAllCustomHours();

        // assert
        assertNotNull(customHoursList);
        assertEquals(0, customHoursList.size());
        verify(customHoursRepository, times(1)).deleteAll();
    }
}
