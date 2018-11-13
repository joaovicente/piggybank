package io.github.joaovicente.piggybank.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Builder
@Data

public class TransactionCreateDto {
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
    @NotNull(message="must not be null")
    private Date date;
    private TransactionKindDto kind;
    @NotNull(message="must not be null")
    private String description;
    @Min(value=1, message="must be above 0")
    private int amount;
}
