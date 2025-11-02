package com.example.maersk.repository;

import com.example.maersk.model.BookingDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends ReactiveMongoRepository<BookingDocument, String> {
}
