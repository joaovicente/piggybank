package io.github.joaovicente.piggybank.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.joaovicente.piggybank.type.TransactionKind;
import io.github.joaovicente.piggybank.validator.Enum;
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
    @NotNull(message="must not be null")
    @NotEmpty(message="must not be empty")
    private String kidId;
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
    @NotNull(message="must not be null")
    private Date date;
    @NotNull(message="must not be null")
    private TransactionKind kind;
    @NotNull(message="must not be null")
    private String description;
    @Min(value=1, message="must be above 0")
    private int amount;
}
