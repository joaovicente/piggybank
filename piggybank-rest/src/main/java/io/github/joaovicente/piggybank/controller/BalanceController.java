package io.github.joaovicente.piggybank.controller;

import io.github.joaovicente.piggybank.dto.BalanceDto;
import io.github.joaovicente.piggybank.dto.ErrorDto;
import io.github.joaovicente.piggybank.service.BalanceService;
import io.github.joaovicente.piggybank.service.KidNotFoundException;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Collections;

import io.github.joaovicente.piggybank.dto.ErrorUtil.ErrorCode;
import io.github.joaovicente.piggybank.dto.ErrorUtil.ErrorMessage;

@RestController
public class BalanceController {
    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    @GetMapping(value = "/balance")
    public BalanceDto getBalance(@NotNull @RequestParam(name="kidId") String kidId) {
        BalanceDto balanceDto;
        try {
            balanceDto = new BalanceDto(balanceService.calculateKidBalance(kidId));
        }
        catch(KidNotFoundException e)  {
            ErrorDto errorDto = ErrorDto.builder()
                    .error(ErrorCode.RESOURCE_NOT_FOUND.value())
                    .message(Collections.singletonList(ErrorMessage.NOT_FOUND.prefix("kidId")))
                    .build();
            throw new RestResponseException(errorDto);
        }
        return balanceDto;
    }
}
