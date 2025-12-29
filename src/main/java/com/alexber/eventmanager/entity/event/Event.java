package com.alexber.eventmanager.entity.event;

import java.time.OffsetDateTime;

public record Event(
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
