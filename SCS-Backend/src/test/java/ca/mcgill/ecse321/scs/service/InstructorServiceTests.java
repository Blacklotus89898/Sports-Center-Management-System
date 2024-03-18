package ca.mcgill.ecse321.scs.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Collections;

import java.time.LocalDate;

import java.sql.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.scs.exception.SCSException;
import ca.mcgill.ecse321.scs.model.Instructor;
import ca.mcgill.ecse321.scs.dao.InstructorRepository;
import ca.mcgill.ecse321.scs.dao.OwnerRepository;
import ca.mcgill.ecse321.scs.dao.CustomerRepository;

@SuppressWarnings("null")
@SpringBootTest
public class InstructorServiceTests {
    @Mock
    private InstructorRepository instructorRepository;
    @Mock
    private OwnerRepository ownerRepository;
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private InstructorService instructorService;

    Instructor instructor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        int id = 1;
        String email = "instructor@sports.center";
        String password = "password";
        String name = "Instructor Name";
        Date creationDate = Date.valueOf(LocalDate.now());
        
        instructor = new Instructor();
        instructor.setAccountId(id);
        instructor.setEmail(email);
        instructor.setPassword(password);
        instructor.setName(name);
        instructor.setCreationDate(creationDate);

        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);
        when(instructorRepository.findInstructorByEmailAndPassword(email, password)).thenReturn(instructor);
        when(instructorRepository.findInstructorByAccountId(id)).thenReturn(instructor);
    }

    @Test
    public void testCreateInstructor() {
        // set up
        String email = instructor.getEmail();
        String password = instructor.getPassword();
        String name = instructor.getName();

        Instructor createdInstructor = null;

        when(instructorRepository.findInstructorByEmail(email)).thenReturn(null);
        when(instructorRepository.findInstructorByEmail(email)).thenReturn(null);
        when(instructorRepository.findInstructorByEmail(email)).thenReturn(null);

        // act
        createdInstructor = instructorService.createInstructor(email, password, name);

        // assert
        assertNotNull(createdInstructor);
        assertEquals(instructor.getEmail(), createdInstructor.getEmail());
        assertEquals(instructor.getPassword(), createdInstructor.getPassword());
        assertEquals(instructor.getName(), createdInstructor.getName());
    }

    @Test
    public void testCreateInstructorNullEmail() {
        // set up
        String email = null;
        String password = instructor.getPassword();
        String name = instructor.getName();

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            instructorService.createInstructor(email, password, name);
        });

        // assert
        assertEquals("Email cannot be empty.", exception.getMessage());
    }

    @Test
    public void testCreateInstructorInvalidEmail() {
        // set up
        String email = "invalid email";
        String password = instructor.getPassword();
        String name = instructor.getName();

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            instructorService.createInstructor(email, password, name);
        });

        // assert
        assertEquals("Email is not valid.", exception.getMessage());
    }

    @Test
    public void testCreateInstructorNullPassword() {
        // set up
        String email = instructor.getEmail();
        String password = null;
        String name = instructor.getName();

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            instructorService.createInstructor(email, password, name);
        });

        // assert
        assertEquals("Password cannot be empty.", exception.getMessage());
    }

    @Test
    public void testCreateInstructorNullName() {
        // set up
        String email = instructor.getEmail();
        String password = instructor.getPassword();
        String name = null;

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            instructorService.createInstructor(email, password, name);
        });

        // assert
        assertEquals("Name cannot be empty.", exception.getMessage());
    }

    @Test
    public void testCreateInstructorExistingAccount() {
        // set up
        String email = instructor.getEmail();
        String password = instructor.getPassword();
        String name = instructor.getName();

        when(instructorRepository.findInstructorByEmail(email)).thenReturn(instructor);

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            instructorService.createInstructor(email, password, name);
        });

        // assert
        assertEquals("An account with this email already exists.", exception.getMessage());
    }

    @Test
    public void testGetInstructorById() {
        // set up
        int id = instructor.getAccountId();

        Instructor foundInstructor = null;

        // act
        foundInstructor = instructorService.getInstructorById(id);

        // assert
        assertNotNull(foundInstructor);
        assertEquals(instructor.getAccountId(), foundInstructor.getAccountId());
        assertEquals(instructor.getEmail(), foundInstructor.getEmail());
        assertEquals(instructor.getPassword(), foundInstructor.getPassword());
        assertEquals(instructor.getName(), foundInstructor.getName());
        assertEquals(instructor.getCreationDate(), foundInstructor.getCreationDate());
    }

    @Test
    public void testGetInstructorByIdNotFound() {
        // set up
        int id = 2;

        when(instructorRepository.findInstructorByAccountId(id)).thenReturn(null);

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            instructorService.getInstructorById(id);
        });

        // assert
        assertEquals("Instructor not found.", exception.getMessage());
    }

    @Test
    public void testGetAllInstructors() {
        // set up
        List<Instructor> instructors = Collections.singletonList(instructor);

        when(instructorRepository.findAll()).thenReturn(instructors);

        // act
        List<Instructor> foundInstructors = instructorService.getAllInstructors();

        // assert
        assertNotNull(foundInstructors);
        assertEquals(instructors.size(), foundInstructors.size());
        assertEquals(instructors.get(0).getAccountId(), foundInstructors.get(0).getAccountId());
        assertEquals(instructors.get(0).getEmail(), foundInstructors.get(0).getEmail());
        assertEquals(instructors.get(0).getPassword(), foundInstructors.get(0).getPassword());
        assertEquals(instructors.get(0).getName(), foundInstructors.get(0).getName());
        assertEquals(instructors.get(0).getCreationDate(), foundInstructors.get(0).getCreationDate());
    }

    @Test
    public void testUpdateInstructor() {
        // set up
        int id = instructor.getAccountId();
        String email = "random@sports.center";
        String password = "new password";
        String name = "New Name";

        Instructor updatedInstructor = null;

        when(instructorRepository.findInstructorByAccountId(id)).thenReturn(instructor);

        // act
        updatedInstructor = instructorService.updateInstructor(id, email, password, name);

        // assert
        assertNotNull(updatedInstructor);
        assertEquals(instructor.getAccountId(), updatedInstructor.getAccountId());
        assertEquals(email, updatedInstructor.getEmail());
        assertEquals(password, updatedInstructor.getPassword());
        assertEquals(name, updatedInstructor.getName());
    }

    @Test
    public void testUpdateInstructorNotFound() {
        // set up
        int id = 2;
        String email = "any@sports.center";
        String password = "new password";
        String name = "New Name";

        when(instructorRepository.findInstructorByAccountId(id)).thenReturn(null);

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            instructorService.updateInstructor(id, email, password, name);
        });

        // assert
        assertEquals("Instructor not found.", exception.getMessage());
    }

    @Test
    public void testUpdateInstructorInvalidEmail() {
        // set up
        int id = instructor.getAccountId();
        String email = "invalid email";
        String password = instructor.getPassword();
        String name = instructor.getName();

        when(instructorRepository.findInstructorByAccountId(id)).thenReturn(instructor);

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            instructorService.updateInstructor(id, email, password, name);
        });

        // assert
        assertEquals("Email is not valid.", exception.getMessage());
    }

    @Test
    public void testUpdateInstructorNullPassword() {
        // set up
        int id = instructor.getAccountId();
        String email = instructor.getEmail();
        String password = null;
        String name = instructor.getName();

        when(instructorRepository.findInstructorByAccountId(id)).thenReturn(instructor);

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            instructorService.updateInstructor(id, email, password, name);
        });

        // assert
        assertEquals("Password cannot be empty.", exception.getMessage());
    }

    @Test
    public void testUpdateInstructorNullName() {
        // set up
        int id = instructor.getAccountId();
        String email = instructor.getEmail();
        String password = instructor.getPassword();
        String name = null;

        when(instructorRepository.findInstructorByAccountId(id)).thenReturn(instructor);

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            instructorService.updateInstructor(id, email, password, name);
        });

        // assert
        assertEquals("Name cannot be empty.", exception.getMessage());
    }

    @Test
    public void testDeleteInstructor() {
        // set up
        int id = instructor.getAccountId();

        when(instructorRepository.findInstructorByAccountId(id)).thenReturn(instructor);

        // act
        instructorService.deleteInstructor(id);

        // assert
        verify(instructorRepository, times(1)).delete(instructor);
    }

    @Test
    public void testDeleteInstructorNotFound() {
        // set up
        int id = 2;

        when(instructorRepository.findInstructorByAccountId(id)).thenReturn(null);

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            instructorService.deleteInstructor(id);
        });

        // assert
        assertEquals("Instructor not found.", exception.getMessage());
    }

    @Test
    public void testDeleteAllInstructors() {
        // set up
        List<Instructor> instructors = Collections.singletonList(instructor);

        when(instructorRepository.findAll()).thenReturn(instructors);

        // act
        instructorService.deleteAllInstructors();

        // assert
        verify(instructorRepository, times(1)).deleteAll();
    }
}
