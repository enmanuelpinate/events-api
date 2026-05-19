package com.miguayoyo.eventsapi.infrastructure.controller;

import com.miguayoyo.eventsapi.application.command.*;
import com.miguayoyo.eventsapi.application.query.FindAllEventsQueryHandler;
import com.miguayoyo.eventsapi.application.query.dto.EventDto;
import com.miguayoyo.eventsapi.domain.model.EventStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // We mock every application component that the controller injects
    @MockitoBean
    private CreateEventCommandHandler createEventCommandHandler;

    @MockitoBean
    private UpdateEventCommandHandler updateEventCommandHandler;

    @MockitoBean
    private DeleteEventCommandHandler deleteEventCommandHandler;

    @MockitoBean
    private FindAllEventsQueryHandler findAllEventsQueryHandler;

    @Test
    void should_ReturnAllEvents_When_GettingEventStream() throws Exception {
        // Arrange
        Instant sampleTime = Instant.now().plus(5, ChronoUnit.DAYS);
        EventDto mockEvent = new EventDto(
                "event-123",
                "Festival Gastronómico",
                "Comida típica",
                sampleTime,
                "Chacao",
                "Feria",
                "tag-food",
                "https://image.com/food.jpg",
                EventStatus.PUBLISHED.name()
        );

        when(findAllEventsQueryHandler.execute()).thenReturn(List.of(mockEvent));

        // Act & Assert
        mockMvc.perform(get("/api/v1/events"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("event-123"))
                .andExpect(jsonPath("$[0].title").value("Festival Gastronómico"))
                .andExpect(jsonPath("$[0].status").value("PUBLISHED"));
    }

    @Test
    void should_ReturnCreatedStatusAndId_When_SubmittingValidForm() throws Exception {
        // Arrange
        Instant futureTime = Instant.now().plus(2, ChronoUnit.DAYS);
        CreateEventCommand command = new CreateEventCommand(
                "Concierto de Rock",
                "Bandas locales en vivo",
                futureTime,
                "Plaza Altamira",
                "Música",
                "tag-rock",
                "https://image.com/rock.jpg"
        );

        String fakeGeneratedId = "new-uuid-456";
        when(createEventCommandHandler.handle(any(CreateEventCommand.class))).thenReturn(fakeGeneratedId);

        // Act & Assert
        mockMvc.perform(post("/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(content().string(fakeGeneratedId));
    }

    @Test
    void should_ReturnOkStatus_When_UpdatingExistingEvent() throws Exception {
        // Arrange
        String eventId = "existing-id-789";
        Instant updateTime = Instant.now().plus(1, ChronoUnit.DAYS);
        CreateEventCommand incomingUpdates = new CreateEventCommand(
                "Título Modificado",
                "Descripción nueva",
                updateTime,
                "Nuevo Lugar",
                "Cine",
                "tag-film",
                "https://image.com/movie.jpg"
        );

        // Explicitly tell the void handler to do nothing when executed
        doNothing().when(updateEventCommandHandler).handle(any(UpdateEventCommand.class));

        // Act & Assert
        mockMvc.perform(put("/api/v1/events/{id}", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incomingUpdates)))
                .andExpect(status().isOk());
    }

    @Test
    void should_ReturnNoContentStatus_When_DeletingEvent() throws Exception {
        // Arrange
        String eventIdToDelete = "target-id-000";
        doNothing().when(deleteEventCommandHandler).handle(any(DeleteEventCommand.class));

        // Act & Assert
        mockMvc.perform(delete("/api/v1/events/{id}", eventIdToDelete))
                .andExpect(status().isNoContent());
    }
}
