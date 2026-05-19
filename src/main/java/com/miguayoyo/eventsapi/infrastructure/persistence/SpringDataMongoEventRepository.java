package com.miguayoyo.eventsapi.infrastructure.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataMongoEventRepository extends MongoRepository<DocumentEvent, String> {
}
