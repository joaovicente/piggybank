package io.github.joaovicente.piggybank.controller;

import io.github.joaovicente.piggybank.model.Transaction;
import io.github.joaovicente.piggybank.mapper.TransactionMapper;
import io.github.joaovicente.piggybank.dao.TransactionRepository;
import io.github.joaovicente.piggybank.dto.StatementDto;
import io.github.joaovicente.piggybank.dto.TransactionResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StatementController {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransactionMapper transactionMapper;

    @RequestMapping(value = "/statement", method = RequestMethod.GET)

    public StatementDto getStatement() {
	    List<Transaction> transactions = transactionRepository.findAll();
	    List<TransactionResponseDto> transactionDtos = new ArrayList<>();
	    for (Transaction transaction : transactions) {
            transactionDtos.add(transactionMapper.toDto(transaction));
        }
        StatementDto statement = StatementDto.builder()
                .transactions(transactionDtos)
                .build();
	    return statement;
    }
}

