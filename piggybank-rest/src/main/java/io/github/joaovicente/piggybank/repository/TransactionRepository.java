package io.github.joaovicente.piggybank.repository;

import io.github.joaovicente.piggybank.entity.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    public List<Transaction> findByKidId(String kidId);
}
