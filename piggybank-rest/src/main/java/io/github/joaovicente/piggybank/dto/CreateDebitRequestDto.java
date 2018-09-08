package io.github.joaovicente.piggybank.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateDebitRequestDto {
    private String description;
    private int amount;
}

