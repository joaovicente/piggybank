package io.github.joaovicente.piggybank;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class CreditController {
    @Autowired
    TransactionRepository transactionRepository;

    @RequestMapping(value = "/credit", method = RequestMethod.POST)

    public IdResponseDto createCredit(@RequestBody CreateCreditRequestDto createCreditRequestDto) {
        Transaction transaction = Transaction.builder()
                .description(createCreditRequestDto.getDescription())
                .amount(createCreditRequestDto.getAmount())
                .kind(Transaction.Kind.CREDIT)
                .build();
	transactionRepository.insert(transaction);
	IdResponseDto id = IdResponseDto.builder()
		.id(transaction.getId().toString())
		.build();
        return id;
    }
}

