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
    @Id @NonNull private EventId id;
    private String title;
    private String description;
    private Instant dateTime;
    private String location;
    private String category;
    private String tagClass;
    private String imageUrl;
    private EventStatus status;

    // Business Methods (Domain Logic)
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
