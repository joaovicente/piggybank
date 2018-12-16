package io.github.joaovicente.piggybank.repository;

import io.github.joaovicente.piggybank.entity.Kid;
import org.springframework.data.repository.CrudRepository;

public interface KidRepository extends CrudRepository<Kid, String> {
}
