package io.github.joaovicente.piggybank.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorDto {
    String error;
    String message;
}
