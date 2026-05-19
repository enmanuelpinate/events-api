package com.miguayoyo.eventsapi.application;

import com.miguayoyo.eventsapi.application.command.DeleteEventCommand;
import com.miguayoyo.eventsapi.application.command.DeleteEventCommandHandler;
import com.miguayoyo.eventsapi.domain.model.Event;
import com.miguayoyo.eventsapi.domain.model.EventId;
import com.miguayoyo.eventsapi.domain.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteEventCommandHandlerTest {

    @Mock
    private EventRepository eventRepository;

    private DeleteEventCommandHandler commandHandler;

    @BeforeEach
    void setUp() {
        commandHandler = new DeleteEventCommandHandler(eventRepository);
    }

    @Test
    void should_DeleteEvent_When_EventExistsInDatabase() {
        // Arrange
        String targetId = "delete-me-123";
        EventId eventId = new EventId(targetId);
        Event mockEvent = mock(Event.class);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(mockEvent));
        DeleteEventCommand command = new DeleteEventCommand(targetId);

        // Act
        commandHandler.handle(command);

        // Assert
        verify(eventRepository).delete(eventId);
    }

    @Test
    void should_ThrowException_When_TryingToDeleteMissingEvent() {
        // Arrange
        String targetId = "missing-id";
        EventId eventId = new EventId(targetId);

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());
        DeleteEventCommand command = new DeleteEventCommand(targetId);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> commandHandler.handle(command));
        verify(eventRepository, never()).delete(any(EventId.class));
    }
}