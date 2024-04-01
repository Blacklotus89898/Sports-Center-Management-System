package ca.mcgill.ecse321.scs.service;

import ca.mcgill.ecse321.scs.dao.ClassTypeRepository;
import ca.mcgill.ecse321.scs.model.ClassType;
import ca.mcgill.ecse321.scs.exception.SCSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The ClassTypeService class provides methods to manage class types in the system.
 * It allows creating, retrieving, updating, and deleting class types.
 */
@Service
public class ClassTypeService {

    @Autowired
    ClassTypeRepository classTypeRepository;

    /**
     * creates a new class type with the specified class name and description.
     */
    @Transactional
    public ClassType createClassType(String className, String description, boolean isApproved) {
        // Check if the input is valid
        if(className == null || className.trim().length() == 0) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Class name cannot be empty.");
        } else if(description == null || description.trim().length() == 0) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Description cannot be empty.");
        }
        
        // Check if the class type already exists
        ClassType existingClassType = classTypeRepository.findClassTypeByClassName(className);
        if (existingClassType != null) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Class type with name " + className + " already exists.");
        }
        
        ClassType classType = new ClassType();
        classType.setClassName(className);
        classType.setDescription(description);
        classType.setIsApproved(isApproved);
        return classTypeRepository.save(classType);
    }

    /**
     * Retrieves the ClassType with the specified class name.
     * 
     * @param className the name of the class type to retrieve
     * @return the ClassType object with the specified class name
     * @throws SCSException if the class type with the specified name does not exist
     */
    @Transactional
    public ClassType getClassType(String className) {
        ClassType classType = classTypeRepository.findClassTypeByClassName(className);
        if (classType == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Class type with name " + className + " does not exist.");
        }
        return classType;
    }

    /**
     * Retrieves all class types from the repository.
     * 
     * @return a list of all class types
     */
    @Transactional
    public List<ClassType> getAllClassTypes() {
        return ServiceUtils.toList(classTypeRepository.findAll());
    }

    /**
     * updates the description of the class type with the specified class name.
     */
    @Transactional
    public ClassType updateClassTypeDescription(String className, String description) {
        // Check if the input is valid
        if(className == null || className.trim().length() == 0) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Class name cannot be empty.");
        } else if(description == null || description.trim().length() == 0) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Description cannot be empty.");
        }
        // Check the class type existence
        ClassType existingClassType = classTypeRepository.findClassTypeByClassName(className);
        if(existingClassType == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Class type with name " + className + " is not found.");
        }
        
        existingClassType.setClassName(className);
        existingClassType.setDescription(description);
        return classTypeRepository.save(existingClassType);
    }

    /**
     * Deletes a class type with the given class name.
     * 
     * @param className the name of the class type to delete
     */
    @Transactional
    public void deleteClassType(String className) {
        ClassType classType = getClassType(className); // This method throws if not found
        classTypeRepository.delete(classType);
    }

    /**
     * Deletes all class types.
     * This method deletes all class types from the repository.
     */
    @Transactional
    public void deleteAllClassTypes() {
        classTypeRepository.deleteAll();
    }

    /**
     * updates the approved status of the class type with the specified class name.
     */
    @Transactional
    public ClassType changeClassTypeApprovedStatus(String classTypeName, boolean isApproved) {
        ClassType classType = classTypeRepository.findClassTypeByClassName(classTypeName);
        if(classType == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Class type with name " + classTypeName + " does not exist.");
        }

        classType.setIsApproved(isApproved);
        classTypeRepository.save(classType);

        return classType;
    }

    /**
     * Retrieves all approved class types from the repository.
     * 
     * @return a list of all approved class types
     */
    @Transactional
    public List<ClassType> getAllApprovedClassTypes() {
        return classTypeRepository.findAllApprovedClassTypes();
    }

    /**
     * Retrieves all not approved class types from the repository.
     * 
     * @return a list of all not approved class types
     */
    @Transactional
    public List<ClassType> getAllNotApprovedClassTypes() {
        return classTypeRepository.findAllNotApprovedClassTypes();
    }
}
