package io.github.joaovicente.piggybank.service;

import io.github.joaovicente.piggybank.dto.IdResponseDto;
import io.github.joaovicente.piggybank.dto.TransactionCreateDto;
import io.github.joaovicente.piggybank.dto.TransactionReadDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    public IdResponseDto createTransaction(String kidId, TransactionCreateDto dto) {
        return null;
    }

    public List<TransactionReadDto> getTransactions(String kidId)  {
        return null;
    }

    public void deleteTransaction(String kidId, String transactionId)   {
    }

}
