package com.miguayoyo.eventsapi.application.query;

import com.miguayoyo.eventsapi.application.query.dto.PublicEventSummaryDto;
import com.miguayoyo.eventsapi.infrastructure.persistence.SpringDataMongoEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FindPublishedEventsQueryHandler {

    private final SpringDataMongoEventRepository mongoRepository;

    // Read Model Bypass: Queries read straight from Mongo for high performance
    public List<PublicEventSummaryDto> execute() {
        return mongoRepository.findByStatus("PUBLISHED")
                .stream()
                .map(doc -> new PublicEventSummaryDto(
                        doc.getId(),
                        doc.getTitle(),
                        doc.getDescription(),
                        doc.getDateTime(),
                        doc.getLocation(),
                        doc.getCategory(),
                        doc.getTagClass(),
                        doc.getImageUrl()
                ))
                .collect(Collectors.toList());
    }
}
