package io.github.joaovicente.piggybank.repository;

import io.github.joaovicente.piggybank.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
}
