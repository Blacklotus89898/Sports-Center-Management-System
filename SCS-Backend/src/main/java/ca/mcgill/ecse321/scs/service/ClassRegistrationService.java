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

    @Transactional
    public ClassRegistration createClassRegistration(int accountId, int specificClassId, Integer registrationId) {
        Customer customer = customerService.getCustomerById(accountId);
        SpecificClass specificClass = specificClassService.getSpecificClass(specificClassId);

        if (specificClass.getCurrentCapacity() == specificClass.getMaxCapacity()) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Class with id " + specificClassId + " is full.");
        }

        if (classRegistrationRepository.findClassRegistrationByRegistrationId(registrationId) != null){
            throw new SCSException(HttpStatus.BAD_REQUEST, "Registration info with id " + registrationId + " already exists.");
        }

        //check that another classRegistration for a class doesn't already exist
        for (ClassRegistration classRegistration : classRegistrationRepository.findAll()) {
            if (classRegistration.getSpecificClass().getClassId() == specificClassId) {
                throw new SCSException(HttpStatus.BAD_REQUEST, "Registration info for class with id " + specificClassId + " already exists.");
            }
        }

        ClassRegistration classRegistration = new ClassRegistration(registrationId, customer, specificClass);
        specificClass.setCurrentCapacity(specificClass.getCurrentCapacity() + 1);
        classRegistrationRepository.save(classRegistration);
        return classRegistration;
    }

    @Transactional
    public ClassRegistration getClassRegistration(Integer classRegistrationId) {
        ClassRegistration classRegistration = classRegistrationRepository.findClassRegistrationByRegistrationId(classRegistrationId);
        if (classRegistration == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Registration info with id " + classRegistrationId + " not found.");
        }
        return classRegistration;
    }

    @Transactional
    public List<ClassRegistration> getAllClassRegistrations() {
        return ServiceUtils.toList(classRegistrationRepository.findAll());
    }

    @Transactional
    public ClassRegistration updateClassRegistration(Integer classRegistrationId, int accountId, int specificClassId) {
        ClassRegistration classRegistration = classRegistrationRepository.findClassRegistrationByRegistrationId(classRegistrationId);

        Customer customer = customerService.getCustomerById(accountId);
        SpecificClass currentSpecificClass = classRegistration.getSpecificClass();
        SpecificClass specificClass = specificClassService.getSpecificClass(specificClassId);


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

    @Transactional
    public void deleteClassRegistration(Integer registrationId) {
        ClassRegistration classRegistration = classRegistrationRepository.findClassRegistrationByRegistrationId(registrationId);
        if (classRegistration == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Class registration with id " + registrationId + " not found.");
        } 
        SpecificClass specificClass = classRegistration.getSpecificClass();
               
        classRegistrationRepository.delete(classRegistration);
        int currentCapacity = specificClass.getCurrentCapacity();
        specificClass.setCurrentCapacity(specificClass.getCurrentCapacity() > 0 ? currentCapacity - 1 : currentCapacity);
    }

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
        public ClassRegistration getClassRegistrationByClassId(int classId) {
            return classRegistrationRepository.findClassRegistrationByClassId(classId);
        }
}