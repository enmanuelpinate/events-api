package com.miguayoyo.eventsapi.application.query.dto;

import java.time.Instant;

public record PublicEventSummaryDto(
        String id,
        String title,
        String description,
        Instant dateTime,
        String location,
        String category,
        String tagClass,
        String imageUrl
) {}