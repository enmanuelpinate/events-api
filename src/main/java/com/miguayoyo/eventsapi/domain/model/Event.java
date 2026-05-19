package com.miguayoyo.eventsapi.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class Event {
    private final EventId id;
    private String title;
    private String description;
    private Instant dateTime;
    private String location;
    private String category;
    private String tagClass;
    private String imageUrl;
    private EventStatus status;

    /**
     * Business domain operations for mutations (Updates, Publishing, Cancelling)
     */
    public void updateDetails(String title, String description, Instant dateTime,
                              String location, String category, String tagClass, String imageUrl) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be updated to empty");
        }
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
        this.location = location;
        this.category = category;
        this.tagClass = tagClass;
        this.imageUrl = imageUrl;
    }

    public void publish() {
        if (this.status == EventStatus.CANCELLED) {
            throw new IllegalStateException("Cannot publish a cancelled event");
        }
        this.status = EventStatus.PUBLISHED;
    }

    public void cancel() {
        this.status = EventStatus.CANCELLED;
    }
}
