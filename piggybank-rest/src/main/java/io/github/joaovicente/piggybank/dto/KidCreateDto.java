package io.github.joaovicente.piggybank.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Data
public class KidCreateDto {
    @NotNull(message = "cannot be null")
    @NotBlank(message = "cannot be empty")
    String name;
}
