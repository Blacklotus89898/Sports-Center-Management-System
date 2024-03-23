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

import ca.mcgill.ecse321.scs.model.ClassRegistration;
import ca.mcgill.ecse321.scs.service.ClassRegistrationService;
import ca.mcgill.ecse321.scs.dto.ErrorDto;
import ca.mcgill.ecse321.scs.dto.ClassRegistrationListDto;
import ca.mcgill.ecse321.scs.dto.ClassRegistrationRequestDto;
import ca.mcgill.ecse321.scs.dto.ClassRegistrationResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@RestController
@Tag(name = "ClassRegistration", description = "ClassRegistration API endpoints")
public class ClassRegistrationController {
    @Autowired
    private ClassRegistrationService classRegistrationService;

    /**
     * create a classRegistration
     * 
     * @param classRegistrationRequestDto
     * @return the created classRegistration
     */
    @PostMapping(value = { "/classRegistration", "/classRegistration/" })
    @Operation(summary = "Create a classRegistration", description = "Creates a new classRegistration")
    @ApiResponse(responseCode = "201", description = "Successful creation of classRegistration")
    @ApiResponse(responseCode = "400", description = "Invalid input for classRegistration creation",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ResponseStatus(HttpStatus.CREATED)
    public ClassRegistrationResponseDto createClassRegistration(@RequestBody ClassRegistrationRequestDto classRegistrationRequestDto) {
        ClassRegistration classRegistration = classRegistrationService.createClassRegistration(
            classRegistrationRequestDto.getAccountId(),
            classRegistrationRequestDto.getClassId());
        return new ClassRegistrationResponseDto(classRegistration);
    }

    /**
     * get classRegistration by id
     * @param registrationId
     * @return the found classRegistration
     */
    @GetMapping(value = { "/classRegistration/{registrationId}", "/classRegistration/{registrationId}/" })
    @Operation(summary = "Get classRegistration by id", description = "Retrieves the classRegistration for the specified id")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of classRegistration")
    @ApiResponse(responseCode = "404", description = "ClassRegistration not found for the specified id",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    public ClassRegistrationResponseDto getClassRegistrationById(@PathVariable int registrationId) {
        return new ClassRegistrationResponseDto(classRegistrationService.getClassRegistration(registrationId));
    }

    /**
     * get classRegistration by specificClass classId
     * @param classId
     * @return the found classRegistration
     */
    @GetMapping(value = { "/specificClass/{classId}/classRegistration", "/specificClass/{classId}/classRegistration/" })
    @Operation(summary = "Get classRegistration by classId", description = "Retrieves the classRegistration for the specified classId")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of classRegistration")
    @ApiResponse(responseCode = "404", description = "ClassRegistration not found for the specified classId",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    public ClassRegistrationListDto getClassRegistrationByClassId(@PathVariable int classId) {
        List<ClassRegistration> classRegistrations = classRegistrationService.getClassRegistrationByClassId(classId);
        List<ClassRegistrationResponseDto> classRegistrationResponseDtos = new ArrayList<>();

        for (ClassRegistration classRegistration : classRegistrations) {
            classRegistrationResponseDtos.add(new ClassRegistrationResponseDto(classRegistration));
        }

        return new ClassRegistrationListDto(classRegistrationResponseDtos);
    }

    /**
     * update a classRegistration from its id
     * @param registrationId
     * @return the updated classRegistration
     */
    @PutMapping(value = { "/classRegistration/{registrationId}", "/classRegistration/{registrationId}/" })
    @Operation(summary = "Update classRegistration by id", description = "Updates the classRegistration for the specified id")
    @ApiResponse(responseCode = "200", description = "Successful update of classRegistration")
    @ApiResponse(responseCode = "404", description = "ClassRegistration not found for the specified id",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input for updating classRegistration",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    public ClassRegistrationResponseDto updateClassRegistrationById(@PathVariable int registrationId, @RequestBody ClassRegistrationRequestDto classRegistrationRequestDto) {
        ClassRegistration classRegistration = classRegistrationService.updateClassRegistration(
            registrationId,
            classRegistrationRequestDto.getAccountId(),
            classRegistrationRequestDto.getClassId());
        return new ClassRegistrationResponseDto(classRegistration);
    }

    /**
     * delete classRegistration by id
     * @param registrationId
     */
    @DeleteMapping(value = { "/classRegistration/{registrationId}", "/classRegistration/{registrationId}/" })
    @Operation(summary = "Delete classRegistration by id", description = "Deletes the classRegistration for the specified id")
    @ApiResponse(responseCode = "204", description = "Successful deletion of classRegistration")
    @ApiResponse(responseCode = "404", description = "ClassRegistration not found for the specified id",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClassRegistrationById(@PathVariable int registrationId) {
        classRegistrationService.deleteClassRegistration(registrationId);
    }

    /**
     * delete all classRegistrations
     */
    @DeleteMapping(value = { "/classRegistrations", "/classRegistrations/" })
    @Operation(summary = "Delete all classRegistrations", description = "Deletes all classRegistrations")
    @ApiResponse(responseCode = "204", description = "Successful deletion of all classRegistrations")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllClassRegistrations() {
        classRegistrationService.deleteAllClassRegistrations();
    }
}
