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
        // Arrange
        Instant futureDate = Instant.now().plus(10, ChronoUnit.DAYS);
        CreateEventCommand command = new CreateEventCommand(
                "Gran Marcha LGBT+",
                "Marcha anual del orgullo",
                futureDate,
                "Caracas",
                "Marcha",
                "tag-pride",
                "https://storage.com/marcha.jpg"
        );

        // Act
        String generatedId = commandHandler.handle(command);

        // Assert
        assertNotNull(generatedId);

        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        verify(eventRepository).save(eventCaptor.capture());

        Event savedEvent = eventCaptor.getValue();
        assertEquals(generatedId, savedEvent.getId().value());
        assertEquals("Gran Marcha LGBT+", savedEvent.getTitle());
        assertEquals(EventStatus.PUBLISHED, savedEvent.getStatus());
    }

    @Test
    void should_ThrowException_When_EventDateIsInThePast() {
        // Arrange
        Instant pastDate = Instant.now().minus(2, ChronoUnit.DAYS);
        CreateEventCommand command = new CreateEventCommand(
                "Evento Invalido",
                "Pasado",
                pastDate,
                "Caracas",
                "Cultural",
                "tag-fail",
                "https://storage.com/fail.jpg"
        );

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> commandHandler.handle(command));
        verifyNoInteractions(eventRepository);
    }
}
