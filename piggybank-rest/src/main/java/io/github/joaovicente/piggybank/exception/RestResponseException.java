package io.github.joaovicente.piggybank.exception;

import io.github.joaovicente.piggybank.dto.ErrorDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RestResponseException extends RuntimeException {
    private ErrorDto errorDto;
}


