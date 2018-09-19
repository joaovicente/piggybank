package io.github.joaovicente.piggybank.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Data
@Builder
public class CreateDebitResponseDto {
    private String id;
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
    private Date date;
    private String description;
    private int amount;
}

