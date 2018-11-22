package io.github.joaovicente.piggybank.controller;

import io.github.joaovicente.piggybank.dto.ErrorDto;
import org.springframework.http.HttpStatus;

public class RestResponseException extends RuntimeException {
    private final transient ErrorDto errorDto;
    private final HttpStatus status;

    RestResponseException(ErrorDto errorDto) {
        this.errorDto = errorDto;
        this.status = HttpStatus.NOT_FOUND;
    }

    public ErrorDto getErrorDto() {
        return this.errorDto;
    }

    public HttpStatus getStatus() {
        return this.status;
    }
}


