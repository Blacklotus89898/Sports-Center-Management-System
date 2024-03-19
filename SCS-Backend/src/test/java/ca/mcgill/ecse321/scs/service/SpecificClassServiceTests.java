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
import ca.mcgill.ecse321.scs.model.ClassType;
import ca.mcgill.ecse321.scs.dao.ClassTypeRepository;
import ca.mcgill.ecse321.scs.model.SpecificClass;
import ca.mcgill.ecse321.scs.dao.SpecificClassRepository;

@SuppressWarnings("null")
@SpringBootTest
public class SpecificClassServiceTests {
    @Mock
    private SpecificClassRepository specificClassRepository;
    @Mock
    private ClassTypeRepository classTypeRepository;
    @Mock
    private ScheduleRepository scheduleRepository;
    
    @InjectMocks
    private ScheduleService scheduleService;
    @InjectMocks
    private ClassTypeService classTypeService;
    @InjectMocks
    private SpecificClassService specificClassService;

    private Schedule schedule;
    private ClassType classType;
    private SpecificClass specificClass;

    private static final int CLASS_ID = -1;
    private static final String CLASS_NAME = "Beginner Yoga";
    private static final String DESCRIPTION = "Yoga class for beginners";
    private static final LocalDate DATE = LocalDate.of(2022, 1, 1);
    private static final LocalTime START_TIME = LocalTime.of(10, 0);
    private static final int HOUR_DURATION = 1;
    private static final int MAX_CAPACITY = 10;
    private static final int CURRENT_CAPACITY = 0;
    private static final double REGISTRATION_FEE = 10.0;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        specificClassService.dependencyInjection(classTypeService, scheduleService);

        schedule = new Schedule();
        schedule.setYear(2022);
        when(scheduleRepository.findScheduleByYear(2022)).thenReturn(schedule);

        classType = new ClassType();
        classType.setClassName("Yoga");
        classType.setDescription("Yoga classes");
        classType.setIsApproved(true);
        when(classTypeRepository.findClassTypeByClassName("Yoga")).thenReturn(classType);

