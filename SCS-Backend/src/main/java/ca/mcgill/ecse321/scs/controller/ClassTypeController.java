package ca.mcgill.ecse321.scs.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import ca.mcgill.ecse321.scs.model.ClassType;
import ca.mcgill.ecse321.scs.service.ClassTypeService;
import ca.mcgill.ecse321.scs.dto.ClassTypeListDto;
import ca.mcgill.ecse321.scs.dto.ClassTypeRequestDto;
import ca.mcgill.ecse321.scs.dto.ClassTypeResponseDto;

@CrossOrigin(origins = "*")
@RestController
public class ClassTypeController {

    @Autowired
    private ClassTypeService classTypeService;
    /**
     * create a class type
     * @param classType
     * @return the created class type
     */
    @PostMapping(value = { "/classType", "/classType/" })
    @ResponseStatus(HttpStatus.CREATED)
    public ClassTypeResponseDto createClassType(@RequestBody ClassTypeRequestDto classType) {
        System.out.println("Creating class type for name: " + classType.getClassName());
        ClassType newClassType = classTypeService.createClassType(classType.getClassName(), classType.getDescription(), classType.getIsApproved());
        return new ClassTypeResponseDto(newClassType);
    }

    /**
     * get class type by name
     * @param className
     * @return the found class type
     */
    @GetMapping(value = { "/classType/{className}", "/classType/{className}/"})
    public ClassTypeResponseDto getClassType(@PathVariable String className) {
        ClassType classType = classTypeService.getClassType(className);
        return new ClassTypeResponseDto(classType);
    }

    /**
     * get all class types
     * @return all class types
     */
    @GetMapping(value = { "/classTypes", "/classTypes/" })
    public ClassTypeListDto getAllClassTypes() {
        List<ClassTypeResponseDto> classTypeDtos = new ArrayList<>();
        for (ClassType c : classTypeService.getAllClassTypes()) {
            classTypeDtos.add(new ClassTypeResponseDto(c));
        }

        return new ClassTypeListDto(classTypeDtos);
    }

    /**
     * delete class type by name
     * @param className
     */
    @DeleteMapping(value = { "/classType/{className}", "/classType/{className}/" })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClassType(@PathVariable String className) {
        classTypeService.deleteClassType(className);
    }

    /**
     * delete all class types
     */
    @DeleteMapping(value = { "/classTypes", "/classTypes/" })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllClassTypes() {
        classTypeService.deleteAllClassTypes();
    }
}
