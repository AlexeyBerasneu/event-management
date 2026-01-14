package com.alexber.eventmanager.kafka.entity;

public record FieldChangeDateTime(
        String oldField,
        String newField
) {
}
