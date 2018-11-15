package io.github.joaovicente.piggybank.mapper;

import io.github.joaovicente.piggybank.entity.Transaction;
import io.github.joaovicente.piggybank.dto.TransactionResponseDto;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public TransactionResponseDto toDto(Transaction transaction)   {
        return TransactionResponseDto.builder()
                .id(transaction.getId())
                .kind(transaction.getKind().toString())
                .description(transaction.getDescription())
                .amount(transaction.getAmount())
                .date(transaction.getDate())
                .build();
    }
}
