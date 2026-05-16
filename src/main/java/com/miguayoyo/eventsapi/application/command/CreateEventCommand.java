package com.miguayoyo.eventsapi.application.command;

import com.miguayoyo.eventsapi.domain.model.EventStatus;

import java.time.Instant;

public record CreateEventCommand(
        String title,
        String description,
        Instant dateTime,
        String location,
        String category,
        String tagClass,
        String imageUrl,
        EventStatus eventStatus
) {}
