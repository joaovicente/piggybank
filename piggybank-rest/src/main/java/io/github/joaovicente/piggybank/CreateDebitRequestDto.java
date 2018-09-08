package io.github.joaovicente.piggybank;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateDebitRequestDto {
    private String description;
    private int amount;
}

