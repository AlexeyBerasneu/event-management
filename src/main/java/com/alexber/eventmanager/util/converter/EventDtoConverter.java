package com.alexber.eventmanager.util.converter;

import com.alexber.eventmanager.entity.event.*;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class EventDtoConverter {

    public EventResponseDto toDto(Event event) {
        return new EventResponseDto(
                event.id(),
                event.name(),
                event.maxPlaces(),
                event.date(),
                event.cost(),
                event.duration(),
                event.ownerId(),
                event.locationId(),
                event.status(),
                event.occupiedPlaces()
        );
    }

    public EventResponseDto toDto(EventEntity eventEntity) {
        return new EventResponseDto(
                eventEntity.getId(),
                eventEntity.getEventName(),
                eventEntity.getMaxPlaces(),
                eventEntity.getDate(),
                eventEntity.getCost(),
                eventEntity.getDuration(),
                eventEntity.getOwner().getId(),
                eventEntity.getLocation().getId(),
                eventEntity.getStatus(),
                eventEntity.getOccupiedPlaces()
        );
    }

    public Event toDomain(EventCreateRequestDto createRequestDto, Long ownerId) {
        return new Event(
                null,
                createRequestDto.name(),
                createRequestDto.maxPlaces(),
                OffsetDateTime.parse(createRequestDto.date(), DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                createRequestDto.cost(),
                createRequestDto.duration(),
                ownerId,
                createRequestDto.locationId(),
                EventStatus.WAIT_START,
                0
        );
    }
}
