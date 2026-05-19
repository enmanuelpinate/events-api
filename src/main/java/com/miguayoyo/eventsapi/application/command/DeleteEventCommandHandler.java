package com.miguayoyo.eventsapi.application.command;

import com.miguayoyo.eventsapi.domain.model.EventId;
import com.miguayoyo.eventsapi.domain.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteEventCommandHandler {
    private final EventRepository eventRepository;

    public void handle(DeleteEventCommand command) {
        // Enforce business validation check before letting data vanish
        EventId eventId = new EventId(command.id());
        eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Cannot delete. Event not found."));

        eventRepository.delete(eventId);
    }
}
