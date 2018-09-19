package io.github.joaovicente.piggybank.controller;

import io.github.joaovicente.piggybank.dto.BalanceResponseDto;
import io.github.joaovicente.piggybank.model.Transaction;
import io.github.joaovicente.piggybank.dao.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BalanceController {
    @Autowired
    TransactionRepository transactionRepository;

    @RequestMapping(value = "/balance", method = RequestMethod.GET)

    public BalanceResponseDto getBalance() {
        int balance =0;
	    List<Transaction> transactions = transactionRepository.findAll();
	    for (Transaction transaction : transactions)    {
	        if (transaction.getKind() == Transaction.Kind.CREDIT)
	            balance = balance + transaction.getAmount();
            else if (transaction.getKind() == Transaction.Kind.DEBIT)
                balance = balance - transaction.getAmount();
        }
        return BalanceResponseDto.builder()
                .balance(balance)
                .build();
    }
}

