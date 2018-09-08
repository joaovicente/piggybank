package io.github.joaovicente.piggybank;

import lombok.Builder;
import lombok.Data;

@Builder
@Data

public class BalanceResponseDto {
    private int balance;
}
