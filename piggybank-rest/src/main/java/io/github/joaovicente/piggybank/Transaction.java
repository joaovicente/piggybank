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
    @Id
    private String id;
//    enum type {DEPOSIT, WITHDRAWAL}
    private String description;
    private float amount;
}
