package io.github.joaovicente.piggybank.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data

public class TransactionResponseDto {
    private String id;
    private String kind;
    private String description;
    private int amount;
}
