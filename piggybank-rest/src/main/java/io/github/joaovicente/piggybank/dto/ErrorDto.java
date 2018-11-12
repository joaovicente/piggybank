package io.github.joaovicente.piggybank.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ErrorDto {
    String error;
    List<String> message;
}
