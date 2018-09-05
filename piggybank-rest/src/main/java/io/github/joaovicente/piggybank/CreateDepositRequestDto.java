package io.github.joaovicente.piggybank;

import lombok.Data;

@Data
public class CreateDepositRequestDto {
    private String description;
    private float amount;
}

