package ca.mcgill.ecse321.scs.controller;

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

import ca.mcgill.ecse321.scs.model.TeachingInfo;
import ca.mcgill.ecse321.scs.service.TeachingInfoService;
import ca.mcgill.ecse321.scs.dto.ErrorDto;
import ca.mcgill.ecse321.scs.dto.TeachingInfoRequestDto;
import ca.mcgill.ecse321.scs.dto.TeachingInfoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@RestController
@Tag(name = "TeachingInfo", description = "The TeachingInfo API")
public class TeachingInfoController {
    @Autowired
    private TeachingInfoService teachingInfoService;

    /**
     * create a teachingInfo
     * 
     * @param teachingInfoRequestDto
     * @return the created teachingInfo
     */
    @PostMapping(value = { "/teachingInfo", "/teachingInfo/" })
    @Operation(summary = "Create a teachingInfo", description = "Creates a new teachingInfo")
    @ApiResponse(responseCode = "201", description = "Successful creation of teachingInfo")
    @ApiResponse(responseCode = "400", description = "Invalid input for teachingInfo creation",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ResponseStatus(HttpStatus.CREATED)
    public TeachingInfoResponseDto createTeachingInfo(@RequestBody TeachingInfoRequestDto teachingInfoRequestDto) {
        TeachingInfo teachingInfo = teachingInfoService.createTeachingInfo(
            teachingInfoRequestDto.getAccountId(),
            teachingInfoRequestDto.getClassId(),
            teachingInfoRequestDto.getTeachingInfoId());
        return new TeachingInfoResponseDto(teachingInfo);
    }

    /**
     * get teachingInfo by id
     * @param teachingInfoId
     * @return the found teachingInfo
     */
    @GetMapping(value = { "/teachingInfo/{teachingInfoId}", "/teachingInfo/{teachingInfoId}/" })
    @Operation(summary = "Get teachingInfo by id", description = "Retrieves the teachingInfo for the specified id")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of teachingInfo")
    @ApiResponse(responseCode = "404", description = "TeachingInfo not found for the specified id",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    public TeachingInfoResponseDto getTeachingInfoById(@PathVariable int teachingInfoId) {
        return new TeachingInfoResponseDto(teachingInfoService.getTeachingInfo(teachingInfoId));
    }

    /**
     * update a teachingInfo from its id
     * @param teachingInfoId
     * @return the updated teachingInfo
     */
    @PutMapping(value = { "/teachingInfo/{teachingInfoId}", "/teachingInfo/{teachingInfoId}/" })
    @Operation(summary = "Update teachingInfo by id", description = "Updates the teachingInfo for the specified id")
    @ApiResponse(responseCode = "200", description = "Successful update of teachingInfo")
    @ApiResponse(responseCode = "404", description = "TeachingInfo not found for the specified id",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input for updating teachingInfo",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    public TeachingInfoResponseDto updateTeachingInfoById(@PathVariable int teachingInfoId, @RequestBody TeachingInfoRequestDto teachingInfoRequestDto) {
        TeachingInfo teachingInfo = teachingInfoService.updateTeachingInfo(
            teachingInfoId,
            teachingInfoRequestDto.getAccountId(),
            teachingInfoRequestDto.getClassId());
        return new TeachingInfoResponseDto(teachingInfo);
    }

    /**
     * delete teachingInfo by id
     * @param teachingInfoId
     */
    @DeleteMapping(value = { "/teachingInfo/{teachingInfoId}", "/teachingInfo/{teachingInfoId}/" })
    @Operation(summary = "Delete teachingInfo by id", description = "Deletes the teachingInfo for the specified id")
    @ApiResponse(responseCode = "204", description = "Successful deletion of teachingInfo")
    @ApiResponse(responseCode = "404", description = "TeachingInfo not found for the specified id",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeachingInfoById(@PathVariable int teachingInfoId) {
        teachingInfoService.deleteTeachingInfo(teachingInfoId);
    }

    /**
     * delete all teachingInfos
     */
    @DeleteMapping(value = { "/teachingInfos", "/teachingInfos/" })
    @Operation(summary = "Delete all teachingInfos", description = "Deletes all teachingInfos")
    @ApiResponse(responseCode = "204", description = "Successful deletion of all teachingInfos")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllTeachingInfos() {
        teachingInfoService.deleteAllTeachingInfos();
    }
}
