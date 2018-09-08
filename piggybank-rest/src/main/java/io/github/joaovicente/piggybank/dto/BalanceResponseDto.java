package io.github.joaovicente.piggybank.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data

public class BalanceResponseDto {
    private int balance;
}
