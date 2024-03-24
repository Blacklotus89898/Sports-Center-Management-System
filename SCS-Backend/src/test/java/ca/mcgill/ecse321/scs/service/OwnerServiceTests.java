package ca.mcgill.ecse321.scs.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import java.sql.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.scs.exception.SCSException;
import ca.mcgill.ecse321.scs.model.Owner;
import ca.mcgill.ecse321.scs.dao.InstructorRepository;
import ca.mcgill.ecse321.scs.dao.OwnerRepository;
import ca.mcgill.ecse321.scs.dao.CustomerRepository;


/**
 * This class contains unit tests for the OwnerService class.
 * It tests various methods of the OwnerService class for creating, updating, retrieving, and deleting owners.
 * The tests cover different scenarios such as creating an owner with valid and invalid inputs, updating an owner with valid and invalid inputs,
 * retrieving an owner by ID, and deleting an owner by ID.
 * The tests use Mockito to mock the dependencies of the OwnerService class.
 */
@SpringBootTest
public class OwnerServiceTests {
    @Mock
    private InstructorRepository instructorRepository;
    @Mock
    private OwnerRepository ownerRepository;
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private OwnerService ownerService;

    Owner owner;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        int id = 1;
        String email = "owner@sports.center";
        String password = "password";
        String name = "Owner Name";
        Date creationDate = Date.valueOf(LocalDate.now());
        
        owner = new Owner();
        owner.setAccountId(id);
        owner.setEmail(email);
        owner.setPassword(password);
        owner.setName(name);
        owner.setCreationDate(creationDate);

