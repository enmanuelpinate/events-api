package com.miguayoyo.eventsapi.domain;

import com.miguayoyo.eventsapi.infrastructure.persistence.DocumentEvent;
import com.miguayoyo.eventsapi.infrastructure.persistence.SpringDataMongoEventRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class EventRepositoryTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private SpringDataMongoEventRepository repository;

    @AfterEach
    void tearDown() {
        // Keeps tests perfectly isolated from one another by purging database entries
        repository.deleteAll();
    }

    @Test
    void should_SaveAndRetrieveDocument_When_PayloadIsValid() {
        // Arrange
        String sampleId = "test-event-uuid-123";

        DocumentEvent event = new DocumentEvent();
        event.setId(sampleId);
        event.setTitle("Concierto de Rock");
        event.setDescription("Bandas locales en directo");
        event.setDateTime(Instant.now().plus(3, ChronoUnit.DAYS));
        event.setLocation("Plaza Altamira");
        event.setCategory("Música");
        event.setTagClass("tag-rock");
        event.setImageUrl("https://image.com/rock.jpg");
        event.setStatus("PUBLISHED");

        // Act
        repository.save(event);
        Optional<DocumentEvent> searchResult = repository.findById(sampleId);

        // Assert
        assertThat(searchResult).isPresent();
        DocumentEvent savedEvent = searchResult.get();
        assertThat(savedEvent.getId()).isEqualTo(sampleId);
        assertThat(savedEvent.getTitle()).isEqualTo("Concierto de Rock");
        assertThat(savedEvent.getCategory()).isEqualTo("Música");
        assertThat(savedEvent.getStatus()).isEqualTo("PUBLISHED");
    }

    @Test
    void should_ReturnEmptyOptional_When_DocumentIdDoesNotExist() {
        // Act
        Optional<DocumentEvent> searchResult = repository.findById("non-existent-id-123");

        // Assert
        assertThat(searchResult).isEmpty();
    }
}
