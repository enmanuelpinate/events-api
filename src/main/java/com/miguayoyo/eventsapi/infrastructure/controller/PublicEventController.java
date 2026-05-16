package com.miguayoyo.eventsapi.infrastructure.controller;

import com.miguayoyo.eventsapi.application.query.FindPublishedEventsQueryHandler;
import com.miguayoyo.eventsapi.application.query.dto.PublicEventSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class PublicEventController {

    private final FindPublishedEventsQueryHandler findPublishedEventsQueryHandler;

    @GetMapping
    public List<PublicEventSummaryDto> getAllPublishedEvents() {
        return findPublishedEventsQueryHandler.execute();
    }
}
