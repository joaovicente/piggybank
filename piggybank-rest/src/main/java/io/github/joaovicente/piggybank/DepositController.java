package io.github.joaovicente.piggybank;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class DepositController {
    @Autowired
    TransactionRepository transactionRepository;

    @RequestMapping(value = "/deposit", method = RequestMethod.POST)

    public CreateDepositRequestDto createDeposit(@RequestBody CreateDepositRequestDto createDepositRequestDto) {
	Transaction transaction = Transaction.builder()
                .description(createDepositRequestDto.getDescription())
                .amount(createDepositRequestDto.getAmount())
                .build();
        transactionRepository.insert(transaction);
        return createDepositRequestDto;
    }
}

