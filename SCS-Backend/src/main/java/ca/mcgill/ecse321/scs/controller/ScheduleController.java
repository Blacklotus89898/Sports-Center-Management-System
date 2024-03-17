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

import ca.mcgill.ecse321.scs.model.Schedule;
import ca.mcgill.ecse321.scs.service.ScheduleService;
import ca.mcgill.ecse321.scs.dto.ErrorDto;
import ca.mcgill.ecse321.scs.dto.ScheduleListDto;
import ca.mcgill.ecse321.scs.dto.ScheduleRequestDto;
import ca.mcgill.ecse321.scs.dto.ScheduleResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@RestController
@Tag(name = "Schedule")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    /**
     * get all schedules
     * 
     * @return list of all the schedules
     */
    @GetMapping(value = { "/schedules", "/schedules/" })
    @Operation(summary = "Get all schedules", description = "Retrieves a list of all schedules")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of schedules")
    public ScheduleListDto getSchedules() {
        List<ScheduleResponseDto> scheduleDtos = new ArrayList<>();
        for (Schedule s : scheduleService.getAllSchedules()) {
            scheduleDtos.add(new ScheduleResponseDto(s));
        }

        return new ScheduleListDto(scheduleDtos);
    }

    /**
     * get schedule by year
     * 
     * @param year
     * @return the found schedule
     */
    @GetMapping(value = { "/schedule/{year}", "/schedule/{year}/" })
    @Operation(summary = "Get schedule by year", description = "Retrieves a schedule for the specified year")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of schedule")
    @ApiResponse(responseCode = "404", description = "Schedule not found for the specified year",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    public ScheduleResponseDto getSchedule(@PathVariable int year) {
        return new ScheduleResponseDto(scheduleService.getSchedule(year));
    }

    /**
     * create a schedule
     * 
     * @param year
     * @return the created schedule, with the year
     */
    @PostMapping(value = { "/schedule", "/schedule/" })
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a schedule", description = "Creates a new schedule for the specified year")
    @ApiResponse(responseCode = "201", description = "Successful creation of schedule")
    @ApiResponse(responseCode = "400", description = "Invalid input for creating schedule",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto schedule) {
        System.out.println("Creating schedule for year: " + schedule.getYear());
        Schedule newSchedule = scheduleService.createSchedule(schedule.getYear());
        return new ScheduleResponseDto(newSchedule);
    }

    /**
     * delete a schedule by year
     * 
     * @param year
     */
    @DeleteMapping(value = { "/schedule/{year}", "/schedule/{year}/" })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a schedule", description = "Deletes a schedule for the specified year")
    @ApiResponse(responseCode = "204", description = "Successful deletion of schedule")
    @ApiResponse(responseCode = "404", description = "Schedule not found for the specified year",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    public void deleteSchedule(@PathVariable int year) {
        scheduleService.deleteSchedule(year);
    }

    /**
     * delete all schedules
     */
    @DeleteMapping(value = { "/schedules", "/schedules/" })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete all schedules", description = "Deletes all schedules")
    @ApiResponse(responseCode = "204", description = "Successful deletion of all schedules")
    public void deleteAllSchedules() {
        scheduleService.deleteAllSchedules();
    }
}
