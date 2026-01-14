package com.alexber.eventmanager.kafka.entity;

import java.util.List;

public record KafkaEvent(
        Long eventId,
        Long changedBy,
        Long ownerId,
        FieldChangeString name,
        FieldChangeInteger maxPlaces,
        FieldChangeDateTime date,
        FieldChangeInteger cost,
        FieldChangeInteger duration,
        FieldChangeLong locationId,
        List<Visitor> visitors
) {
}
