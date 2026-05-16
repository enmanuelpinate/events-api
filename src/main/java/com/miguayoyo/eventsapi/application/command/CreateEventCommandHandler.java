package com.miguayoyo.eventsapi.application.command;

import com.miguayoyo.eventsapi.domain.model.Event;
import com.miguayoyo.eventsapi.domain.model.EventId;
import com.miguayoyo.eventsapi.domain.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateEventCommandHandler {

    private final EventRepository eventRepository;

    public String handle(CreateEventCommand command) {
        Event event = new Event(
                EventId.generate(),
                command.title(),
                command.description(),
                command.dateTime(),
                command.location(),
                command.category(),
                command.tagClass(),
                command.imageUrl(),
                command.eventStatus()
        );

        // Explicit business rule requirement: Let's automatically publish it for now
        event.publish();

        eventRepository.save(event);
        return event.getId().value();
    }
}
