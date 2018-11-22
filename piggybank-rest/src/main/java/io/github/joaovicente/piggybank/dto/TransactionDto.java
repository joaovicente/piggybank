package io.github.joaovicente.piggybank.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.joaovicente.piggybank.type.TransactionKind;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Builder
@Data

public class TransactionDto {
    String id;
    @NotNull
    @NotEmpty
    private String kidId;
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
    @NotNull
    private Date date;
    @NotNull
    private TransactionKind kind;
    @NotNull
    private String description;
    @Min(value=1)
    private int amount;
}
