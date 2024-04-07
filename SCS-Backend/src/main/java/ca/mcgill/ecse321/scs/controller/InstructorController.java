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
import ca.mcgill.ecse321.scs.model.Instructor;
import ca.mcgill.ecse321.scs.service.InstructorService;
import ca.mcgill.ecse321.scs.dto.InstructorResponseDto;
import ca.mcgill.ecse321.scs.dto.InstructorRequestDto;
import ca.mcgill.ecse321.scs.dto.InstructorListDto;


@CrossOrigin(origins = "*")
@RestController
@Tag(name = "Instructor", description = "Endpoints for managing instructors.")
public class InstructorController {
    @Autowired
    private InstructorService instructorService;

    /**
     * Create a new instructor
     * 
     * @param instructorRequestDto
     */
    @PostMapping(value = { "/instructors", "/instructors/" })
    @Operation(summary = "Create a new instructor", description = "Create a new instructor.")
    @ApiResponse(responseCode = "201", description = "Instructor created", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = InstructorResponseDto.class)) })
    @ApiResponse(responseCode = "400", description = "Invalid input", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class)) })
    @ResponseStatus(HttpStatus.CREATED)
    public InstructorResponseDto createInstructor(@RequestBody InstructorRequestDto instructorRequestDto) {
        Instructor instructor = instructorService.createInstructor(instructorRequestDto.getEmail(),
                instructorRequestDto.getPassword(), instructorRequestDto.getName(), instructorRequestDto.getImage());
        return new InstructorResponseDto(instructor);
    }

    /**
     * Get an instructor by id
     * 
     * @param id
     */
    @GetMapping(value = { "/instructors/{id}", "/instructors/{id}/" })
    @Operation(summary = "Get an instructor by id", description = "Get an instructor by id.")
    @ApiResponse(responseCode = "200", description = "Instructor found", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = InstructorResponseDto.class)) })
    @ApiResponse(responseCode = "404", description = "Instructor not found", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class)) })
    public InstructorResponseDto getInstructorById(@PathVariable("id") int id) {
        Instructor instructor = instructorService.getInstructorById(id);
        return new InstructorResponseDto(instructor);
    }

    /**
     * Get all instructors
     */
    @GetMapping(value = { "/instructors", "/instructors/" })
    @Operation(summary = "Get all instructors", description = "Get all instructors.")
    @ApiResponse(responseCode = "200", description = "Instructors found", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = InstructorListDto.class)) })
    public InstructorListDto getAllInstructors() {
        List<Instructor> instructors = instructorService.getAllInstructors();
        List<InstructorResponseDto> instructorResponseDtos = new ArrayList<>();
        for (Instructor instructor : instructors) {
            instructorResponseDtos.add(new InstructorResponseDto(instructor));
        }
        return new InstructorListDto(instructorResponseDtos);
    }

    /**
     * Update an instructor
     * 
     * @param id
     * @param instructorRequestDto
     */
    @PutMapping(value = { "/instructors/{id}", "/instructors/{id}/" })
    @Operation(summary = "Update an instructor", description = "Update an instructor.")
    @ApiResponse(responseCode = "200", description = "Instructor updated", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = InstructorResponseDto.class)) })
    @ApiResponse(responseCode = "400", description = "Invalid input", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class)) })
    public InstructorResponseDto updateInstructor(@PathVariable("id") int id, @RequestBody InstructorRequestDto instructorRequestDto) {
        Instructor instructor = instructorService.updateInstructor(id, instructorRequestDto.getEmail(),
                instructorRequestDto.getPassword(), instructorRequestDto.getName(), instructorRequestDto.getImage());
        return new InstructorResponseDto(instructor);
    }

    /**
     * Delete an instructor
     * 
     * @param id
     */
    @DeleteMapping(value = { "/instructors/{id}", "/instructors/{id}/" })
    @Operation(summary = "Delete an instructor", description = "Delete an instructor.")
    @ApiResponse(responseCode = "204", description = "Instructor deleted")
    @ApiResponse(responseCode = "404", description = "Instructor not found", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class)) })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInstructor(@PathVariable("id") int id) {
        instructorService.deleteInstructor(id);
    }

    /**
     * Delete all instructors
     */
    @DeleteMapping(value = { "/instructors", "/instructors/" })
    @Operation(summary = "Delete all instructors", description = "Delete all instructors.")
    @ApiResponse(responseCode = "204", description = "Instructors deleted")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllInstructors() {
        instructorService.deleteAllInstructors();
    }
}
