package ca.mcgill.ecse321.scs.controller;

import ca.mcgill.ecse321.scs.dto.ErrorDto;
import ca.mcgill.ecse321.scs.dto.OpeningHoursDto;
import ca.mcgill.ecse321.scs.model.OpeningHours;
import ca.mcgill.ecse321.scs.service.OpeningHoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalTime;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content; 

@RestController
@RequestMapping("/openingHours")
@Tag(name = "OpeningHours", description = "Endpoints for managing opening hours.")
public class OpeningHoursController {

    // @Autowired
    // private OpeningHoursService openingHoursService;

    // /**
    //  * create opening hours
    //  * @param openingHoursDto
    //  * @return the created opening hours
    //  */
    // @PostMapping
    // @Operation(summary = "Create opening hours", description = "Creates the opening hours for the specified day")
    // @ApiResponses(value = {
    // @ApiResponse(responseCode = "201", description = "Successful creation of opening hours",
    //                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = OpeningHoursDto.class))), // Fix the content attribute
    // @ApiResponse(responseCode = "400", description = "Invalid input for opening hours",
    //                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class)))
    // })
    // @ResponseStatus(HttpStatus.CREATED)
    // public OpeningHoursDto createOpeningHours(@RequestBody OpeningHoursDto openingHoursDto) {
    //     OpeningHoursDto createdOpeningHours = new OpeningHoursDto(openingHoursService.createOpeningHours(
    //     openingHoursDto.getDayOfWeek().toString(),
    //     openingHoursDto.getOpenTime(),
    //     openingHoursDto.getCloseTime(),
    //     openingHoursDto.getYear()
    //     ));

    //     return createdOpeningHours;
    // }

    // /**
    //  * get opening hours by day
    //  * @param day
    //  * @return  the found opening hours
    //  */
    // @GetMapping("/{day}")
    // @Operation(summary = "Get opening hours by day", description = "Retrieves the opening hours for the specified day")
    // @ApiResponse(responseCode = "200", description = "Successful retrieval of opening hours")
    // @ApiResponse(responseCode = "404", description = "Opening hours not found for the specified day",
    //              content = @Content(mediaType = "application/json",
    //              schema = @Schema(implementation = ErrorDto.class)))
    // @ResponseStatus(HttpStatus.OK)
    // public OpeningHoursDto getOpeningHoursByDay(@PathVariable String day) {
    //     OpeningHours openingHours = openingHoursService.getOpeningHoursByDay(day);
    //     OpeningHoursDto openingHoursDto = new OpeningHoursDto(openingHours);
    //     return openingHoursDto;
    // }

    // /**
    //  * update opening hours
    //  * @param openingHoursDto
    //  * @return the updated opening hours
    //  */
    // @PutMapping("/{day}")
    // @Operation(summary = "Update opening hours", description = "Updates the opening hours for the specified day")
    // @ApiResponse(responseCode = "200", description = "Successful update of opening hours")
    // @ApiResponse(responseCode = "400", description = "Invalid input for updating opening hours",
    //              content = @Content(mediaType = "application/json",
    //              schema = @Schema(implementation = ErrorDto.class)))
    // @ApiResponse(responseCode = "404", description = "Opening hours not found for the specified day",
    //                 content = @Content(mediaType = "application/json",
    //                 schema = @Schema(implementation = ErrorDto.class))) 
    // @ResponseStatus(HttpStatus.OK)
    // public OpeningHoursDto updateOpeningHours(@RequestBody OpeningHoursDto openingHoursDto, @PathVariable String day) {
    //     OpeningHoursDto updatedOpeningHours = new OpeningHoursDto(openingHoursService.updateOpeningHours(
    //         LocalTime.parse(openingHoursDto.getOpenTime().toString()),
    //         openingHoursDto.getCloseTime(),
    //         openingHoursDto.getYear(),
    //         day
    //     ));
    //     return updatedOpeningHours;
    // }

    // /**
    //  * delete opening hours by day
    //  * @param day
    //  */
    // @DeleteMapping("/{day}")
    // @Operation(summary = "Delete opening hours by day", description = "Deletes the opening hours for the specified day")
    // @ApiResponse(responseCode = "204", description = "Successful deletion of opening hours")
    // @ApiResponse(responseCode = "404", description = "Opening hours not found for the specified day",
    //              content = @Content(mediaType = "application/json",
    //              schema = @Schema(implementation = ErrorDto.class)))
    // @ResponseStatus(HttpStatus.NO_CONTENT)
    // public void deleteOpeningHours(@PathVariable String day) {
    //     openingHoursService.deleteOpeningHours(day);
    // }
    // /**
    //  * delete all opening hours
    //  */
    // @DeleteMapping
    // @Operation(summary = "Delete all opening hours", description = "Deletes all opening hours")
    // @ApiResponse(responseCode = "204", description = "Successful deletion of all opening hours")
    // @ResponseStatus(HttpStatus.NO_CONTENT)
    // public void deleteAllOpeningHours() {
    //     openingHoursService.deleteAllOpeningHours();
    // }
}