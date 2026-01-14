package com.alexber.eventmanager.kafka.entity;

public record FieldChangeLong(
        Long oldField,
        Long newField
) {
}
