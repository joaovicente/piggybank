package io.github.joaovicente.piggybank.config;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.github.joaovicente.piggybank.dto.ErrorUtil.ErrorCode;
import io.github.joaovicente.piggybank.dto.ErrorUtil.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import io.github.joaovicente.piggybank.dto.ErrorDto;
import io.github.joaovicente.piggybank.controller.RestResponseException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@ControllerAdvice
class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RestResponseException.class )
    public ResponseEntity<ErrorDto> handleInputValidationException(RestResponseException ex) {
        return status(ex.getStatus()).body(ex.getErrorDto());
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + " " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + " " + error.getDefaultMessage());
        }

        ErrorDto errorDto = ErrorDto.builder()

                .error(ErrorCode.INVALID_INPUT.value())
                .message(errors)
                .build();

        return handleExceptionInternal(
                ex, errorDto, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        InvalidFormatException invalidFormatException = (InvalidFormatException) ex.getCause();
        final String field = invalidFormatException.getPath().get(0).getFieldName();
        ErrorDto errorDto = ErrorDto.builder()
                .error(ErrorCode.INVALID_INPUT.value())
                .message(Collections.singletonList(ErrorMessage.IS_INVALID.prefix(field)))
                .build();

        return handleExceptionInternal(
                ex, errorDto, headers, HttpStatus.BAD_REQUEST, request);
    }
}
