package ca.mcgill.ecse321.scs.controller;

import ca.mcgill.ecse321.scs.dto.ErrorDto;
import ca.mcgill.ecse321.scs.dto.OpeningHoursDto;
import ca.mcgill.ecse321.scs.exception.SCSException;
import ca.mcgill.ecse321.scs.model.OpeningHours;
import ca.mcgill.ecse321.scs.service.OpeningHoursService;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content; 

@RestController
@Tag(name = "OpeningHours", description = "Endpoints for managing opening hours.")
public class OpeningHoursController {
    @Autowired
    private OpeningHoursService openingHoursService;

    /**
     * create opening hours
     * @param openingHoursDto
     * @return the created opening hours
     */
    @PostMapping(value = { "/schedules/{year}/openingHours", "/schedules/{year}/openingHours/" })
    @Operation(summary = "Create opening hours", description = "Creates the opening hours for the specified day of week for that year.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Successful creation of opening hours",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = OpeningHoursDto.class))), // Fix the content attribute
    @ApiResponse(responseCode = "400", description = "Invalid input for opening hours",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class)))
    })
    @ResponseStatus(HttpStatus.CREATED)
    public OpeningHoursDto createOpeningHours(@PathVariable int year, @RequestBody OpeningHoursDto openingHoursDto) {
        String dayOfWeek;

        try {
            dayOfWeek = openingHoursDto.getDayOfWeek().toString();
        } catch (Exception e) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Invalid day of week.");
        }

        OpeningHoursDto createdOpeningHours = new OpeningHoursDto(openingHoursService.createOpeningHours(
            dayOfWeek,
            openingHoursDto.getOpenTime(),
            openingHoursDto.getCloseTime(),
            openingHoursDto.getYear()
        ));

        return createdOpeningHours;
    }

    /**
     * get opening hours by day
     * @param day
     * @return  the found opening hours
     */
    @GetMapping(value = {"/schedules/{year}/openingHours/{day}", "/schedules/{year}/openingHours/{day}/"})
    @Operation(summary = "Get opening hours by day", description = "Retrieves the opening hours for the specified day")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of opening hours")
    @ApiResponse(responseCode = "404", description = "Opening hours not found for the specified day",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ResponseStatus(HttpStatus.OK)
    public OpeningHoursDto getOpeningHoursByDay(@PathVariable int year, @PathVariable String day) {
        OpeningHours openingHours = openingHoursService.getOpeningHoursByDay(day, year);
        OpeningHoursDto openingHoursDto = new OpeningHoursDto(openingHours);
        return openingHoursDto;
    }

    /**
     * get all opening hours for a year
     * @param year
     * @return all opening hours for the year
     */
    @GetMapping(value = {"/schedules/{year}/openingHours", "/schedules/{year}/openingHours/"})
    @Operation(summary = "Get all opening hours for a year", description = "Retrieves all opening hours for the specified year")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of all opening hours")
    @ApiResponse(responseCode = "404", description = "Opening hours not found for the specified year",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ResponseStatus(HttpStatus.OK)
    public Iterable<OpeningHoursDto> getAllOpeningHours(@PathVariable int year) {
        List<OpeningHoursDto> openingHoursDtoList = new ArrayList<OpeningHoursDto>();
       
        for (OpeningHours openingHours : openingHoursService.getAllOpeningHours(year)) {
            openingHoursDtoList.add(new OpeningHoursDto(openingHours));
        }

        return openingHoursDtoList;
    }

    /**
     * update opening hours
     * @param openingHoursDto
     * @return the updated opening hours
     */
    @PutMapping(value = {"/schedules/{year}/openingHours/{day}", "/schedules/{year}/openingHours/{day}/"})
    @Operation(summary = "Update opening hours", description = "Updates the opening hours for the specified day")
    @ApiResponse(responseCode = "200", description = "Successful update of opening hours")
    @ApiResponse(responseCode = "400", description = "Invalid input for updating opening hours",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ApiResponse(responseCode = "404", description = "Opening hours not found for the specified day",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDto.class))) 
    @ResponseStatus(HttpStatus.OK)
    public OpeningHoursDto updateOpeningHours(@PathVariable int year, @RequestBody OpeningHoursDto openingHoursDto, @PathVariable String day) {
        OpeningHoursDto updatedOpeningHours = new OpeningHoursDto(openingHoursService.updateOpeningHours(
            openingHoursDto.getOpenTime(),
            openingHoursDto.getCloseTime(),
            openingHoursDto.getYear(),
            day
        ));
        return updatedOpeningHours;
    }

    /**
     * delete opening hours by day
     * @param day
     */
    @DeleteMapping(value = {"/schedules/{year}/openingHours/{day}", "/schedules/{year}/openingHours/{day}/"})
    @Operation(summary = "Delete opening hours by day", description = "Deletes the opening hours for the specified day")
    @ApiResponse(responseCode = "204", description = "Successful deletion of opening hours")
    @ApiResponse(responseCode = "404", description = "Opening hours not found for the specified day",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOpeningHours(@PathVariable int year, @PathVariable String day) {
        openingHoursService.deleteOpeningHours(day, year);
    }
    
    /**
     * delete all opening hours
     */
    @DeleteMapping(value = {"/schedules/{year}/openingHours", "/schedules/{year}/openingHours/"})
    @Operation(summary = "Delete all opening hours", description = "Deletes all opening hours")
    @ApiResponse(responseCode = "204", description = "Successful deletion of all opening hours")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllOpeningHours(@PathVariable int year) {
        openingHoursService.deleteAllOpeningHours(year);
    }
}