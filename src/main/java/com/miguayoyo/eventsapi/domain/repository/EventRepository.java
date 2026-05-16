package com.miguayoyo.eventsapi.domain.repository;

import com.miguayoyo.eventsapi.domain.model.Event;
import com.miguayoyo.eventsapi.domain.model.EventId;

import java.util.Optional;

public interface EventRepository {
    void save(Event event);
    Optional<Event> findById(EventId id);
}
