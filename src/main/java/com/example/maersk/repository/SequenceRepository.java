package com.example.maersk.repository;

import com.example.maersk.model.DatabaseSequence;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SequenceRepository extends ReactiveMongoRepository<DatabaseSequence, String> {
}
