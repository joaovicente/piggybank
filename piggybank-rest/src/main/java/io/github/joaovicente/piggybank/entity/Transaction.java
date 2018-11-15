package io.github.joaovicente.piggybank.entity;

import io.github.joaovicente.piggybank.type.TransactionKind;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    private final String id = UUID.randomUUID().toString();
    private String kidId;
    private Date date;
    private TransactionKind kind;
    private String description;
    private int amount;
}
