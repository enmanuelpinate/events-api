package com.miguayoyo.eventsapi.domain.model;

import java.time.Instant;

public class Event {
    private EventId id;
    private String title;
    private String description;
    private Instant dateTime;
    private String location;
    private String category;
    private String tagClass;
    private String imageUrl;
    private EventStatus status;
}
