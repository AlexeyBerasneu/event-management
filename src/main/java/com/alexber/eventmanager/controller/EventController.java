package com.alexber.eventmanager.controller;

import com.alexber.eventmanager.entity.event.*;
import com.alexber.eventmanager.entity.user.User;
import com.alexber.eventmanager.service.EventService;
import com.alexber.eventmanager.util.converter.EventDtoConverter;
import com.alexber.eventmanager.util.converter.EventUpdateDtoConverter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/events")
public class EventController {

    private final Logger logger = LoggerFactory.getLogger(EventController.class);
    private final EventService eventService;
    private final EventDtoConverter eventDtoConverter;
    private final EventUpdateDtoConverter eventUpdateDtoConverter;

    public EventController(EventService eventService, EventDtoConverter eventDtoConverter, EventUpdateDtoConverter eventUpdateDtoConverter) {
        this.eventService = eventService;
        this.eventDtoConverter = eventDtoConverter;
        this.eventUpdateDtoConverter = eventUpdateDtoConverter;
    }

    @PostMapping
    public ResponseEntity<EventResponseDto> createEvent(@Valid @RequestBody EventCreateRequestDto eventCreateRequestDto,
                                                        @AuthenticationPrincipal User user) {
        logger.info("Creating event {}", eventCreateRequestDto);
        Long ownerId = user.id();
        Event createdEvent = eventService.createEvent(eventDtoConverter.toDomain(eventCreateRequestDto, ownerId));
        return ResponseEntity.status(HttpStatus.CREATED).body(eventDtoConverter.toDto(createdEvent));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable
                                            @Positive Long eventId,
                                            @AuthenticationPrincipal User user) {
        logger.info("Deleting event {}", eventId);
        eventService.deleteEventById(eventId, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDto> getEvent(@PathVariable @Positive Long eventId) {
        logger.info("Getting event {}", eventId);
        return ResponseEntity.ok(eventService.getEventById(eventId));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponseDto> updateEvent(@Valid
                                                        @RequestBody EventUpdateRequestDto eventUpdateRequestDto,
                                                        @PathVariable
                                                        @Positive
                                                        Long eventId,
                                                        @AuthenticationPrincipal User user
    ) {
        logger.info("Updating event {}", eventId);
        Event updatedEvent = eventService.updateEvent(eventUpdateDtoConverter.toDomain(eventUpdateRequestDto, user.id()), eventId, user);
        return ResponseEntity.ok(eventDtoConverter.toDto(updatedEvent));
    }

    @PostMapping("/search")
    public List<EventResponseDto> searchEvents(@RequestBody EventSearchRequestDto searchRequestDto) {
        logger.info("Searching events with filters");
        List<Event> searchedEvents = eventService.searchEventsWithParam(searchRequestDto);
        return searchedEvents.stream().map(eventDtoConverter::toDto).collect(Collectors.toList());
    }

    @GetMapping("/my")
    public List<EventResponseDto> getMyEvents(@AuthenticationPrincipal User user) {
        logger.info("Getting my events");
        return eventService.getAllEventsByUser(user);
    }

    @PostMapping("/registrations/{eventId}")
    public ResponseEntity<Void> registration(@PathVariable
                                             @Positive
                                             Long eventId,
                                             @AuthenticationPrincipal User user) {
        logger.info("Registration to event {}", eventId);
        eventService.registration(eventId, user.id());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/registrations/cancel/{eventId}")
    public ResponseEntity<Void> cancelRegistration(@PathVariable
                                                   @Positive
                                                   Long eventId,
                                                   @AuthenticationPrincipal User user) {
        logger.info("Cancelling registration for event {}", eventId);
        eventService.cancelRegistration(eventId, user.id());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/registrations/my")
    public List<EventResponseDto> getMyRegistrations(@AuthenticationPrincipal User user) {
        logger.info("Getting my registrations");
        return eventService.getEventsByUserId(user.id());
    }
}
