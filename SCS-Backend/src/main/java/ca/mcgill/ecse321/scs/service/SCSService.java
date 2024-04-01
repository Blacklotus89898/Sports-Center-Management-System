package ca.mcgill.ecse321.scs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.scs.dao.CustomerRepository;
import ca.mcgill.ecse321.scs.dao.InstructorRepository;
import ca.mcgill.ecse321.scs.dao.OwnerRepository;
import ca.mcgill.ecse321.scs.exception.SCSException;
import ca.mcgill.ecse321.scs.model.Account;

/**
 * The SCSService class provides methods for logging in.
 * It interacts with the CustomerRepository, InstructorRepository, and OwnerRepository
 * to perform login operations.
 */
@Service
public class SCSService {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    InstructorRepository instructorRepository;

    @Autowired
    OwnerRepository ownerRepository;

    /**
     * Login with the specified email and password.
     * @param email
     * @param password
     * @return
     */
    @Transactional
    public Account login(String email, String password) {
        Account account = null;

        // input validation
        if (email == null || email.trim().length() == 0) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Email cannot be empty.");
        } else if (password == null || password.trim().length() == 0) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Password cannot be empty.");
        }
        
        // find the account
        account = customerRepository.findCustomerByEmailAndPassword(email, password);
        if (account == null) {
            // if the account is not found in the customer repository, then search in the instructor repository
            account = instructorRepository.findInstructorByEmailAndPassword(email, password);
        } 
        if (account == null) {
            // if the account is not found in the instructor repository, then search in the owner repository
            account = ownerRepository.findOwnerByEmailAndPassword(email, password);
        }

        // if the account is not found in any of the repositories, then throw an exception
        if (account == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Please verify that your email and password is correct.");
        }

        return account;
    }
}
