package io.github.joaovicente.piggybank.controller;

import io.github.joaovicente.piggybank.model.Transaction;
import io.github.joaovicente.piggybank.dao.TransactionRepository;
import io.github.joaovicente.piggybank.dto.CreateDebitRequestDto;
import io.github.joaovicente.piggybank.dto.IdResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebitController {
    @Autowired
    TransactionRepository transactionRepository;

    @RequestMapping(value = "/debit", method = RequestMethod.POST)

    public IdResponseDto createDebit(@RequestBody CreateDebitRequestDto createDebitRequestDto) {
        Transaction transaction = Transaction.builder()
                .description(createDebitRequestDto.getDescription())
                .amount(createDebitRequestDto.getAmount())
                .kind(Transaction.Kind.DEBIT)
                .build();
	transactionRepository.insert(transaction);
	IdResponseDto id = IdResponseDto.builder()
		.id(transaction.getId().toString())
		.build();
        return id;
    }
}

