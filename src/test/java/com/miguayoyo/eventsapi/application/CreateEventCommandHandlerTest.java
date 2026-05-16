package com.miguayoyo.eventsapi.application;

import com.miguayoyo.eventsapi.application.command.CreateEventCommand;
import com.miguayoyo.eventsapi.application.command.CreateEventCommandHandler;
import com.miguayoyo.eventsapi.domain.model.Event;
import com.miguayoyo.eventsapi.domain.model.EventStatus;
import com.miguayoyo.eventsapi.domain.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class CreateEventCommandHandlerTest {

    @Mock
    private EventRepository eventRepository;

    private CreateEventCommandHandler commandHandler;

    @BeforeEach
    void setUp() {
        commandHandler = new CreateEventCommandHandler(eventRepository);
    }

    @Test
    void should_CreateAndPublishEvent_When_CommandIsValid() {
        // Arrange (Given)
        Instant futureDate = Instant.now().plus(5, ChronoUnit.DAYS);
        CreateEventCommand command = new CreateEventCommand(
                "Gran Marcha del Orgullo",
                "Celebración anual en Caracas",
                futureDate,
                "Plaza Venezuela",
                "Marcha",
                "tag-marcha",
                "https://storage.com/marcha.jpg",
                EventStatus.PUBLISHED
        );

        // Act (When)
        String generatedId = commandHandler.handle(command);

        // Assert (Then)
        assertNotNull(generatedId, "The handler should return a valid generated Event ID");

        // Verify that the repository's save method was called exactly once, and capture the object sent to it
        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        verify(eventRepository).save(eventCaptor.capture());

        Event savedEvent = eventCaptor.getValue();
        assertEquals(generatedId, savedEvent.getId().value());
        assertEquals("Gran Marcha del Orgullo", savedEvent.getTitle());
        assertEquals(EventStatus.PUBLISHED, savedEvent.getStatus(), "New events processed by handler should be PUBLISHED");
    }
}
