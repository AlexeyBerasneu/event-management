package com.alexber.eventmanager.kafka.entity;

public record Visitor(
        Long id,
        String login,
        Integer age
) {
}
