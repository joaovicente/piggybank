package io.github.joaovicente.piggybank.model;

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
    static public enum Kind {CREDIT, DEBIT}
    @Id
    private final String id = UUID.randomUUID().toString();
    private Date date;
    private Kind kind;
    private String description;
    private int amount;
}
