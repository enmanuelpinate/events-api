package com.miguayoyo.eventsapi.application.query;

import com.miguayoyo.eventsapi.application.query.dto.EventDto;
import com.miguayoyo.eventsapi.infrastructure.persistence.SpringDataMongoEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FindAllEventsQueryHandler {
    private final SpringDataMongoEventRepository mongoRepository;

    public List<EventDto> execute() {
        return mongoRepository.findAll()
                .stream()
                .map(doc -> new EventDto(
                        doc.getId(),
                        doc.getTitle(),
                        doc.getDescription(),
                        doc.getDateTime(),
                        doc.getLocation(),
                        doc.getCategory(),
                        doc.getTagClass(),
                        doc.getImageUrl(),
                        doc.getStatus()
                ))
                .collect(Collectors.toList());
    }
}