        when(specificClassRepository.save(any(SpecificClass.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    public void testCreateSpecificClass() {
        // act
        specificClass = specificClassService.createSpecificClass(classType.getClassName(), schedule.getYear(), CLASS_NAME, DESCRIPTION, DATE, START_TIME, HOUR_DURATION, MAX_CAPACITY, CURRENT_CAPACITY, REGISTRATION_FEE);

        // assert
        assertNotNull(specificClass);
        assertEquals(CLASS_NAME, specificClass.getSpecificClassName());
        assertEquals(DESCRIPTION, specificClass.getDescription());
        assertEquals(Date.valueOf(DATE), specificClass.getDate());
        assertEquals(Time.valueOf(START_TIME), specificClass.getStartTime());
        assertEquals(HOUR_DURATION, specificClass.getHourDuration());
        assertEquals(MAX_CAPACITY, specificClass.getMaxCapacity());
        assertEquals(CURRENT_CAPACITY, specificClass.getCurrentCapacity());
        assertEquals(REGISTRATION_FEE, specificClass.getRegistrationFee());
        assertEquals(classType, specificClass.getClassType());
        assertEquals(schedule, specificClass.getSchedule());
    }

    @Test
    public void testCreateSpecificClassClassTypeNotApproved() {
        // arrange
        classType.setIsApproved(false);

        // act
        Exception exception = assertThrows(SCSException.class, () -> specificClassService.createSpecificClass(classType.getClassName(), schedule.getYear(), CLASS_NAME, DESCRIPTION, DATE, START_TIME, HOUR_DURATION, MAX_CAPACITY, CURRENT_CAPACITY, REGISTRATION_FEE));

        // assert
        assertEquals("Class type " + classType.getClassName() + " is not approved.", exception.getMessage());
        classType.setIsApproved(true);
    }

    @Test
    public void testCreateSpecificClassNullClassName() {
        // act
        Exception exception = assertThrows(SCSException.class, () -> specificClassService.createSpecificClass(null, schedule.getYear(), CLASS_NAME, DESCRIPTION, DATE, START_TIME, HOUR_DURATION, MAX_CAPACITY, CURRENT_CAPACITY, REGISTRATION_FEE));

        // assert
        assertEquals("Class type cannot be empty.", exception.getMessage());
    }

    @Test
    public void testCreateSpecificClassNullDescription() {
        // act
        Exception exception = assertThrows(SCSException.class, () -> specificClassService.createSpecificClass(classType.getClassName(), schedule.getYear(), CLASS_NAME, null, DATE, START_TIME, HOUR_DURATION, MAX_CAPACITY, CURRENT_CAPACITY, REGISTRATION_FEE));

        // assert
        assertEquals("Description cannot be empty.", exception.getMessage());
    }

    @Test
    public void testCreateSpecificClassNullDate() {
        // act
        Exception exception = assertThrows(SCSException.class, () -> specificClassService.createSpecificClass(classType.getClassName(), schedule.getYear(), CLASS_NAME, DESCRIPTION, null, START_TIME, HOUR_DURATION, MAX_CAPACITY, CURRENT_CAPACITY, REGISTRATION_FEE));

        // assert
        assertEquals("Invalid date.", exception.getMessage());
    }

    @Test
    public void testCreateSpecificClassYearDoesNotMatchDate() {
        // act
        Exception exception = assertThrows(SCSException.class, () -> specificClassService.createSpecificClass(classType.getClassName(), schedule.getYear(), CLASS_NAME, DESCRIPTION, LocalDate.of(2021, 1, 1), START_TIME, HOUR_DURATION, MAX_CAPACITY, CURRENT_CAPACITY, REGISTRATION_FEE));

        // assert
        assertEquals("Schedule year does not match the date.", exception.getMessage());
    }

    @Test
    public void testCreateSpecificClassNullStartTime() {
        // act
        Exception exception = assertThrows(SCSException.class, () -> specificClassService.createSpecificClass(classType.getClassName(), schedule.getYear(), CLASS_NAME, DESCRIPTION, DATE, null, HOUR_DURATION, MAX_CAPACITY, CURRENT_CAPACITY, REGISTRATION_FEE));

        // assert
        assertEquals("Invalid start time.", exception.getMessage());
    }

    @Test
    public void testCreateSpecificClassNegativeHourDuration() {
        // act
        Exception exception = assertThrows(SCSException.class, () -> specificClassService.createSpecificClass(classType.getClassName(), schedule.getYear(), CLASS_NAME, DESCRIPTION, DATE, START_TIME, -1, MAX_CAPACITY, CURRENT_CAPACITY, REGISTRATION_FEE));

        // assert
        assertEquals("The duration cannot be negative.", exception.getMessage());
    }

    @Test
    public void testCreateSpecificClassNegativeMaxCapacity() {
        // act
        Exception exception = assertThrows(SCSException.class, () -> specificClassService.createSpecificClass(classType.getClassName(), schedule.getYear(), CLASS_NAME, DESCRIPTION, DATE, START_TIME, HOUR_DURATION, -1, CURRENT_CAPACITY, REGISTRATION_FEE));

        // assert
        assertEquals("Maximum capacity must be greater than 0.", exception.getMessage());
    }

    @Test
    public void testCreateSpecificClassNegativeCurrentCapacity() {
        // act
        Exception exception = assertThrows(SCSException.class, () -> specificClassService.createSpecificClass(classType.getClassName(), schedule.getYear(), CLASS_NAME, DESCRIPTION, DATE, START_TIME, HOUR_DURATION, MAX_CAPACITY, -1, REGISTRATION_FEE));

        // assert
        assertEquals("Current capacity cannot be smaller than 0.", exception.getMessage());
    }

    @Test
    public void testCreateSpecificClassCurrentCapacityGreaterThanMaxCapacity() {
        // act
        Exception exception = assertThrows(SCSException.class, () -> specificClassService.createSpecificClass(classType.getClassName(), schedule.getYear(), CLASS_NAME, DESCRIPTION, DATE, START_TIME, HOUR_DURATION, MAX_CAPACITY, MAX_CAPACITY + 1, REGISTRATION_FEE));

        // assert
        assertEquals("Current capacity must be less than or equal to the max capacity.", exception.getMessage());
    }

    @Test
    public void testCreateSpecificClassNegativeRegistrationFee() {
        // act
        Exception exception = assertThrows(SCSException.class, () -> specificClassService.createSpecificClass(classType.getClassName(), schedule.getYear(), CLASS_NAME, DESCRIPTION, DATE, START_TIME, HOUR_DURATION, MAX_CAPACITY, CURRENT_CAPACITY, -1));

        // assert
        assertEquals("Registration fee cannot be negative.", exception.getMessage());
    }

    @Test
    public void testGetSpecificClass() {
        // arrange
        specificClass = new SpecificClass();
        specificClass.setClassId(CLASS_ID);
        when(specificClassRepository.findSpecificClassByClassId(CLASS_ID)).thenReturn(specificClass);

        // act
        SpecificClass result = specificClassService.getSpecificClass(CLASS_ID);

        // assert
        assertNotNull(result);
        assertEquals(specificClass, result);
    }

    @Test
    public void testGetSpecificClassNotFound() {
        // act
        Exception exception = assertThrows(SCSException.class, () -> specificClassService.getSpecificClass(CLASS_ID));

        // assert
        assertEquals("Specific class with id " + CLASS_ID + " not found.", exception.getMessage());
    }

    @Test
    public void testUpdateSpecificClassScheduleYearNotMatchDate() {
        // arrange
        schedule.setYear(2021);
        when(specificClassRepository.findSpecificClassByClassId(CLASS_ID)).thenReturn(specificClass);

        // act
        Exception exception = assertThrows(SCSException.class, () -> specificClassService.updateSpecificClass(CLASS_ID, classType.getClassName(), schedule.getYear(), CLASS_NAME, DESCRIPTION, DATE, START_TIME, HOUR_DURATION, MAX_CAPACITY, CURRENT_CAPACITY, REGISTRATION_FEE));

        // assert
        assertEquals("Schedule year does not match the date.", exception.getMessage());
        schedule.setYear(2022);
    }

    @Test
    public void testUpdateSpecificClass() {
        // arrange
        specificClass = new SpecificClass();
        specificClass.setClassId(CLASS_ID);
        when(specificClassRepository.findSpecificClassByClassId(CLASS_ID)).thenReturn(specificClass);

        // act
        specificClass = specificClassService.updateSpecificClass(CLASS_ID, classType.getClassName(), schedule.getYear(), CLASS_NAME, DESCRIPTION, DATE, START_TIME, HOUR_DURATION, MAX_CAPACITY, CURRENT_CAPACITY, REGISTRATION_FEE);

        // assert
        assertNotNull(specificClass);
        assertEquals(CLASS_ID, specificClass.getClassId());
        assertEquals(CLASS_NAME, specificClass.getSpecificClassName());
        assertEquals(DESCRIPTION, specificClass.getDescription());
        assertEquals(Date.valueOf(DATE), specificClass.getDate());
        assertEquals(Time.valueOf(START_TIME), specificClass.getStartTime());
        assertEquals(HOUR_DURATION, specificClass.getHourDuration());
        assertEquals(MAX_CAPACITY, specificClass.getMaxCapacity());
        assertEquals(CURRENT_CAPACITY, specificClass.getCurrentCapacity());
        assertEquals(REGISTRATION_FEE, specificClass.getRegistrationFee());
        assertEquals(classType, specificClass.getClassType());
        assertEquals(schedule, specificClass.getSchedule());
    }

    @Test
    public void testUpdateSpecificClassClassTypeNotApproved() {
        // arrange
        classType.setIsApproved(false);
        when(specificClassRepository.findSpecificClassByClassId(CLASS_ID)).thenReturn(specificClass);

        // act
        Exception exception = assertThrows(SCSException.class, () -> specificClassService.updateSpecificClass(CLASS_ID, classType.getClassName(), schedule.getYear(), CLASS_NAME, DESCRIPTION, DATE, START_TIME, HOUR_DURATION, MAX_CAPACITY, CURRENT_CAPACITY, REGISTRATION_FEE));

        // assert
        assertEquals("Class type " + classType.getClassName() + " is not approved.", exception.getMessage());
        classType.setIsApproved(true);
    }

    @Test
    public void testUpdateSpecificClassNotFound() {
        //
        when(specificClassRepository.findSpecificClassByClassId(CLASS_ID)).thenReturn(null);

        // act
        Exception exception = assertThrows(SCSException.class, () -> specificClassService.updateSpecificClass(CLASS_ID, classType.getClassName(), schedule.getYear(), CLASS_NAME, DESCRIPTION, DATE, START_TIME, HOUR_DURATION, MAX_CAPACITY, CURRENT_CAPACITY, REGISTRATION_FEE));

        // assert
        assertEquals("Specific class with id " + CLASS_ID + " not found.", exception.getMessage());
    }

    @Test
    public void testUpdateSpecificClassNullClassName() {
        // act
        Exception exception = assertThrows(SCSException.class, () -> specificClassService.updateSpecificClass(CLASS_ID, null, schedule.getYear(), CLASS_NAME, DESCRIPTION, DATE, START_TIME, HOUR_DURATION, MAX_CAPACITY, CURRENT_CAPACITY, REGISTRATION_FEE));

        // assert
        assertEquals("Class type cannot be empty.", exception.getMessage());
    }

    @Test
    public void testUpdateSpecificClassNullDescription() {
        // act
        Exception exception = assertThrows(SCSException.class, () -> specificClassService.updateSpecificClass(CLASS_ID, classType.getClassName(), schedule.getYear(), CLASS_NAME, null, DATE, START_TIME, HOUR_DURATION, MAX_CAPACITY, CURRENT_CAPACITY, REGISTRATION_FEE));

        // assert
        assertEquals("Description cannot be empty.", exception.getMessage());
    }

    @Test
    public void testUpdateSpecificClassNullDate() {
        // act
        Exception exception = assertThrows(SCSException.class, () -> specificClassService.updateSpecificClass(CLASS_ID, classType.getClassName(), schedule.getYear(), CLASS_NAME, DESCRIPTION, null, START_TIME, HOUR_DURATION, MAX_CAPACITY, CURRENT_CAPACITY, REGISTRATION_FEE));

        // assert
        assertEquals("Invalid date.", exception.getMessage());
    }

    @Test
    public void testUpdateSpecificClassNullStartTime() {
        // act
        Exception exception = assertThrows(SCSException.class, () -> specificClassService.updateSpecificClass(CLASS_ID, classType.getClassName(), schedule.getYear(), CLASS_NAME, DESCRIPTION, DATE, null, HOUR_DURATION, MAX_CAPACITY, CURRENT_CAPACITY, REGISTRATION_FEE));

        // assert
        assertEquals("Invalid start time.", exception.getMessage());
    }

    @Test
    public void testUpdateSpecificClassNegativeHourDuration() {
        // act
        Exception exception = assertThrows(SCSException.class, () -> specificClassService.updateSpecificClass(CLASS_ID, classType.getClassName(), schedule.getYear(), CLASS_NAME, DESCRIPTION, DATE, START_TIME, -1, MAX_CAPACITY, CURRENT_CAPACITY, REGISTRATION_FEE));

        // assert
        assertEquals("The duration cannot be negative.", exception.getMessage());
    }

    @Test
    public void testUpdateSpecificClassNegativeMaxCapacity() {
        // act
        Exception exception = assertThrows(SCSException.class, () -> specificClassService.updateSpecificClass(CLASS_ID, classType.getClassName(), schedule.getYear(), CLASS_NAME, DESCRIPTION, DATE, START_TIME, HOUR_DURATION, -1, CURRENT_CAPACITY, REGISTRATION_FEE));

        // assert
        assertEquals("Maximum capacity must be greater than 0.", exception.getMessage());
    }

    @Test
    public void testUpdateSpecificClassNegativeCurrentCapacity() {
        // act
        Exception exception = assertThrows(SCSException.class, () -> specificClassService.updateSpecificClass(CLASS_ID, classType.getClassName(), schedule.getYear(), CLASS_NAME, DESCRIPTION, DATE, START_TIME, HOUR_DURATION, MAX_CAPACITY, -1, REGISTRATION_FEE));

        // assert
        assertEquals("Current capacity cannot be smaller than 0.", exception.getMessage());
    }

    @Test
    public void testUpdateSpecificClassCurrentCapacityGreaterThanMaxCapacity() {
        // act
        Exception exception = assertThrows(SCSException.class, () -> specificClassService.updateSpecificClass(CLASS_ID, classType.getClassName(), schedule.getYear(), CLASS_NAME, DESCRIPTION, DATE, START_TIME, HOUR_DURATION, MAX_CAPACITY, MAX_CAPACITY + 1, REGISTRATION_FEE));

        // assert
        assertEquals("Current capacity must be less than or equal to the max capacity.", exception.getMessage());
    }

    @Test
    public void testUpdateSpecificClassNegativeRegistrationFee() {
        // act
        Exception exception = assertThrows(SCSException.class, () -> specificClassService.updateSpecificClass(CLASS_ID, classType.getClassName(), schedule.getYear(), CLASS_NAME, DESCRIPTION, DATE, START_TIME, HOUR_DURATION, MAX_CAPACITY, CURRENT_CAPACITY, -1));

        // assert
        assertEquals("Registration fee cannot be negative.", exception.getMessage());
    }

    @Test
    public void testGetAllSpecificClasses() {
        // set up
        SpecificClass specificClass1 = new SpecificClass();
        specificClass1.setClassId(CLASS_ID);
        specificClass1.setSchedule(schedule);

        SpecificClass specificClass2 = new SpecificClass();
        specificClass2.setClassId(CLASS_ID);
        specificClass2.setSchedule(schedule);

        SpecificClass specificClass3 = new SpecificClass();
        specificClass3.setClassId(CLASS_ID);
        specificClass3.setSchedule(schedule);

        when(specificClassRepository.findSpecificClassByScheduleYear(schedule.getYear())).thenReturn(List.of(specificClass1, specificClass2, specificClass3));

        // act
        List<SpecificClass> specificClasses = specificClassService.getAllSpecificClasses(schedule.getYear());

        // assert
        assertNotNull(specificClasses);
        assertEquals(3, specificClasses.size());
        assertTrue(specificClasses.contains(specificClass1));
        assertTrue(specificClasses.contains(specificClass2));
        assertTrue(specificClasses.contains(specificClass3));
    }

    @Test
    public void testDeleteSpecificClass() {
        // arrange
        specificClass = new SpecificClass();
        specificClass.setClassId(CLASS_ID);
        when(specificClassRepository.findSpecificClassByClassId(CLASS_ID)).thenReturn(specificClass);

        // act
        specificClassService.deleteSpecificClass(CLASS_ID);

        // assert
        verify(specificClassRepository, times(1)).delete(specificClass);
    }

    @Test
    public void testDeleteSpecificClassNotFound() {
        // act
        Exception exception = assertThrows(SCSException.class, () -> specificClassService.deleteSpecificClass(CLASS_ID));

        // assert
        assertEquals("Specific class with id " + CLASS_ID + " not found.", exception.getMessage());
    }

    @Test
    public void testDeleteAllSpecificClasses() {
        // act
        specificClassService.deleteAllSpecificClasses();

        // assert
        verify(specificClassRepository, times(1)).deleteAll();
    }

    @Test
    public void testDeleteAllSpecificClassesByYear() {
        // set up
        SpecificClass specificClass1 = new SpecificClass();
        specificClass1.setClassId(CLASS_ID);
        specificClass1.setSchedule(schedule);

        SpecificClass specificClass2 = new SpecificClass();
        specificClass2.setClassId(CLASS_ID);
        specificClass2.setSchedule(schedule);

        SpecificClass specificClass3 = new SpecificClass();
        specificClass3.setClassId(CLASS_ID);
        specificClass3.setSchedule(schedule);

        when(specificClassRepository.findSpecificClassByScheduleYear(schedule.getYear())).thenReturn(List.of(specificClass1, specificClass2, specificClass3));

        // act
        specificClassService.deleteAllSpecificClassesByYear(schedule.getYear());

        // assert
        verify(specificClassRepository, times(3)).delete(any(SpecificClass.class));
    }
}