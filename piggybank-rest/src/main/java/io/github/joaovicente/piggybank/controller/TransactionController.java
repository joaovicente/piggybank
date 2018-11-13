package io.github.joaovicente.piggybank.controller;

import io.github.joaovicente.piggybank.dto.IdResponseDto;
import io.github.joaovicente.piggybank.dto.TransactionCreateDto;
import io.github.joaovicente.piggybank.dto.TransactionReadDto;
import io.github.joaovicente.piggybank.service.TransactionService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class TransactionController {
    private TransactionService transactionService;

    @Autowired
    TransactionController(TransactionService transactionService)   {
        this.transactionService = transactionService;
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    @PostMapping(path="/kids/{kidId}/transactions")
    public IdResponseDto createTransaction(
            @PathVariable(name="kidId") String kidId,
            @Valid @RequestBody TransactionCreateDto reqBody)    {
        return transactionService.createTransaction(kidId, reqBody);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    @DeleteMapping(path="/kids/{kidId}/transactions/{transactionId}")
    public void deleteTransaction(
            @PathVariable(name="kidId") String kidId,
            @PathVariable(name="transactionId") String transactionId) {
        transactionService.deleteTransaction(kidId, transactionId);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    @GetMapping(path="/kids/{kidId}/transactions")
    public List<TransactionReadDto> getTransactions(@PathVariable(name="kidId") String kidId) {
        return transactionService.getTransactions(kidId);
    }
}
