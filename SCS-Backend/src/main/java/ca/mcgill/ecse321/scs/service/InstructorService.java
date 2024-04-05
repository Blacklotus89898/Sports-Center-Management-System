package ca.mcgill.ecse321.scs.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.scs.dao.CustomerRepository;
import ca.mcgill.ecse321.scs.dao.InstructorRepository;
import ca.mcgill.ecse321.scs.dao.OwnerRepository;
import ca.mcgill.ecse321.scs.exception.SCSException;
import ca.mcgill.ecse321.scs.model.Instructor;

import java.util.regex.Pattern; 

/**
 * The InstructorService class provides methods for managing instructors.
 * It interacts with the InstructorRepository, OwnerRepository, and CustomerRepository
 * to perform CRUD operations on instructors.
 */
@Service
public class InstructorService {
    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private CustomerRepository customerRepository;

    // regex for email validation
    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                        "[a-zA-Z0-9_+&*-]+)*@" + 
                        "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                        "A-Z]{2,7}$"; 

    /**
     * Create a new instructor with the specified email, password, name, and image.
     * @param email
     * @param password
     * @param name
     * @param image
     * @return
     */
    @Transactional
    public Instructor createInstructor(String email, String password, String name, byte[] image) {
        if (email == null || email.trim().length() == 0) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Email cannot be empty.");
        } else if (!Pattern.compile(emailRegex).matcher(email).matches()) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Email is not valid.");
        } else if (password == null || password.trim().length() == 0) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Password cannot be empty.");
        } else if (name == null || name.trim().length() == 0) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Name cannot be empty.");
        }
        
        // email in use by another account
        if (instructorRepository.findInstructorByEmail(email) != null || ownerRepository.findOwnerByEmail(email) != null
                || customerRepository.findCustomerByEmail(email) != null) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "An account with this email already exists.");
        }

        Instructor instructor = new Instructor();
        instructor.setEmail(email);
        instructor.setPassword(password);
        instructor.setName(name);
        instructor.setCreationDate(Date.valueOf(LocalDate.now()));
        instructor.setImage(image);
        instructorRepository.save(instructor);

        return instructor;
    }

    /**
     * Retrieves the instructor with the specified ID.
     * @param id
     * @return
     */
    @Transactional
    public Instructor getInstructorById(int id) {
        Instructor instructor = instructorRepository.findInstructorByAccountId(id);
        if (instructor == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Instructor not found.");
        }
        return instructor;
    }

    /**
     * Retrieves the instructor with the specified email.
     * @param email
     * @return
     */
    @Transactional
    public List<Instructor> getAllInstructors() {
        return ServiceUtils.toList(instructorRepository.findAll());
    }

    /**
     * Updates the instructor with the specified ID.
     * @param accountId
     * @param email
     * @param password
     * @param name
     * @param image
     * @return
     */
    @Transactional
    public Instructor updateInstructor(int accountId, String email, String password, String name, byte[] image) {
        Instructor instructor = instructorRepository.findInstructorByAccountId(accountId);
        if (instructor == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Instructor not found.");
        }

        // email in use by another account
        Instructor instructorByEmail = instructorRepository.findInstructorByEmail(email);
        if ((instructorByEmail != null && instructorByEmail.getAccountId() != accountId) || ownerRepository.findOwnerByEmail(email) != null
                || customerRepository.findCustomerByEmail(email) != null) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "An account with this email already exists.");
        }

        if (email != null && email.trim().length() > 0) {
            if (!Pattern.compile(emailRegex).matcher(email).matches()) {
                throw new SCSException(HttpStatus.BAD_REQUEST, "Email is not valid.");
            }
            instructor.setEmail(email);
        }

        if (password != null && password.trim().length() > 0) {
            instructor.setPassword(password);
        } else {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Password cannot be empty.");
        }

        if (name != null && name.trim().length() > 0) {
            instructor.setName(name);
        } else {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Name cannot be empty.");
        }

        instructor.setImage(image);
        instructorRepository.save(instructor);
        return instructor;
    }

    /**
     * Deletes the instructor with the specified ID.
     * @param accountId
     */
    @Transactional
    public void deleteInstructor(int accountId) {
        Instructor instructor = instructorRepository.findInstructorByAccountId(accountId);
        if (instructor == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Instructor not found.");
        }
        instructorRepository.delete(instructor);
    }

    /**
     * Deletes all instructors.
     */
    @Transactional
    public void deleteAllInstructors() {
        instructorRepository.deleteAll();
    }
}