package io.github.joaovicente.piggybank.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Data
@Builder
public class CreateCreditResponseDto {
    private String id;
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
    private Date date;
    private String description;
    private int amount;

}

