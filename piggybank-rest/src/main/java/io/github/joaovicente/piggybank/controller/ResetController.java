package io.github.joaovicente.piggybank.controller;

import io.github.joaovicente.piggybank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResetController {
    @Autowired
    TransactionRepository transactionRepository;

    @RequestMapping(value = "/reset", method = RequestMethod.POST)

    public void reset() {
        transactionRepository.deleteAll();
    }
}

