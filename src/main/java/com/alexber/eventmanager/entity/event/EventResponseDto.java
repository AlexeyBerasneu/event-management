package com.alexber.eventmanager.entity.event;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record EventResponseDto(
        Long id,
        String name,
        Integer maxPlaces,
        OffsetDateTime date,
        Integer cost,
        Integer duration,
        Long ownerId,
        Long locationId,
        EventStatus status,
        Integer occupiedPlaces
) {
}
