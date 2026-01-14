package com.alexber.eventmanager.kafka.entity;

public record FieldChangeInteger(
        Integer oldField,
        Integer newField
) {
}
