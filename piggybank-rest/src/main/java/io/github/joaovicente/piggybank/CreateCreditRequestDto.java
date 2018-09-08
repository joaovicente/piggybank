package io.github.joaovicente.piggybank;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateCreditRequestDto {
    private String description;
    private int amount;
}

