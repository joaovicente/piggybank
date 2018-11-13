package io.github.joaovicente.piggybank.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import io.github.joaovicente.piggybank.validator.Enum;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Builder
@Data

public class TransactionCreateDto {
    @NotNull(message="must not be null")
    @NotEmpty(message="must not be empty")
    String kidId;
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
    @NotNull(message="must not be null")
    private Date date;
//    @Enum(enumClass=TransactionKindDto.class, ignoreCase=true)
    @NotNull(message="must not be null")
    private TransactionKindDto kind;
    @NotNull(message="must not be null")
    private String description;
    @Min(value=1, message="must be above 0")
    private int amount;
}
