package ca.mcgill.ecse321.scs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.scs.dao.ClassRegistrationRepository;
import ca.mcgill.ecse321.scs.exception.SCSException;
import ca.mcgill.ecse321.scs.model.Customer;
import ca.mcgill.ecse321.scs.model.SpecificClass;
import ca.mcgill.ecse321.scs.model.ClassRegistration;

/**
 * The ClassRegistrationService class provides methods for managing class registrations.
 * It interacts with the ClassRegistrationRepository, CustomerService, and SpecificClassService
 * to perform CRUD operations on class registrations.
 */
@Service
public class ClassRegistrationService {
    @Autowired
    private ClassRegistrationRepository classRegistrationRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private SpecificClassService specificClassService;


    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public void setSpecificClassService(SpecificClassService specificClassService) {
        this.specificClassService = specificClassService;
    }

    
    /**
     * Represents a registration for a specific class by a customer.
     */
    @Transactional
    public ClassRegistration createClassRegistration(int accountId, int specificClassId) {
        // get the customer and specific class
        Customer customer = customerService.getCustomerById(accountId);
        SpecificClass specificClass = specificClassService.getSpecificClass(specificClassId);


        // check if the class is full
        if (specificClass.getCurrentCapacity() == specificClass.getMaxCapacity()) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Class with id " + specificClassId + " is full.");
        }

        //check if the users is already registered for the class
        for (ClassRegistration classRegistration : classRegistrationRepository.findAll()) {
            if (classRegistration.getCustomer().getAccountId() == accountId && classRegistration.getSpecificClass().getClassId() == specificClassId) {
                throw new SCSException(HttpStatus.BAD_REQUEST, "Customer with id " + accountId + " is already registered for class with id " + specificClassId + ".");
            }
        }

        // create the class registration
        ClassRegistration classRegistration = new ClassRegistration();
        classRegistration.setCustomer(customer);
        classRegistration.setSpecificClass(specificClass);
        specificClass.setCurrentCapacity(specificClass.getCurrentCapacity() + 1);
        
        return classRegistrationRepository.save(classRegistration);
    }

    /**
     * Retrieves the class registration with the specified ID.
     * 
     * @param classRegistrationId the ID of the class registration to retrieve
     * @return the class registration with the specified ID
     * @throws SCSException if the class registration with the specified ID is not found
     */
    @Transactional
    public ClassRegistration getClassRegistration(Integer classRegistrationId) {
        // get the class registration
        ClassRegistration classRegistration = classRegistrationRepository.findClassRegistrationByRegistrationId(classRegistrationId);
        if (classRegistration == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Registration info with id " + classRegistrationId + " not found.");
        }
        return classRegistration;
    }

    /**
     * Retrieves all class registrations from the database.
     *
     * @return a list of ClassRegistration objects representing all class registrations
     */
    @Transactional
    public List<ClassRegistration> getAllClassRegistrations() {
        // get all class registrations
        return ServiceUtils.toList(classRegistrationRepository.findAll());
    }

    /**
     * Represents a class registration in the system.
     * A class registration associates a customer with a specific class.
     */
    @Transactional
    public ClassRegistration updateClassRegistration(Integer classRegistrationId, int accountId, int specificClassId) {
        // get the class registration, customer, and specific class
        ClassRegistration classRegistration = classRegistrationRepository.findClassRegistrationByRegistrationId(classRegistrationId);
        if (classRegistration == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Class registration with id " + classRegistrationId + " not found.");
        }
        
        Customer customer = customerService.getCustomerById(accountId);
        SpecificClass currentSpecificClass = classRegistration.getSpecificClass();
        SpecificClass specificClass = specificClassService.getSpecificClass(specificClassId);

        // check if the class is full
        if (currentSpecificClass.getClassId() != specificClassId && specificClass.getCurrentCapacity() == specificClass.getMaxCapacity()) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Class with id " + specificClassId + " is full.");
        } else {
            // effect is nulled if the class is the same
            specificClass.setCurrentCapacity(specificClass.getCurrentCapacity() + 1);
            currentSpecificClass.setCurrentCapacity(currentSpecificClass.getCurrentCapacity() - 1);
        }

        classRegistration.setCustomer(customer);
        classRegistration.setSpecificClass(specificClass);
        classRegistrationRepository.save(classRegistration);
        return classRegistration;
    }

    /**
     * Deletes a class registration with the given registration ID.
     * 
     * @param registrationId the ID of the class registration to delete
     * @throws SCSException if the class registration with the given ID is not found
     */
    @Transactional
    public void deleteClassRegistration(Integer registrationId) {
        // get the class registration
        ClassRegistration classRegistration = classRegistrationRepository.findClassRegistrationByRegistrationId(registrationId);
        if (classRegistration == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Class registration with id " + registrationId + " not found.");
        } 
        SpecificClass specificClass = classRegistration.getSpecificClass();
               
        // delete the class registration
        classRegistrationRepository.delete(classRegistration);
        int currentCapacity = specificClass.getCurrentCapacity();

        // update the current capacity of the specific class
        specificClass.setCurrentCapacity(specificClass.getCurrentCapacity() > 0 ? currentCapacity - 1 : currentCapacity);
    }

    /**
     * Deletes all class registrations and resets the current capacity of the associated specific classes to 0.
     */
    @Transactional
    public void deleteAllClassRegistrations() {
        for (ClassRegistration classRegistration : classRegistrationRepository.findAll()) {
            SpecificClass specificClass = classRegistration.getSpecificClass();
            specificClass.setCurrentCapacity(0);
        }
        classRegistrationRepository.deleteAll();
    }

    //get the class registration for a specific class given the class id
    @Transactional
    public List<ClassRegistration> getClassRegistrationByClassId(int classId) {
        return classRegistrationRepository.findClassRegistrationsByClassId(classId);
    }
}