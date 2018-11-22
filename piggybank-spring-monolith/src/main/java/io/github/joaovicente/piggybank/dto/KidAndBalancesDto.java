package io.github.joaovicente.piggybank.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KidAndBalancesDto {
    private String kidId;
    private String kidName;
    private int kidBalance;
}
