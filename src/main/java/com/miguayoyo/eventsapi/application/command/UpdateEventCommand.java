package com.miguayoyo.eventsapi.application.command;

import java.time.Instant;

public record UpdateEventCommand(
        String id,
        String title,
        String description,
        Instant dateTime,
        String location,
        String category,
        String tagClass,
        String imageUrl
) {}
