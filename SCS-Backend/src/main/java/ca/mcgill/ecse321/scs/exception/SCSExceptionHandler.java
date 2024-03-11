package ca.mcgill.ecse321.scs.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import ca.mcgill.ecse321.scs.dto.ErrorDto;

@ControllerAdvice
public class SCSExceptionHandler {
    @ExceptionHandler(SCSException.class)
    public ResponseEntity<ErrorDto> handleEventRegistrationException(SCSException e) {
        return new ResponseEntity<ErrorDto>(new ErrorDto(e.getMessage()), e.getStatus());
    }
}