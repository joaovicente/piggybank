package io.github.joaovicente.piggybank.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import io.github.joaovicente.piggybank.dto.ErrorDto;
import io.github.joaovicente.piggybank.controller.RestResponseException;

import static org.springframework.http.ResponseEntity.status;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(RestResponseException.class )
    public ResponseEntity<ErrorDto> handleInputValidationException(RestResponseException ex) {
        return status(ex.getStatus()).body(ex.getErrorDto());
    }
}
