package com.alexber.eventmanager.util.converter;

import com.alexber.eventmanager.entity.event.*;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class EventUpdateDtoConverter {

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

    public Event toDomain(EventUpdateRequestDto updateRequestDto,Long ownerId) {
        return new Event(
                null,
                updateRequestDto.name(),
                updateRequestDto.maxPlaces(),
                OffsetDateTime.parse(updateRequestDto.date(), DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                updateRequestDto.cost(),
                updateRequestDto.duration(),
                ownerId,
                updateRequestDto.locationId(),
                null,
                null
        );
    }
}
