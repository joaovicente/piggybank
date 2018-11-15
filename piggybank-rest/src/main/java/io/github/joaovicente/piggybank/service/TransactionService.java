package io.github.joaovicente.piggybank.service;

import io.github.joaovicente.piggybank.dto.IdResponseDto;
import io.github.joaovicente.piggybank.dto.TransactionDto;
import io.github.joaovicente.piggybank.dto.TransactionReadDto;
import io.github.joaovicente.piggybank.entity.Transaction;
import io.github.joaovicente.piggybank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    private TransactionRepository transactionRepository;
    private KidService kidService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, KidService kidService) {
        this.transactionRepository = transactionRepository;
        this.kidService = kidService;
    }

    public IdResponseDto createTransaction(TransactionDto requestDto) {
        Transaction transaction = null;
        transactionRepository.insert(transaction);
        IdResponseDto responseDto = IdResponseDto.builder()
                .id(transaction.getId())
                .build();
        return responseDto;
    }

    public List<TransactionReadDto> getTransactionsByKidId(String kidId)  {
        List<Transaction> modelTransaction = transactionRepository.findByKidId(kidId);
        List<TransactionReadDto> dtoTransactions = null;
        //TODO: Convert transaction model to transaction dto
        return dtoTransactions;
    }


    public void deleteTransaction(String transactionId)   {
        //TODO: Throw TransactionNotFound if transactionId does not exist
        transactionRepository.deleteById(transactionId);
    }

}
