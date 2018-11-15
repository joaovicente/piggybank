package io.github.joaovicente.piggybank.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.joaovicente.piggybank.type.TransactionKind;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Builder
@Data

public class TransactionReadDto {
    String id;
    String kidId;
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
    private Date date;
    private TransactionKind kind;
    private String description;
    private int amount;
}
