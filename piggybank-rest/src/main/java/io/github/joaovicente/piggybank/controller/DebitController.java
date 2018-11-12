package io.github.joaovicente.piggybank.controller;

import io.github.joaovicente.piggybank.dto.CreateDebitResponseDto;
import io.github.joaovicente.piggybank.dto.ErrorDto;
import io.github.joaovicente.piggybank.model.Transaction;
import io.github.joaovicente.piggybank.dao.TransactionRepository;
import io.github.joaovicente.piggybank.dto.CreateDebitRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collections;
import java.util.Date;

@RestController
public class DebitController {
    private TransactionRepository transactionRepository;

    @RequestMapping(value = "/debit", method = RequestMethod.POST)
    public CreateDebitResponseDto createDebit(@RequestBody CreateDebitRequestDto createDebitRequestDto) {
        // Fill-in today's date if null
        if (createDebitRequestDto.getDate() == null)   {
            createDebitRequestDto.setDate(new Date());
        }
        if (createDebitRequestDto.getAmount() < 0) {
            ErrorDto errorDto = ErrorDto.builder()
                    .error("INVALID_DEBIT_AMOUNT")
                    .message(Collections.singletonList("Negative values are not allowed"))
                    .build();
           throw new RestResponseException(errorDto, HttpStatus.BAD_REQUEST);
        }
        Transaction transaction = Transaction.builder()
                .description(createDebitRequestDto.getDescription())
                .amount(createDebitRequestDto.getAmount())
                .kind(Transaction.Kind.DEBIT)
                .date(createDebitRequestDto.getDate())
                .build();
	transactionRepository.insert(transaction);
    return CreateDebitResponseDto.builder()
            .id(transaction.getId())
            .description(transaction.getDescription())
            .date(transaction.getDate())
            .amount(transaction.getAmount())
            .build();
    }
}

