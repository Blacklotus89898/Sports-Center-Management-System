package ca.mcgill.ecse321.scs.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.scs.dto.ErrorDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import ca.mcgill.ecse321.scs.dto.AccountRequestDto;
import ca.mcgill.ecse321.scs.dto.AccountResponseDto;
import ca.mcgill.ecse321.scs.service.SCSService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@CrossOrigin(origins = "*")
@RestController
@Tag(name = "SCS", description = "Endpoints for global SCS interactions.")
public class SCSController {
    @Autowired
    private SCSService scsService;
    
    /**
     * login using email and password
     * 
     * @param accountRequestDto
     * @return the account if found
     */
    @PostMapping(value = { "/login", "/login/" })
    @Operation(summary = "Login", description = "Logs in the user with the specified email and password")
    @ApiResponse(responseCode = "200", description = "Successful login",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = AccountResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid email or password", 
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ApiResponse(responseCode = "404", description = "Account not found", 
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ResponseStatus(HttpStatus.OK)
    public AccountResponseDto login(@RequestBody AccountRequestDto accountRequestDto) {
        return new AccountResponseDto(scsService.login(accountRequestDto.getEmail(), accountRequestDto.getPassword()));
    }
}