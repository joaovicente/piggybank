package io.github.joaovicente.piggybank.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Data
public class KidDto {
    private String id;
    @NotNull
    @NotBlank
    private String name;
}
