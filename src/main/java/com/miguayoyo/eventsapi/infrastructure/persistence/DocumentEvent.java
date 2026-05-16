package com.miguayoyo.eventsapi.infrastructure.persistence;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "events")
public class DocumentEvent {
    @Id private String id;
    private String title;
    private String description;
    private Instant dateTime;
    private String location;
    private String category;
    private String tagClass;
    private String imageUrl;
    private String status;
}