        when(ownerRepository.save(any(Owner.class))).thenReturn(owner);
        when(ownerRepository.findOwnerByEmailAndPassword(email, password)).thenReturn(owner);
        when(ownerRepository.findOwnerByAccountId(id)).thenReturn(owner);
    }

    @Test
    public void testCreateOwner() {
        // set up
        String email = owner.getEmail();
        String password = owner.getPassword();
        String name = owner.getName();

        Owner createdOwner = null;

        when(ownerRepository.findOwnerByEmail(email)).thenReturn(null);
        when(ownerRepository.findOwnerByEmail(email)).thenReturn(null);
        when(ownerRepository.findOwnerByEmail(email)).thenReturn(null);

        // act
        createdOwner = ownerService.createOwner(email, password, name);

        // assert
        assertNotNull(createdOwner);
        assertEquals(owner.getEmail(), createdOwner.getEmail());
        assertEquals(owner.getPassword(), createdOwner.getPassword());
        assertEquals(owner.getName(), createdOwner.getName());
    }

    @Test
    public void testCreateOwnerNullEmail() {
        // set up
        String email = null;
        String password = owner.getPassword();
        String name = owner.getName();

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            ownerService.createOwner(email, password, name);
        });

        // assert
        assertEquals("Email cannot be empty.", exception.getMessage());
    }

    @Test
    public void testCreateOwnerInvalidEmail() {
        // set up
        String email = "invalid email";
        String password = owner.getPassword();
        String name = owner.getName();

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            ownerService.createOwner(email, password, name);
        });

        // assert
        assertEquals("Email is not valid.", exception.getMessage());
    }

    @Test
    public void testCreateOwnerNullPassword() {
        // set up
        String email = owner.getEmail();
        String password = null;
        String name = owner.getName();

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            ownerService.createOwner(email, password, name);
        });

        // assert
        assertEquals("Password cannot be empty.", exception.getMessage());
    }

    @Test
    public void testCreateOwnerNullName() {
        // set up
        String email = owner.getEmail();
        String password = owner.getPassword();
        String name = null;

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            ownerService.createOwner(email, password, name);
        });

        // assert
        assertEquals("Name cannot be empty.", exception.getMessage());
    }

    @Test
    public void testCreateOwnerExistingAccount() {
        // set up
        String email = owner.getEmail();
        String password = owner.getPassword();
        String name = owner.getName();

        when(ownerRepository.findOwnerByEmail(email)).thenReturn(owner);

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            ownerService.createOwner(email, password, name);
        });

        // assert
        assertEquals("An account with this email already exists.", exception.getMessage());
    }

    @Test
    public void testGetOwnerById() {
        // set up
        int id = owner.getAccountId();

        // act
        Owner foundOwner = ownerService.getOwnerById(id);

        // assert
        assertNotNull(foundOwner);
        assertEquals(owner.getAccountId(), foundOwner.getAccountId());
        assertEquals(owner.getEmail(), foundOwner.getEmail());
        assertEquals(owner.getPassword(), foundOwner.getPassword());
        assertEquals(owner.getName(), foundOwner.getName());
        assertEquals(owner.getCreationDate(), foundOwner.getCreationDate());
    }

    @Test
    public void testGetOwnerByIdNotFound() {
        // set up
        int id = 2;

        when(ownerRepository.findOwnerByAccountId(id)).thenReturn(null);

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            ownerService.getOwnerById(id);
        });

        // assert
        assertEquals("Owner not found.", exception.getMessage());
    }

    @Test
    public void testUpdateOwner() {
        // set up
        int id = owner.getAccountId();
        String email = "random@sports.center";
        String password = "new password";
        String name = "New Name";

        when(ownerRepository.findOwnerByAccountId(id)).thenReturn(owner);

        // act
        Owner updatedOwner = ownerService.updateOwner(id, email, password, name);

        // assert
        assertNotNull(updatedOwner);
        assertEquals(owner.getAccountId(), updatedOwner.getAccountId());
        assertEquals(email, updatedOwner.getEmail());
        assertEquals(password, updatedOwner.getPassword());
        assertEquals(name, updatedOwner.getName());
    }

    @Test
    public void testUpdateOwnerNotFound() {
        // set up
        int id = 2;
        String email = "any@sports.center";
        String password = "new password";
        String name = "New Name";

        when(ownerRepository.findOwnerByAccountId(id)).thenReturn(null);

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            ownerService.updateOwner(id, email, password, name);
        });

        // assert
        assertEquals("Owner not found.", exception.getMessage());
    }

    @Test
    public void testUpdateOwnerInvalidEmail() {
        // set up
        int id = owner.getAccountId();
        String email = "invalid email";
        String password = owner.getPassword();
        String name = owner.getName();

        when(ownerRepository.findOwnerByAccountId(id)).thenReturn(owner);

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            ownerService.updateOwner(id, email, password, name);
        });

        // assert
        assertEquals("Email is not valid.", exception.getMessage());
    }

    @Test
    public void testUpdateOwnerNullPassword() {
        // set up
        int id = owner.getAccountId();
        String email = owner.getEmail();
        String password = null;
        String name = owner.getName();

        when(ownerRepository.findOwnerByAccountId(id)).thenReturn(owner);

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            ownerService.updateOwner(id, email, password, name);
        });

        // assert
        assertEquals("Password cannot be empty.", exception.getMessage());
    }

    @Test
    public void testUpdateOwnerNullName() {
        // set up
        int id = owner.getAccountId();
        String email = owner.getEmail();
        String password = owner.getPassword();
        String name = null;

        when(ownerRepository.findOwnerByAccountId(id)).thenReturn(owner);

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            ownerService.updateOwner(id, email, password, name);
        });

        // assert
        assertEquals("Name cannot be empty.", exception.getMessage());
    }

    @Test
    public void testDeleteOwnerById() {
        // set up
        int id = owner.getAccountId();

        when(ownerRepository.findOwnerByAccountId(id)).thenReturn(owner);

        // act
        ownerService.deleteOwner(id);

        // assert
        verify(ownerRepository, times(1)).delete(owner);
    }

    @Test
    public void testDeleteOwnerByIdNotFound() {
        // set up
        int id = 2;

        when(ownerRepository.findOwnerByAccountId(id)).thenReturn(null);

        // act
        SCSException exception = assertThrows(SCSException.class, () -> {
            ownerService.deleteOwner(id);
        });

        // assert
        assertEquals("Owner not found.", exception.getMessage());
    }
}
