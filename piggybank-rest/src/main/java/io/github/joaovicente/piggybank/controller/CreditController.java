package io.github.joaovicente.piggybank.controller;

import io.github.joaovicente.piggybank.dto.CreateCreditResponseDto;
import io.github.joaovicente.piggybank.dto.ErrorDto;
import io.github.joaovicente.piggybank.exception.RestResponseException;
import io.github.joaovicente.piggybank.model.Transaction;
import io.github.joaovicente.piggybank.dao.TransactionRepository;
import io.github.joaovicente.piggybank.dto.CreateCreditRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static org.springframework.http.ResponseEntity.ok;

@RestController
public class CreditController {
    @Autowired
    TransactionRepository transactionRepository;

    @RequestMapping(value = "/credit", method = RequestMethod.POST)
    public CreateCreditResponseDto createCredit(@RequestBody CreateCreditRequestDto createCreditRequestDto) {
        Date date;
        // Fill-in today's date if null
        if (createCreditRequestDto.getDate() == null)   {
            date = new Date();
        }
        else    {
            date = createCreditRequestDto.getDate();
        }
        if (createCreditRequestDto.getAmount() < 0) {
            ErrorDto errorDto = ErrorDto.builder()
                    .error("INVALID_CREDIT_AMOUNT")
                    .message("Negative values are not allowed")
                    .build();
            throw new RestResponseException(errorDto);
        }
        Transaction transaction = Transaction.builder()
                .description(createCreditRequestDto.getDescription())
                .amount(createCreditRequestDto.getAmount())
                .kind(Transaction.Kind.CREDIT)
                .date(date)
                .build();
        transactionRepository.insert(transaction);
        return CreateCreditResponseDto.builder()
                .id(transaction.getId())
                .description(transaction.getDescription())
                .date(transaction.getDate())
                .amount(transaction.getAmount())
                .build();
    }
}

