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

    @Test
    //test for trying to assign an instructor to a class they are already teaching
    public void testCreateInvalidTeachingInfoInstructorAlreadyTeachingClass() {
        // set up
        int accountId = 1;
        int specificClassId = 1234;
        int teachingInfoId = 5;

        TeachingInfo existingTeachingInfo = new TeachingInfo(teachingInfoId, this.instructor, this.specificClass);
        when(teachingInfoRepository.findAll()).thenReturn(List.of(existingTeachingInfo));

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            teachingInfoService.createTeachingInfo(accountId, specificClassId, teachingInfoId);
        });

        // assert
        assertEquals("Instructor with id " + accountId + " is already teaching class with id " + specificClassId, exception.getMessage());
    }

}