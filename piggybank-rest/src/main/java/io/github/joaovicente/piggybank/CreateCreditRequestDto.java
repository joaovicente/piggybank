package io.github.joaovicente.piggybank;

import lombok.Data;

@Data
public class CreateCreditRequestDto {
    private String description;
    private int amount;
}

