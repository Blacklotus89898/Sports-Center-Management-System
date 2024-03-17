package ca.mcgill.ecse321.scs.service;

import ca.mcgill.ecse321.scs.dao.ClassTypeRepository;
import ca.mcgill.ecse321.scs.model.ClassType;
import ca.mcgill.ecse321.scs.exception.SCSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClassTypeService {

    @Autowired
    ClassTypeRepository classTypeRepository;

    @Transactional
    public ClassType createClassType(String className, String description, boolean isApproved) {
        if(className == null || className.trim().length() == 0) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Class name cannot be empty.");
        } else if(description == null || description.trim().length() == 0) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Description cannot be empty.");
        }
        // Check if the class type already exists
        ClassType existingClassType = classTypeRepository.findClassTypeByClassName(className);
        if(existingClassType != null) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Class type with name " + className + " already exists.");
        }
        
        ClassType classType = new ClassType();
        classType.setClassName(className);
        classType.setDescription(description);
        classType.setIsApproved(isApproved);
        return classTypeRepository.save(classType);
    }

    @Transactional
    public ClassType getClassType(String className) {
        ClassType classType = classTypeRepository.findClassTypeByClassName(className);
        if(classType == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Class type with name " + className + " does not exist.");
        }
        return classType;
    }

    @Transactional
    public List<ClassType> getAllClassTypes() {
        return ServiceUtils.toList(classTypeRepository.findAll());
    }

    @Transactional
    public ClassType updateClassType(String className, String description, boolean isApproved) {
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
        existingClassType.setIsApproved(isApproved);
        return classTypeRepository.save(existingClassType);
    }

    @Transactional
    public ClassType updateClassType(String className, boolean isApproved) {
        if(className == null || className.trim().length() == 0) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Class name cannot be empty.");
        }
        // Check the class type existence
        ClassType existingClassType = classTypeRepository.findClassTypeByClassName(className);
        if(existingClassType == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Class type with name " + className + " is not found.");
        }
        
        existingClassType.setClassName(className);
        existingClassType.setIsApproved(isApproved);
        return classTypeRepository.save(existingClassType);
    }

    @Transactional
    public void deleteClassType(String className) {
        ClassType classType = getClassType(className); // This method throws if not found
        classTypeRepository.delete(classType);
    }

    @Transactional
    public void deleteAllClassTypes() {
        classTypeRepository.deleteAll();
    }
}
