package com.miguayoyo.eventsapi.domain.model;

import java.util.UUID;

public record EventId(String value) {
    public EventId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("EventId cannot be empty");
        }
    }

    public static EventId generate() {
        return new EventId(UUID.randomUUID().toString());
    }
}
