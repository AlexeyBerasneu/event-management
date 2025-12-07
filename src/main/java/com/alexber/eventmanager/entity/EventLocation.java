package com.alexber.eventmanager.entity;

public record EventLocation(
        Long id,
        String name,
        String address,
        Integer capacity,
        String description
) {
}
