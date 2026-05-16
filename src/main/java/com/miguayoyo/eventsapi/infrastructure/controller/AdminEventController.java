package com.miguayoyo.eventsapi.infrastructure.controller;

import com.miguayoyo.eventsapi.application.command.CreateEventCommand;
import com.miguayoyo.eventsapi.application.command.CreateEventCommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final CreateEventCommandHandler createEventCommandHandler;

    @PostMapping
    public ResponseEntity<String> createEvent(@RequestBody CreateEventCommand command) {
        String eventId = createEventCommandHandler.handle(command);
        return new ResponseEntity<>(eventId, HttpStatus.CREATED);
    }
}
