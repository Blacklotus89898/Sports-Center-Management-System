package ca.mcgill.ecse321.scs.controller;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

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

import ca.mcgill.ecse321.scs.model.CustomHours;
import ca.mcgill.ecse321.scs.service.CustomHoursService;
import ca.mcgill.ecse321.scs.dto.ErrorDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import ca.mcgill.ecse321.scs.dto.CustomHoursRequestDto;
import ca.mcgill.ecse321.scs.dto.CustomHoursResponseDto;
import ca.mcgill.ecse321.scs.dto.CustomHoursListDto;

@CrossOrigin(origins = "*")
@RestController
@Tag(name = "CustomHours", description = "Endpoints for managing custom hours.")
public class CustomHoursController {
    @Autowired
    private CustomHoursService customHoursService;

    /**
     * get custom hours by date
     * 
     * @param date
     * @return the found custom hour
     */
    @GetMapping(value = { "/customHours/date/{date}", "/customHours/date/{date}/" })
    @Operation(summary = "Get custom hours by date", description = "Retrieves the custom hours for the specified date")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of custom hours")
    @ApiResponse(responseCode = "404", description = "Custom hours not found for the specified date",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    public CustomHoursResponseDto getCustomHoursByDate(@PathVariable LocalDate date) {
        // there can only be 1 custom hours for a given date

        return new CustomHoursResponseDto(customHoursService.getCustomHoursByDate(date));
    }
    
    /**
     * create custom hours
     * 
     * @param customHoursDto
     * @return the created custom hours
     */
    @PostMapping(value = { "/customHours", "/customHours/" })
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create custom hours", description = "Creates new custom hours")
    @ApiResponse(responseCode = "201", description = "Successful creation of custom hours")
    @ApiResponse(responseCode = "400", description = "Invalid input for creating custom hours", 
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    public CustomHoursResponseDto createCustomHours(@RequestBody CustomHoursRequestDto customHoursDto) {
        String name = customHoursDto.getName();
        String description = customHoursDto.getDescription();
        LocalDate date = customHoursDto.getDate();
        LocalTime openTime = customHoursDto.getOpenTime();
        LocalTime closeTime = customHoursDto.getCloseTime();
        int year = customHoursDto.getSchedule().getYear();

        CustomHours customHours = customHoursService.createCustomHours(name, description, date, openTime, closeTime, year);
        return new CustomHoursResponseDto(customHours);
    }

    /**
     * update custom hours
     * 
     * @param name
     * @param customHoursDto
     * @return the updated custom hours
     */
    @PutMapping(value = { "/customHours/{name}", "/customHours/{name}/" })
    @Operation(summary = "Update custom hours", description = "Updates the custom hours with the specified name")
    @ApiResponse(responseCode = "200", description = "Successful update of custom hours")
    @ApiResponse(responseCode = "400", description = "Invalid input for updating custom hours",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ApiResponse(responseCode = "404", description = "Custom hours not found with the specified name",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    public CustomHoursResponseDto updateCustomHours(@PathVariable String name, @RequestBody CustomHoursRequestDto customHoursDto) {
        String description = customHoursDto.getDescription();
        LocalDate date = customHoursDto.getDate();
        LocalTime openTime = customHoursDto.getOpenTime();
        LocalTime closeTime = customHoursDto.getCloseTime();
        int year = customHoursDto.getSchedule().getYear();

        CustomHours customHours = customHoursService.updateCustomHours(name, description, date, openTime, closeTime, year);
        return new CustomHoursResponseDto(customHours);
    }

    /* ============================================================================================================== */
    /*                                      Custom Hours Endpoints per Schedule
    /*                                    Endpoints with a Dependency on Schedule
    /* ============================================================================================================== */

    /**
     * get all custom hours
     * 
     * @param year
     * @return list of all the custom hours
     */
    @GetMapping(value = { "/schedules/{year}/customHours", "/schedules/{year}/customHours/" })
    @Operation(summary = "Get all custom hours for a schedule", description = "Retrieves all custom hours for the specified schedule year")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of custom hours")
    public CustomHoursListDto getCustomHours(@PathVariable int year) {
        List<CustomHoursResponseDto> customHoursDtos = new ArrayList<>();
        for (CustomHours ch : customHoursService.getAllCustomHours(year)) {
            customHoursDtos.add(new CustomHoursResponseDto(ch));
        }

        return new CustomHoursListDto(customHoursDtos);
    }

    /**
     * get custom hours by name
     * 
     * @param name
     * @param year
     * 
     * @return the found custom hours
     */
    @GetMapping(value = { "/schedules/{year}/customHours/{name}", "/schedules/{year}/customHours/{name}/" })
    @Operation(summary = "Get custom hours by name for a schedule", description = "Retrieves the custom hours with the specified name for the specified schedule year")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of custom hours")
    @ApiResponse(responseCode = "404", description = "Custom hours not found with the specified name for the specified schedule year",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    public CustomHoursResponseDto getCustomHour(@PathVariable String name, @PathVariable int year) {
        return new CustomHoursResponseDto(customHoursService.getCustomHours(name, year));
    }

    /**
     * delete custom hours by name
     * 
     * @param name
     * @param year
     */
    @DeleteMapping(value = { "/schedules/{year}/customHours/{name}", "/schedules/{year}/customHours/{name}/" })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete custom hours by name for a schedule", description = "Deletes the custom hours with the specified name for the specified schedule year")
    @ApiResponse(responseCode = "204", description = "Successful deletion of custom hours")
    @ApiResponse(responseCode = "404", description = "Custom hours not found with the specified name for the specified schedule year",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    public void deleteCustomHours(@PathVariable String name, @PathVariable int year) {
        customHoursService.deleteCustomHours(name, year);
    }

    /**
     * delete all custom hours
     * 
     * @param year
     */
    @DeleteMapping(value = { "/schedules/{year}/customHours", "/schedules/{year}/customHours/" })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete all custom hours for a schedule", description = "Deletes all custom hours for the specified schedule year")
    @ApiResponse(responseCode = "204", description = "Successful deletion of all custom hours")
    public void deleteAllCustomHours(@PathVariable int year) {
        customHoursService.deleteAllCustomHours(year);
    }
}
