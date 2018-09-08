package io.github.joaovicente.piggybank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    static public enum Kind {CREDIT, DEBIT}
    @Id
    private final UUID id = UUID.randomUUID();
    private Kind kind;
    private String description;
    private int amount;
}
