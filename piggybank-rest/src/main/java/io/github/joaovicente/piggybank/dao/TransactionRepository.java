package io.github.joaovicente.piggybank.dao;

import io.github.joaovicente.piggybank.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
}
