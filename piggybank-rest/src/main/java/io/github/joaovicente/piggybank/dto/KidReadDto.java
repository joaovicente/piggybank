package io.github.joaovicente.piggybank.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class KidReadDto {
    String id;
    String name;
}