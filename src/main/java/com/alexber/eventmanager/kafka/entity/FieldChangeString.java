package com.alexber.eventmanager.kafka.entity;

public record FieldChangeString(
        String oldField,
        String newField
) {
}
