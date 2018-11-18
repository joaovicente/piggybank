package io.github.joaovicente.piggybank.service;

import io.github.joaovicente.piggybank.entity.Transaction;
import io.github.joaovicente.piggybank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    private TransactionRepository transactionRepository;
    private KidService kidService;

    @Autowired
    public TransactionService(
            TransactionRepository transactionRepository, KidService kidService) {
        this.transactionRepository = transactionRepository;
        this.kidService = kidService;
    }

    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.insert(transaction);
    }

    public List<Transaction> getTransactionsByKidId(String kidId)  {
        List<Transaction> transactionList;
        if (!kidService.kidExists(kidId))    {
            throw new KidNotFoundException();
        }
        transactionList = transactionRepository.findByKidId(kidId);
        return transactionList;
    }


    public Transaction getTransactionById(String transactionId)  {
        Optional<Transaction> transaction = transactionRepository.findById(transactionId);
        if (!transaction.isPresent())    {
            throw new TransactionNotFoundException();
        }
        return transaction.get();
    }

    public void deleteTransaction(String transactionId)   {
        Optional<Transaction> transaction = transactionRepository.findById(transactionId);
        if (!transaction.isPresent())    {
            throw new TransactionNotFoundException();
        }
        transactionRepository.deleteById(transactionId);
    }

}
