package ca.mcgill.ecse321.scs.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.scs.exception.SCSException;
import ca.mcgill.ecse321.scs.model.Schedule;
import ca.mcgill.ecse321.scs.model.SpecificClass;
import ca.mcgill.ecse321.scs.model.TeachingInfo;
import ca.mcgill.ecse321.scs.dao.SpecificClassRepository;
import ca.mcgill.ecse321.scs.dao.TeachingInfoRepository;
import ca.mcgill.ecse321.scs.model.ClassType;
import ca.mcgill.ecse321.scs.model.Instructor;
import ca.mcgill.ecse321.scs.dao.InstructorRepository;

@SuppressWarnings("null")
@SpringBootTest
public class TeachingInfoServiceTests {
    @Mock
    private TeachingInfoRepository teachingInfoRepository;
    @InjectMocks
    private TeachingInfoService teachingInfoService;

    @Mock
    private InstructorRepository instructorRepository;
    @InjectMocks
    private InstructorService instructorService;

    @Mock
    private SpecificClassRepository specificClassRepository;
    @InjectMocks
    private SpecificClassService specificClassService;

    private Instructor instructor;
    private SpecificClass specificClass;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);                     // initializes mocks and injects them

        // manually inject the instructorService and specificClassService into teachingInfoService if required
        teachingInfoService.setInstructorService(instructorService);
        teachingInfoService.setSpecificClassService(specificClassService);

        int id1 = 1;
        Date date1 = Date.valueOf("2007-10-02");
        String name1 = "Bob";
        String email1 = "bob@gmail.com";
        String password1 = "bobpassword";

        Instructor instructor = new Instructor(id1, date1, name1, email1, password1);
        this.instructor = instructor;

        int id3 = 2;
        Date date3 = Date.valueOf("2008-11-03");
        String name3 = "Jeremy";
        String email3 = "jeremy@gmail.com";
        String password3 = "jeremypassword";
        Instructor instructor2 = new Instructor(id3, date3, name3, email3, password3);

        int id2 = 1234;
        String specificClassName = "Yoga with Bob";
        String description = "Bring your own mat";
        Date date2 = Date.valueOf("2020-01-01");
        Time startTime = Time.valueOf("10:00:00");
        int hourDuration = 3;
        int maxCapacity = 200;
        int currentCapacity = 50;
        double registrationFee = 10.00;
        ClassType classType = new ClassType("Yoga", "Come relax with some yoga", true);
        Schedule schedule = new Schedule(2020);
        SpecificClass specificClass = new SpecificClass(id2, specificClassName, description, date2, startTime, hourDuration, maxCapacity, currentCapacity, registrationFee, classType, schedule);
        this.specificClass = specificClass;

        when(instructorRepository.findInstructorByAccountId(id1)).thenReturn(instructor);
        when(instructorRepository.findInstructorByAccountId(id3)).thenReturn(instructor2);
        when(specificClassRepository.findSpecificClassByClassId(id2)).thenReturn(specificClass);
    }

    @Test
    public void testCreateValidTeachingInfo() {
        // set up
        int accountId = 1;
        int specificClassId = 1234;
        int teachingInfoId = 5;

        when(teachingInfoRepository.save(any(TeachingInfo.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        TeachingInfo teachingInfo = teachingInfoService.createTeachingInfo(accountId, specificClassId, teachingInfoId);

        // assert
        assertNotNull(teachingInfo);
        assertEquals(accountId, teachingInfo.getInstructor().getAccountId());
        assertEquals(specificClassId, teachingInfo.getSpecificClass().getClassId());
        assertEquals(teachingInfoId, teachingInfo.getTeachingInfoId());

        verify(teachingInfoRepository, times(1)).save(any(TeachingInfo.class));
    }

    @Test
    public void testCreateInvalidTeachingInfoExists() {
        // teaching info with that id already exists

        // set up
        int accountId = 1;
        int specificClassId = 1234;

        int teachingInfoId = 1;

        TeachingInfo existingTeachingInfo = new TeachingInfo(teachingInfoId, this.instructor, this.specificClass);
        when(teachingInfoRepository.findTeachingInfoByTeachingInfoId(teachingInfoId)).thenReturn(existingTeachingInfo);

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            teachingInfoService.createTeachingInfo(accountId, specificClassId, teachingInfoId);
        });

        // assert
        assertEquals("Teaching info with id " + teachingInfoId + " already exists.", exception.getMessage());
    }


    @Test
    public void testCreateInvalidTeachingInfoInvalidInstructor() {
        // instructor is invalid

        // set up
        int accountId = -1;
        int specificClassId = 1234;
        int teachingInfoId = 5;

        when(teachingInfoRepository.save(any(TeachingInfo.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            teachingInfoService.createTeachingInfo(accountId, specificClassId, teachingInfoId);
        });

        // assert
        assertEquals("Instructor not found.", exception.getMessage());
    }

    @Test
    public void testCreateInvalidTeachingInfoInvalidSpecificClass() {
        // specific class is invalid

        // set up
        int accountId = 1;
        int specificClassId = -1;
        int teachingInfoId = 5;

        when(teachingInfoRepository.save(any(TeachingInfo.class))).thenAnswer((invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            teachingInfoService.createTeachingInfo(accountId, specificClassId, teachingInfoId);
        });

        // assert
        assertEquals("Specific class with id " + specificClassId + " not found.", exception.getMessage());
    }

    @Test
    public void testUpdateValidTeachingInfo() {
        // set up
        int teachingInfoId = 5;
        int accountId = 1;
        int specificClassId = 1234;

        TeachingInfo teachingInfo = new TeachingInfo(teachingInfoId, this.instructor, this.specificClass);
        when(teachingInfoRepository.findTeachingInfoByTeachingInfoId(teachingInfoId)).thenReturn(teachingInfo);
        when(teachingInfoRepository.save(any(TeachingInfo.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        TeachingInfo updatedTeachingInfo = teachingInfoService.updateTeachingInfo(teachingInfoId, 2, specificClassId);

        // assert
        assertNotNull(updatedTeachingInfo);
        assertEquals(teachingInfoId, updatedTeachingInfo.getTeachingInfoId());
        assertEquals(2, updatedTeachingInfo.getInstructor().getAccountId());
        assertEquals(specificClassId, updatedTeachingInfo.getSpecificClass().getClassId());

        verify(teachingInfoRepository, times(1)).save(any(TeachingInfo.class));
    
    }

    @Test
    public void testDeleteCustomHours() {
        // set up
        int teachingInfoId = 5;
        int accountId = 1;
        int specificClassId = 1234;

        TeachingInfo teachingInfo = new TeachingInfo(teachingInfoId, this.instructor, this.specificClass);
        when(teachingInfoRepository.findTeachingInfoByTeachingInfoId(teachingInfoId)).thenReturn(teachingInfo).thenReturn(null);

        // act
        teachingInfoService.deleteTeachingInfo(teachingInfoId);
        verify(teachingInfoRepository, times(1)).delete(teachingInfo);

        // act & assert exception
        Exception exception = assertThrows(SCSException.class, () -> {
            teachingInfoService.getTeachingInfo(teachingInfoId);
        });

        // assert
        assertEquals("Teaching info with id " + teachingInfoId + " not found.", exception.getMessage());
        verify(teachingInfoRepository, times(1)).delete(teachingInfo);
    }

    @Test
    public void testUpdateTeachingInfoInvalidInstructor() {
        // trying to update with an invalid instructor
        // set up
        int teachingInfoId = 5;
        int accountId = 1;
        int specificClassId = 1234;

        TeachingInfo teachingInfo = new TeachingInfo(teachingInfoId, this.instructor, this.specificClass);
        when(teachingInfoRepository.findTeachingInfoByTeachingInfoId(teachingInfoId)).thenReturn(teachingInfo);
        when(teachingInfoRepository.save(any(TeachingInfo.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            teachingInfoService.updateTeachingInfo(teachingInfoId, -1, specificClassId);
        });

        // assert
        assertEquals("Instructor not found.", exception.getMessage());
    
    }

    @Test
    public void testGetAllTeachingInfos() {
        // set up
        int teachingInfoId = 5;
        int accountId = 1;
        int specificClassId = 1234;

        TeachingInfo teachingInfo = new TeachingInfo(teachingInfoId, this.instructor, this.specificClass);
        TeachingInfo teachingInfo2 = new TeachingInfo(6, instructorService.getInstructorById(2), this.specificClass);
        when(teachingInfoRepository.findAll()).thenReturn(List.of(teachingInfo, teachingInfo2));

        // act
        List<TeachingInfo> teachingInfosList = teachingInfoService.getAllTeachingInfos();

        // assert
        assertNotNull(teachingInfosList);
        assertEquals(2, teachingInfosList.size());
        assertTrue(teachingInfosList.contains(teachingInfo));
        assertTrue(teachingInfosList.contains(teachingInfo2));
    }

    @Test
    public void testUpdateTeachingInfoInvalidSpecificClass() {
        // trying to update with an invalid specific class
        // set up
        int teachingInfoId = 5;
        int accountId = 1;
        int specificClassId = 1234;

        TeachingInfo teachingInfo = new TeachingInfo(teachingInfoId, this.instructor, this.specificClass);
        when(teachingInfoRepository.findTeachingInfoByTeachingInfoId(teachingInfoId)).thenReturn(teachingInfo);
        when(teachingInfoRepository.save(any(TeachingInfo.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            teachingInfoService.updateTeachingInfo(teachingInfoId, accountId, -1);
        });

        // assert
        assertEquals("Specific class with id -1 not found.", exception.getMessage());
    
    }

    @Test
    public void testCreateTeachingInfoInvalidInstructor(){
        // set up
        int accountId = -1;
        int specificClassId = 1234;
        int teachingInfoId = 5;

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            teachingInfoService.createTeachingInfo(accountId, specificClassId, teachingInfoId);
        });

        // assert
        assertEquals("Instructor not found.", exception.getMessage());
    
    }

    @Test
    public void testDeleteAllTeachingInfos(){
        // set up
        int teachingInfoId = 5;
        int accountId = 1;
        int specificClassId = 1234;

        TeachingInfo teachingInfo = new TeachingInfo(teachingInfoId, this.instructor, this.specificClass);
        TeachingInfo teachingInfo2 = new TeachingInfo(6, instructorService.getInstructorById(2), this.specificClass);
        when(teachingInfoRepository.findAll()).thenReturn(List.of(teachingInfo, teachingInfo2));

        // act
        teachingInfoService.deleteAllTeachingInfos();
        verify(teachingInfoRepository, times(1)).deleteAll();
    
    }

    @Test
    public void testGetTeachingInfo() {
        // set up
        int teachingInfoId = 5;
        int accountId = 1;
        int specificClassId = 1234;

        TeachingInfo teachingInfo = new TeachingInfo(teachingInfoId, this.instructor, this.specificClass);
        when(teachingInfoRepository.findTeachingInfoByTeachingInfoId(teachingInfoId)).thenReturn(teachingInfo);

        // act
        TeachingInfo returnedTeachingInfo = teachingInfoService.getTeachingInfo(teachingInfoId);

        // assert
        assertNotNull(returnedTeachingInfo);
        assertEquals(teachingInfoId, returnedTeachingInfo.getTeachingInfoId());
        assertEquals(accountId, returnedTeachingInfo.getInstructor().getAccountId());
        assertEquals(specificClassId, returnedTeachingInfo.getSpecificClass().getClassId());
    
    }

}





    
/* 
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
        when(customHoursRepository.findCustomHoursByName(name, year)).thenReturn(existingCustomHours);

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
        when(customHoursRepository.findCustomHoursByName(name, year)).thenReturn(customHours);

        // act
        CustomHours returnedCustomHours = customHoursService.getCustomHours(name, year);

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
        int year = schedule.getYear();

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            customHoursService.getCustomHours(name, year);
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
        when(customHoursRepository.findCustomHoursByName(name, year)).thenReturn(customHours);
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
        when(customHoursRepository.findCustomHoursByName(name, year)).thenReturn(customHours);
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
        when(customHoursRepository.findCustomHoursByName(name, year)).thenReturn(customHours);
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
        when(customHoursRepository.findCustomHoursByName(name, year)).thenReturn(customHours);
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
    public void testUpdateCustomHoursInvalidTime() {
        // set up
        String name = "cny";
        String description = "chinese new year";
        LocalDate date = LocalDate.of(2023, 1, 28);
        LocalTime openTime = LocalTime.of(0, 0);
        LocalTime closeTime = LocalTime.of(23, 59);
        int year = 2023;

        CustomHours customHours = new CustomHours(name, description, Date.valueOf(date), Time.valueOf(openTime), Time.valueOf(closeTime), this.schedule);
        when(customHoursRepository.findCustomHoursByName(name, year)).thenReturn(customHours);
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
        when(customHoursRepository.findCustomHoursByName(name, year)).thenReturn(customHours);
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
        when(customHoursRepository.findCustomHoursByName(name, year)).thenReturn(customHours);
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
        int year = 2022;

        CustomHours customHours = new CustomHours(name, description, Date.valueOf(date), Time.valueOf(openTime), Time.valueOf(closeTime), this.schedule);
        when(customHoursRepository.findCustomHoursByName(name, year)).thenReturn(customHours);
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
        int year = schedule.getYear();

        CustomHours customHours = new CustomHours(name, description, Date.valueOf(date), Time.valueOf(openTime), Time.valueOf(closeTime), this.schedule);
        CustomHours customHours2 = new CustomHours(name, description, Date.valueOf(date2), Time.valueOf(openTime), Time.valueOf(closeTime), this.schedule);
        when(customHoursRepository.findAll()).thenReturn(List.of(customHours, customHours2));

        // act
        List<CustomHours> customHoursList = customHoursService.getAllCustomHours(year);

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
        int year = schedule.getYear();

        CustomHours customHours = new CustomHours(name, description, Date.valueOf(date), Time.valueOf(openTime), Time.valueOf(closeTime), this.schedule);
        when(customHoursRepository.findCustomHoursByName(name, year)).thenReturn(customHours).thenReturn(null);

        // act
        customHoursService.deleteCustomHours(name, year);
        verify(customHoursRepository, times(1)).delete(customHours);

        // act & assert exception
        Exception exception = assertThrows(SCSException.class, () -> {
            customHoursService.getCustomHours(name, year);
        });

        // assert
        assertEquals("Custom hours with name " + name + " does not exist.", exception.getMessage());
        verify(customHoursRepository, times(1)).delete(customHours);
    }

    @Test
    public void testDeleteCustomHoursNull() {
        // set up
        String nonExistentName = "cny";
        int year = schedule.getYear();
        when(customHoursRepository.findCustomHoursByName(nonExistentName, year)).thenReturn(null);

        // act & assert
        SCSException exception = assertThrows(SCSException.class, () -> {
            customHoursService.deleteCustomHours(nonExistentName, year);
        });

        // assert
        assertEquals("Custom hours with name " + nonExistentName + " not found in year " + year + ".", exception.getMessage());
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
        int year = 2023;
        
        CustomHours customHours = new CustomHours(name, description, Date.valueOf(date), Time.valueOf(openTime), Time.valueOf(closeTime), this.schedule);
        CustomHours customHours2 = new CustomHours(name, description, Date.valueOf(date2), Time.valueOf(openTime), Time.valueOf(closeTime), this.schedule);

        // mock the initial state before deletion
        when(customHoursRepository.findAll()).thenReturn(List.of(customHours, customHours2));

        // act: Delete all custom hours
        customHoursService.deleteAllCustomHours(year);
        when(customHoursRepository.findAll()).thenReturn(Collections.emptyList()); // adjust the mock to reflect the post-deletion state

        List<CustomHours> customHoursList = customHoursService.getAllCustomHours(year);

        // assert
        assertNotNull(customHoursList);
        assertEquals(0, customHoursList.size());
        verify(customHoursRepository, times(1)).deleteAll();
    }
}
*/