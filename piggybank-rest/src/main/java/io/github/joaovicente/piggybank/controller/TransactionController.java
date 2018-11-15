package io.github.joaovicente.piggybank.controller;

import io.github.joaovicente.piggybank.dto.ErrorDto;
import io.github.joaovicente.piggybank.dto.IdResponseDto;
import io.github.joaovicente.piggybank.dto.TransactionDto;
import io.github.joaovicente.piggybank.dto.TransactionReadDto;
import io.github.joaovicente.piggybank.service.KidNotFoundException;
import io.github.joaovicente.piggybank.service.TransactionNotFoundException;
import io.github.joaovicente.piggybank.service.TransactionService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
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
    @PostMapping(path="/transactions")
    public IdResponseDto createTransaction(
            @Valid @RequestBody TransactionDto reqBody)    {
       IdResponseDto dto;
        try {
            dto = transactionService.createTransaction(reqBody);
        }
        catch(KidNotFoundException e)  {

            ErrorDto errorDto = ErrorDto.builder()
                    .error("NOT_FOUND")
                    .message(Collections.singletonList("kidId not found"))
                    .build();
            throw new RestResponseException(errorDto, HttpStatus.NOT_FOUND);
        }
        return dto;
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
    public List<TransactionReadDto> getTransactions(@RequestParam(name="kidId") String kidId) {
        List<TransactionReadDto> response;
        try {
            response = transactionService.getTransactionsByKidId(kidId);
        }
        catch(KidNotFoundException e)  {
            ErrorDto errorDto = ErrorDto.builder()
                    .error("NOT_FOUND")
                    .message(Collections.singletonList("kidId not found"))
                    .build();
            throw new RestResponseException(errorDto, HttpStatus.NOT_FOUND);
        }
        return response;
    }
}
