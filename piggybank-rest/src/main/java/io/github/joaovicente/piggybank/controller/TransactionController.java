package io.github.joaovicente.piggybank.controller;

import io.github.joaovicente.piggybank.dto.ErrorDto;
import io.github.joaovicente.piggybank.dto.TransactionDto;
import io.github.joaovicente.piggybank.entity.Transaction;
import io.github.joaovicente.piggybank.service.KidNotFoundException;
import io.github.joaovicente.piggybank.service.TransactionNotFoundException;
import io.github.joaovicente.piggybank.service.TransactionService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TransactionController {
    private TransactionService transactionService;
    private ModelMapper modelMapper;

    @Autowired
    TransactionController(TransactionService transactionService, ModelMapper modelMapper)   {
        this.transactionService = transactionService;
        this.modelMapper = modelMapper;
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    @PostMapping(path="/transactions")
    public TransactionDto createTransaction(@Valid @RequestBody TransactionDto request)    {
        Transaction createdTransaction;

        try {
            createdTransaction = transactionService.createTransaction(modelMapper.map(request, Transaction.class));
        }
        catch(KidNotFoundException e)  {
            ErrorDto errorDto = ErrorDto.builder()
                    .error("NOT_FOUND")
                    .message(Collections.singletonList("kidId not found"))
                    .build();
            throw new RestResponseException(errorDto, HttpStatus.NOT_FOUND);
        }
        return modelMapper.map(createdTransaction, TransactionDto.class);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    @DeleteMapping(path="/transactions/{transactionId}")
    public void deleteTransaction(
            @PathVariable(name="transactionId") String transactionId) {
        try {
            transactionService.deleteTransaction(transactionId);
        }
        catch(TransactionNotFoundException e)  {
            ErrorDto errorDto = ErrorDto.builder()
                    .error("NOT_FOUND")
                    .message(Collections.singletonList("transactionId not found"))
                    .build();
            throw new RestResponseException(errorDto, HttpStatus.NOT_FOUND);
        }
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    @GetMapping(path="/transactions")
    public List<TransactionDto> getTransactions(@RequestParam(name="kidId") String kidId) {
        List<Transaction> transactionList;
        try {
            transactionList = transactionService.getTransactionsByKidId(kidId);
        }
        catch(KidNotFoundException e)  {
            ErrorDto errorDto = ErrorDto.builder()
                    .error("NOT_FOUND")
                    .message(Collections.singletonList("kidId not found"))
                    .build();
            throw new RestResponseException(errorDto, HttpStatus.NOT_FOUND);
        }
        return transactionList.stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDto.class))
                .collect(Collectors.toList());
    }
}
