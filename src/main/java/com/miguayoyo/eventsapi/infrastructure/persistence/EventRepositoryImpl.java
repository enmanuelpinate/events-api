package com.miguayoyo.eventsapi.infrastructure.persistence;

import com.miguayoyo.eventsapi.domain.model.Event;
import com.miguayoyo.eventsapi.domain.model.EventId;
import com.miguayoyo.eventsapi.domain.model.EventStatus;
import com.miguayoyo.eventsapi.domain.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EventRepositoryImpl implements EventRepository {

    private final SpringDataMongoEventRepository springDataRepository;

    @Override
    public void save(Event event) {
        DocumentEvent document = new DocumentEvent();
        document.setId(event.getId().value());
        document.setTitle(event.getTitle());
        document.setDescription(event.getDescription());
        document.setDateTime(event.getDateTime());
        document.setLocation(event.getLocation());
        document.setCategory(event.getCategory());
        document.setTagClass(event.getTagClass());
        document.setImageUrl(event.getImageUrl());
        document.setStatus(event.getStatus().name());

        springDataRepository.save(document);
    }

    @Override
    public Optional<Event> findById(EventId id) {
        return springDataRepository.findById(id.value())
                .map(doc -> new Event(
                        new EventId(doc.getId()),
                        doc.getTitle(),
                        doc.getDescription(),
                        doc.getDateTime(),
                        doc.getLocation(),
                        doc.getCategory(),
                        doc.getTagClass(),
                        doc.getImageUrl(),
                        EventStatus.valueOf(doc.getStatus()) // The missing 9th argument!
                ));
    }
}