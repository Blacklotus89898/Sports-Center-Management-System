package ca.mcgill.ecse321.scs.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.scs.dto.ErrorDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import ca.mcgill.ecse321.scs.dto.SpecificClassRequestDto;
import ca.mcgill.ecse321.scs.dto.SpecificClassResponseDto;
import ca.mcgill.ecse321.scs.dto.SpecificClassListDto;
import ca.mcgill.ecse321.scs.service.SpecificClassService;
import ca.mcgill.ecse321.scs.model.SpecificClass;

@CrossOrigin(origins = "*")
@RestController
@Tag(name = "SpecificClass", description = "Endpoints for managing specific classes.")
public class SpecificClassController {
    @Autowired
    private SpecificClassService specificClassService;

    /**
     * create specific class
     * @param specificClassRequestDto
     * @return the created specific class
     */
    @PostMapping(value = { "/specificClass", "/specificClass/" })
    @Operation(summary = "Create specific class", description = "Creates a new specific class")
    @ApiResponse(responseCode = "201", description = "Successful creation of specific class")
    @ApiResponse(responseCode = "400", description = "Invalid input for specific class creation",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ResponseStatus(HttpStatus.CREATED)
    public SpecificClassResponseDto createSpecificClass(@RequestBody SpecificClassRequestDto specificClassRequestDto) {
        SpecificClass specificClass = specificClassService.createSpecificClass(
            specificClassRequestDto.getClassType(), 
            specificClassRequestDto.getYear(), 
            specificClassRequestDto.getSpecificClassName(), specificClassRequestDto.getDescription(), 
            specificClassRequestDto.getDate(), 
            specificClassRequestDto.getStartTime(), 
            specificClassRequestDto.getHourDuration(), 
            specificClassRequestDto.getMaxCapacity(), 
            specificClassRequestDto.getCurrentCapacity(), 
            specificClassRequestDto.getRegistrationFee(),
            specificClassRequestDto.getImage());
        return new SpecificClassResponseDto(specificClass);
    }

    /**
     * get specific class by id
     * @param classId
     * @return the found specific class
     */
    @GetMapping(value = { "/specificClass/{classId}", "/specificClass/{classId}/" })
    @Operation(summary = "Get specific class by id", description = "Retrieves the specific class for the specified id")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of specific class")
    @ApiResponse(responseCode = "404", description = "Specific class not found for the specified id",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    public SpecificClassResponseDto getSpecificClassById(@PathVariable int classId) {
        return new SpecificClassResponseDto(specificClassService.getSpecificClass(classId));
    }

    /**
     * update a specific class from it's id
     * @param classId
     * @return the found specific classes
     */
    @PutMapping(value = { "/specificClass/{classId}", "/specificClass/{classId}/" })
    @Operation(summary = "Update specific class by id", description = "Updates the specific class for the specified id")
    @ApiResponse(responseCode = "200", description = "Successful update of specific class")
    @ApiResponse(responseCode = "404", description = "Specific class not found for the specified id",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input for updating specific class",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    public SpecificClassResponseDto updateSpecificClassById(@PathVariable int classId, @RequestBody SpecificClassRequestDto specificClassRequestDto) {
        SpecificClass specificClass = specificClassService.updateSpecificClass(
            classId, 
            specificClassRequestDto.getClassType(), 
            specificClassRequestDto.getYear(), 
            specificClassRequestDto.getSpecificClassName(), 
            specificClassRequestDto.getDescription(), 
            specificClassRequestDto.getDate(), 
            specificClassRequestDto.getStartTime(), 
            specificClassRequestDto.getHourDuration(), 
            specificClassRequestDto.getMaxCapacity(), 
            specificClassRequestDto.getCurrentCapacity(), 
            specificClassRequestDto.getRegistrationFee(),
            specificClassRequestDto.getImage());
        return new SpecificClassResponseDto(specificClass);
    }

    /**
     * delete specific class by id
     * @param classId
     */
    @DeleteMapping(value = { "/specificClass/{classId}", "/specificClass/{classId}/" })
    @Operation(summary = "Delete specific class by id", description = "Deletes the specific class for the specified id")
    @ApiResponse(responseCode = "204", description = "Successful deletion of specific class")
    @ApiResponse(responseCode = "404", description = "Specific class not found for the specified id",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSpecificClassById(@PathVariable int classId) {
        specificClassService.deleteSpecificClass(classId);
    }

    /**
     * get all specific classes by year
     * @param year
     * @return the found specific classes
     */
    @GetMapping(value = { "/specificClass/year/{year}", "/specificClass/year/{year}/" })
    @Operation(summary = "Get all specific classes by year", description = "Retrieves all specific classes for the specified year")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of specific classes")
    public SpecificClassListDto getAllSpecificClassesByYear(@PathVariable int year) {
        List<SpecificClass> specificClasses = specificClassService.getAllSpecificClasses(year);
        
        List<SpecificClassResponseDto> specificClassResponseDtos = new ArrayList<>();

        for (SpecificClass specificClass : specificClasses) {
            specificClassResponseDtos.add(new SpecificClassResponseDto(specificClass));
        }

        return new SpecificClassListDto(specificClassResponseDtos);
    }

    /**
     * delete all specific classes
     */
    @DeleteMapping(value = { "/specificClass", "/specificClass/" })
    @Operation(summary = "Delete all specific classes", description = "Deletes all specific classes")
    @ApiResponse(responseCode = "204", description = "Successful deletion of specific classes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllSpecificClasses() {
        specificClassService.deleteAllSpecificClasses();
    }

    /**
     * delete all specific classes by year
     * @param year
     */
    @DeleteMapping(value = { "/specificClass/year/{year}", "/specificClass/year/{year}/" })
    @Operation(summary = "Delete all specific classes by year", description = "Deletes all specific classes for the specified year")
    @ApiResponse(responseCode = "204", description = "Successful deletion of specific classes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllSpecificClassesByYear(@PathVariable int year) {
        specificClassService.deleteAllSpecificClassesByYear(year);
    }
}