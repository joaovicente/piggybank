package io.github.joaovicente.piggybank.mapper;

import io.github.joaovicente.piggybank.model.Transaction;
import io.github.joaovicente.piggybank.dto.TransactionResponseDto;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public TransactionResponseDto toDto(Transaction transaction)   {
        TransactionResponseDto dto = TransactionResponseDto.builder()
                .id(transaction.getId().toString())
                .kind(transaction.getKind().toString())
                .description(transaction.getDescription())
                .amount(transaction.getAmount())
                .build();
        return dto;
    }
}
