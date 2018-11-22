package io.github.joaovicente.piggybank.repository;

import io.github.joaovicente.piggybank.entity.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, String> {
    List<Transaction> findByKidId(String kidId);
}
