package com.miguayoyo.eventsapi.infrastructure.controller;

import com.miguayoyo.eventsapi.application.command.*;
import com.miguayoyo.eventsapi.application.query.FindAllEventsQueryHandler;
import com.miguayoyo.eventsapi.application.query.dto.EventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {
    private final CreateEventCommandHandler createEventCommandHandler;
    private final UpdateEventCommandHandler updateEventCommandHandler;
    private final DeleteEventCommandHandler deleteEventCommandHandler;
    private final FindAllEventsQueryHandler findAllEventsQueryHandler;

    @GetMapping
    public List<EventDto> getAllEvents() {
        return findAllEventsQueryHandler.execute();
    }

    @PostMapping
    public ResponseEntity<String> createEvent(@RequestBody CreateEventCommand command) {
        String eventId = createEventCommandHandler.handle(command);
        return new ResponseEntity<>(eventId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateEvent(@PathVariable String id, @RequestBody CreateEventCommand command) {
        UpdateEventCommand updateCommand = new UpdateEventCommand(
                id,
                command.title(),
                command.description(),
                command.dateTime(),
                command.location(),
                command.category(),
                command.tagClass(),
                command.imageUrl()
        );
        updateEventCommandHandler.handle(updateCommand);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String id) {
        deleteEventCommandHandler.handle(new DeleteEventCommand(id));
        return ResponseEntity.noContent().build();
    }
}
