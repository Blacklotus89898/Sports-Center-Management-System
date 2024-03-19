package ca.mcgill.ecse321.scs.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import ca.mcgill.ecse321.scs.model.ClassType;
import ca.mcgill.ecse321.scs.service.ClassTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import ca.mcgill.ecse321.scs.dto.ClassTypeListDto;
import ca.mcgill.ecse321.scs.dto.ClassTypeRequestDto;
import ca.mcgill.ecse321.scs.dto.ClassTypeResponseDto;
import ca.mcgill.ecse321.scs.dto.ErrorDto;

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
    @Operation(summary = "Create a class type", description = "Creates a new class type")
    @ApiResponse(responseCode = "201", description = "Class type created")
    @ApiResponse(responseCode = "400", description = "Invalid input",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ResponseStatus(HttpStatus.CREATED)
    public ClassTypeResponseDto createClassType(@RequestBody ClassTypeRequestDto classType) {
        ClassType newClassType = classTypeService.createClassType(classType.getClassName(), classType.getDescription(), classType.getIsApproved());
        return new ClassTypeResponseDto(newClassType);
    }

    /**
     * get class type by name
     * @param className
     * @return the found class type
     */
    @GetMapping(value = { "/classType/{className}", "/classType/{className}/"})
    @Operation(summary = "Get class type by name", description = "Retrieves the class type with the specified name")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of class type")
    @ApiResponse(responseCode = "404", description = "Class type not found with the specified name",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    public ClassTypeResponseDto getClassType(@PathVariable String className) {
        ClassType classType = classTypeService.getClassType(className);
        return new ClassTypeResponseDto(classType);
    }

    /**
     * get all class types
     * @return all class types
     */
    @GetMapping(value = { "/classTypes", "/classTypes/" })
    @Operation(summary = "Get all class types", description = "Retrieves all class types")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of class types")
    public ClassTypeListDto getAllClassTypes() {
        List<ClassTypeResponseDto> classTypeDtos = new ArrayList<>();
        for (ClassType c : classTypeService.getAllClassTypes()) {
            classTypeDtos.add(new ClassTypeResponseDto(c));
        }

        return new ClassTypeListDto(classTypeDtos);
    }

    /**
     * update class type by name
     * @param className
     * @return the updated class type
     */
    @PutMapping(value = { "/classTypes/{className}", "/classTypes/{className}/" })
    @Operation(summary = "Update class type by name", description = "Updates the class type with the specified name")
    @ApiResponse(responseCode = "200", description = "Successful update of class type")
    @ApiResponse(responseCode = "400", description = "Invalid input for updating class type",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ApiResponse(responseCode = "404", description = "Class type not found with the specified name",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ResponseStatus(HttpStatus.OK)
    public ClassTypeResponseDto updateClassTypeDescription(@PathVariable String className, @RequestBody ClassTypeRequestDto classType) {
        ClassType updatedClassType = classTypeService.updateClassTypeDescription(className, classType.getDescription());
        return new ClassTypeResponseDto(updatedClassType);
    }

    /**
     * delete class type by name
     * @param className
     */
    @DeleteMapping(value = { "/classType/{className}", "/classType/{className}/" })
    @Operation(summary = "Delete class type by name", description = "Deletes the class type with the specified name")
    @ApiResponse(responseCode = "204", description = "Successful deletion of class type")
    @ApiResponse(responseCode = "404", description = "Class type not found with the specified name",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClassType(@PathVariable String className) {
        classTypeService.deleteClassType(className);
    }

    /**
     * delete all class types
     */
    @DeleteMapping(value = { "/classTypes", "/classTypes/" })
    @Operation(summary = "Delete all class types", description = "Deletes all class types")
    @ApiResponse(responseCode = "204", description = "Successful deletion of class types")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllClassTypes() {
        classTypeService.deleteAllClassTypes();
    }

    /**
     * get all class types of approved status {isApproved}
     * @param isApproved
     * @return all class types of approved status {isApproved}
     */
    @GetMapping(value = { "/classTypes/approved/{isApproved}", "/classTypes/approved/{isApproved}/" })
    @Operation(summary = "Get all class types by approved status", description = "Retrieves all class types by approved status (true/false)")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of class types")
    public ClassTypeListDto getAllClassTypesByIsApproved(@PathVariable boolean isApproved) {
        List<ClassTypeResponseDto> classTypeDtos = new ArrayList<>();

        List<ClassType> classTypes;
        if (isApproved) {
            classTypes = classTypeService.getAllApprovedClassTypes();
        } else {
            classTypes = classTypeService.getAllNotApprovedClassTypes();
        }

        for (ClassType c : classTypes) {
            classTypeDtos.add(new ClassTypeResponseDto(c));
        }

        return new ClassTypeListDto(classTypeDtos);
    }

    /**
     * change the approved status of a class type
     * @param className
     * @param isApproved
     * @return the updated class type
     */
    @PutMapping(value = { "/classTypes/{className}/approved/{isApproved}", "/classTypes/{className}/approved/{isApproved}/" })
    @Operation(summary = "Change the approved status of a class type", description = "Changes the approved status (true/false) of a class type")
    @ApiResponse(responseCode = "200", description = "Successful update of class type")
    @ApiResponse(responseCode = "400", description = "Invalid input for updating class type",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ApiResponse(responseCode = "404", description = "Class type not found with the specified name",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ResponseStatus(HttpStatus.OK)
    public ClassType changeClassTypeApprovedStatus(@PathVariable String className, @PathVariable boolean isApproved) {
        ClassType classType = classTypeService.changeClassTypeApprovedStatus(className, isApproved);

        return classType;
    }
}