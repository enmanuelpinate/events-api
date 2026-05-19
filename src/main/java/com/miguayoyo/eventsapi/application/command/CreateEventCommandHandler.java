package com.miguayoyo.eventsapi.application.command;

import com.miguayoyo.eventsapi.domain.model.Event;
import com.miguayoyo.eventsapi.domain.model.EventId;
import com.miguayoyo.eventsapi.domain.model.EventStatus;
import com.miguayoyo.eventsapi.domain.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateEventCommandHandler {
    private final EventRepository eventRepository;

    public String handle(CreateEventCommand command) {
        // Validate date requirements first
        if (command.dateTime().isBefore(java.time.Instant.now())) {
            throw new IllegalArgumentException("Event date cannot be in the past");
        }

        // Count them out: We need exactly 9 arguments here
        Event event = new Event(
                EventId.generate(),             // 1. Id
                command.title(),                // 2. Title
                command.description(),          // 3. Description
                command.dateTime(),             // 4. DateTime (Instant)
                command.location(),             // 5. Location
                command.category(),             // 6. Category
                command.tagClass(),             // 7. TagClass
                command.imageUrl(),             // 8. ImageUrl
                EventStatus.DRAFT               // 9. Initial Domain Status
        );

        // If your business rule dictates that new events are live instantly:
        event.publish(); // This safely transitions the status internally from DRAFT to PUBLISHED

        eventRepository.save(event);
        return event.getId().value();
    }
}
