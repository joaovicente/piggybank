package io.github.joaovicente.piggybank.repository;

import io.github.joaovicente.piggybank.entity.Kid;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface KidRepository extends MongoRepository<Kid, String> {
}
