package io.github.joaovicente.piggybank;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
public class DepositController {
    @RequestMapping(value = "/deposit", method = RequestMethod.POST)

    public CreateDepositRequestDto createDeposit(@RequestBody CreateDepositRequestDto createDepositRequestDto) {
        return createDepositRequestDto;
    }
}

