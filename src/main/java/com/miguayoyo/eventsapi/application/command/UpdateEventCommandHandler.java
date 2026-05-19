package com.miguayoyo.eventsapi.application.command;

import com.miguayoyo.eventsapi.domain.model.Event;
import com.miguayoyo.eventsapi.domain.model.EventId;
import com.miguayoyo.eventsapi.domain.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateEventCommandHandler {
    private final EventRepository eventRepository;

    public void handle(UpdateEventCommand command) {
        Event event = eventRepository.findById(new EventId(command.id()))
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + command.id()));

        event.updateDetails(
                command.title(),
                command.description(),
                command.dateTime(),
                command.location(),
                command.category(),
                command.tagClass(),
                command.imageUrl()
        );

        eventRepository.save(event);
    }
}
