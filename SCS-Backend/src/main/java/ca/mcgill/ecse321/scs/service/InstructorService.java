package ca.mcgill.ecse321.scs.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.scs.dao.CustomerRepository;
import ca.mcgill.ecse321.scs.dao.InstructorRepository;
import ca.mcgill.ecse321.scs.dao.OwnerRepository;
import ca.mcgill.ecse321.scs.exception.SCSException;
import ca.mcgill.ecse321.scs.model.Instructor;

import java.util.regex.Pattern; 

public class InstructorService {
    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private CustomerRepository customerRepository;

    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                        "[a-zA-Z0-9_+&*-]+)*@" + 
                        "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                        "A-Z]{2,7}$"; 

    @Transactional
    public Instructor createInstructor(String email, String password, String name) {
        if (email == null || email.trim().length() == 0) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Email cannot be empty.");
        } else if (!Pattern.compile(emailRegex).matcher(email).matches()) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Email is not valid.");
        } else if (password == null || password.trim().length() == 0) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Password cannot be empty.");
        } else if (name == null || name.trim().length() == 0) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Name cannot be empty.");
        }

        if (instructorRepository.findInstructorByEmail(email) != null || ownerRepository.findOwnerByEmail(email) != null
                || customerRepository.findCustomerByEmail(email) != null) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "An account with this email already exists.");
        }

        Instructor instructor = new Instructor();
        instructor.setEmail(email);
        instructor.setPassword(password);
        instructor.setName(name);
        instructor.setCreationDate(Date.valueOf(LocalDate.now()));
        instructorRepository.save(instructor);

        return instructor;
    }

    @Transactional
    public Instructor getInstructorById(int id) {
        Instructor instructor = instructorRepository.findInstructorByAccountId(id);
        if (instructor == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Instructor not found.");
        }
        return instructor;
    }

    @Transactional
    public List<Instructor> getAllInstructors() {
        return ServiceUtils.toList(instructorRepository.findAll());
    }

    @Transactional
    public Instructor updateInstructor(int accountId, String email, String password, String name) {
        Instructor instructor = instructorRepository.findInstructorByAccountId(accountId);
        if (instructor == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Instructor not found.");
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

        instructorRepository.save(instructor);
        return instructor;
    }

    @Transactional
    public void deleteInstructor(int accountId) {
        Instructor instructor = instructorRepository.findInstructorByAccountId(accountId);
        if (instructor == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Instructor not found.");
        }
        instructorRepository.delete(instructor);
    }

    @Transactional
    public void deleteAllInstructors() {
        instructorRepository.deleteAll();
    }
}