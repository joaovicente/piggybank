package io.github.joaovicente.piggybank.controller;

import io.github.joaovicente.piggybank.dto.ErrorDto;
import org.springframework.http.HttpStatus;

public class RestResponseException extends RuntimeException {
    private final ErrorDto errorDto;
    private final HttpStatus status;

    public RestResponseException(ErrorDto errorDto, HttpStatus status) {
        this.errorDto = errorDto;
        this.status = status;
    }

    public ErrorDto getErrorDto() {
        return this.errorDto;
    }

    public HttpStatus getStatus() {
        return this.status;
    }
}


