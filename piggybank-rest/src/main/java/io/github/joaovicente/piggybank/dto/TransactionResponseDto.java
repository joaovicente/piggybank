package io.github.joaovicente.piggybank.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import java.util.Date;
import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Builder
@Data

public class TransactionResponseDto {
    private String id;
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
    private Date date;
    private String kind;
    private String description;
    private int amount;
}
