package com.miguayoyo.eventsapi.application;

import com.miguayoyo.eventsapi.application.command.UpdateEventCommand;
import com.miguayoyo.eventsapi.application.command.UpdateEventCommandHandler;
import com.miguayoyo.eventsapi.domain.model.Event;
import com.miguayoyo.eventsapi.domain.model.EventId;
import com.miguayoyo.eventsapi.domain.model.EventStatus;
import com.miguayoyo.eventsapi.domain.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateEventCommandHandlerTest {

    @Mock
    private EventRepository eventRepository;

    private UpdateEventCommandHandler commandHandler;

    @BeforeEach
    void setUp() {
        commandHandler = new UpdateEventCommandHandler(eventRepository);
    }

    @Test
    void should_UpdateEventDetails_When_EventExists() {
        // Arrange
        String existingId = "existing-uuid-123";
        Instant originalDate = Instant.now().plus(2, ChronoUnit.DAYS);

        Event existingEvent = new Event(
                new EventId(existingId),
                "Titulo Original",
                "Descripcion vieja",
                originalDate,
                "Lugar viejo",
                "Categoria",
                "tag-old",
                "https://link.com/old.jpg",
                EventStatus.PUBLISHED
        );

        when(eventRepository.findById(new EventId(existingId))).thenReturn(Optional.of(existingEvent));

        Instant newDate = Instant.now().plus(5, ChronoUnit.DAYS);
        UpdateEventCommand command = new UpdateEventCommand(
                existingId,
                "Titulo Actualizado",
                "Descripcion fresca!",
                newDate,
                "Altamira",
                "Concierto",
                "tag-new",
                "https://link.com/new.jpg"
        );

        // Act
        commandHandler.handle(command);

        // Assert
        assertEquals("Titulo Actualizado", existingEvent.getTitle());
        assertEquals("Descripcion fresca!", existingEvent.getDescription());
        assertEquals("Altamira", existingEvent.getLocation());
        assertEquals(newDate, existingEvent.getDateTime());
        verify(eventRepository).save(existingEvent);
    }

    @Test
    void should_ThrowException_When_UpdatingNonExistentEvent() {
        // Arrange
        String fakeId = "not-found-id";
        when(eventRepository.findById(new EventId(fakeId))).thenReturn(Optional.empty());

        UpdateEventCommand command = new UpdateEventCommand(
                fakeId, "Title", "Desc", Instant.now().plus(1, ChronoUnit.DAYS), "Loc", "Cat", "tag", "img"
        );

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> commandHandler.handle(command));
        verify(eventRepository, never()).save(any(Event.class));
    }
}
