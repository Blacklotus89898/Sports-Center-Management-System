package ca.mcgill.ecse321.scs.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import ca.mcgill.ecse321.scs.model.Owner;
import ca.mcgill.ecse321.scs.service.OwnerService;
import ca.mcgill.ecse321.scs.dto.OwnerResponseDto;
import ca.mcgill.ecse321.scs.dto.OwnerRequestDto;

@CrossOrigin(origins = "*")
@RestController
@Tag(name = "Owner", description = "Endpoints for managing owner.")
public class OwnerController {
    @Autowired
    private OwnerService ownerService;

    /**
     * Create a new owner
     * 
     * @param ownerRequestDto
     */
    @PostMapping(value = { "/owner", "/owner/" })
    @Operation(summary = "Create a new owner", description = "Create a new owner.")
    @ApiResponse(responseCode = "201", description = "Owner created", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = OwnerResponseDto.class)) })
    @ApiResponse(responseCode = "400", description = "Invalid input", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class)) })
    @ResponseStatus(HttpStatus.CREATED)
    public OwnerResponseDto createOwner(@RequestBody OwnerRequestDto ownerRequestDto) {
        Owner owner = ownerService.createOwner(ownerRequestDto.getEmail(),
                ownerRequestDto.getPassword(), ownerRequestDto.getName(), ownerRequestDto.getImage());
        return new OwnerResponseDto(owner);
    }

    /**
     * Get an owner by id
     * 
     * @param id
     */
    @GetMapping(value = { "/owner/{id}", "/owner/{id}/" })
    @Operation(summary = "Get an owner by id", description = "Get an owner by id.")
    @ApiResponse(responseCode = "200", description = "Owner found", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = OwnerResponseDto.class)) })
    @ApiResponse(responseCode = "404", description = "Owner not found", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class)) })
    public OwnerResponseDto getOwnerById(@PathVariable("id") int id) {
        Owner owner = ownerService.getOwnerById(id);
        return new OwnerResponseDto(owner);
    }

    /**
     * Update an owner
     * 
     * @param id
     * @param ownerRequestDto
     */
    @PutMapping(value = { "/owner/{id}", "/owner/{id}/" })
    @Operation(summary = "Update an owner", description = "Update an owner.")
    @ApiResponse(responseCode = "200", description = "Owner updated", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = OwnerResponseDto.class)) })
    @ApiResponse(responseCode = "400", description = "Invalid input", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class)) })
    public OwnerResponseDto updateOwner(@PathVariable("id") int id, @RequestBody OwnerRequestDto ownerRequestDto) {
        Owner owner = ownerService.updateOwner(id, ownerRequestDto.getEmail(),
                ownerRequestDto.getPassword(), ownerRequestDto.getName(), ownerRequestDto.getImage());
        return new OwnerResponseDto(owner);
    }

    /**
     * Delete an owner
     * 
     * @param id
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = { "/owner/{id}", "/owner/{id}/" })
    @Operation(summary = "Delete an owner", description = "Delete an owner.")
    @ApiResponse(responseCode = "204", description = "Owner deleted")
    @ApiResponse(responseCode = "404", description = "Owner not found", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class)) })
    public void deleteOwner(@PathVariable("id") int id) {
        ownerService.deleteOwner(id);
    }
}
