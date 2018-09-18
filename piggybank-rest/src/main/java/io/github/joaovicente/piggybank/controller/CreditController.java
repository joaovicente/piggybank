package io.github.joaovicente.piggybank.controller;

import io.github.joaovicente.piggybank.model.Transaction;
import io.github.joaovicente.piggybank.dao.TransactionRepository;
import io.github.joaovicente.piggybank.dto.CreateCreditRequestDto;
import io.github.joaovicente.piggybank.dto.IdResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreditController {
    @Autowired
    TransactionRepository transactionRepository;

    @RequestMapping(value = "/credit", method = RequestMethod.POST)

    public IdResponseDto createCredit(@RequestBody CreateCreditRequestDto createCreditRequestDto) {
        //TODO: Fill-in today's date if null
        Transaction transaction = Transaction.builder()
                .description(createCreditRequestDto.getDescription())
                .amount(createCreditRequestDto.getAmount())
                .kind(Transaction.Kind.CREDIT)
                .date(createCreditRequestDto.getDate())
                .build();
	transactionRepository.insert(transaction);
	IdResponseDto id = IdResponseDto.builder()
		.id(transaction.getId().toString())
		.build();
        return id;
    }
}

