package io.github.joaovicente.piggybank.service;

import io.github.joaovicente.piggybank.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.github.joaovicente.piggybank.type.TransactionKind.CREDIT;

@Service
public class BalanceService {
    private final KidService kidService;
    private final TransactionService transactionService;

    @Autowired
    public BalanceService(KidService kidService, TransactionService transactionService) {
        this.kidService = kidService;
        this.transactionService = transactionService;
    }

    public int calculateKidBalance(String kidId)    {
        if (!kidService.kidExists(kidId))    {
            throw new KidNotFoundException();
        }
        List<Transaction> transactionList = transactionService.getTransactionsByKidId(kidId);
        return transactionList.stream()
                .mapToInt(t -> t.getKind()==CREDIT ? t.getAmount() : -t.getAmount())
                .sum();
    }
}
