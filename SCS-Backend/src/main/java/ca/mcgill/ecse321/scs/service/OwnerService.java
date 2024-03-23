package ca.mcgill.ecse321.scs.service;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.scs.dao.CustomerRepository;
import ca.mcgill.ecse321.scs.dao.InstructorRepository;
import ca.mcgill.ecse321.scs.dao.OwnerRepository;
import ca.mcgill.ecse321.scs.exception.SCSException;
import ca.mcgill.ecse321.scs.model.Owner;

import java.util.regex.Pattern; 

@Service
public class OwnerService {
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
    public Owner createOwner(String email, String password, String name) {
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

        Owner owner = new Owner();
        owner.setEmail(email);
        owner.setPassword(password);
        owner.setName(name);
        owner.setCreationDate(Date.valueOf(LocalDate.now()));

        return ownerRepository.save(owner);
    }

    @Transactional
    public Owner getOwnerById(int id) {
        Owner owner = ownerRepository.findOwnerByAccountId(id);
        if (owner == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Owner not found.");
        }
        return owner;
    }

    @Transactional
    public Owner updateOwner(int accountId, String email, String password, String name) {
        Owner owner = ownerRepository.findOwnerByAccountId(accountId);
        if (owner == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Owner not found.");
        }

        // email in use by another account
        Owner ownerByEmail = ownerRepository.findOwnerByEmail(email);
        if ((ownerByEmail != null && ownerByEmail.getAccountId() != accountId) || instructorRepository.findInstructorByEmail(email) != null
                || customerRepository.findCustomerByEmail(email) != null) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "An account with this email already exists.");
        }

        if (email != null && email.trim().length() > 0) {
            if (!Pattern.compile(emailRegex).matcher(email).matches()) {
                throw new SCSException(HttpStatus.BAD_REQUEST, "Email is not valid.");
            }
            owner.setEmail(email);
        }

        if (password != null && password.trim().length() > 0) {
            owner.setPassword(password);
        } else {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Password cannot be empty.");
        }

        if (name != null && name.trim().length() > 0) {
            owner.setName(name);
        } else {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Name cannot be empty.");
        }

        ownerRepository.save(owner);
        return owner;
    }

    @Transactional
    public void deleteOwner(int accountId) {
        Owner owner = ownerRepository.findOwnerByAccountId(accountId);
        if (owner == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Owner not found.");
        }
        ownerRepository.delete(owner);
    }
}