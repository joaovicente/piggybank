package io.github.joaovicente.piggybank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class IdResponseDto {
    private String id;
}

