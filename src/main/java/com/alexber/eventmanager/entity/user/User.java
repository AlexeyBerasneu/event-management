package com.alexber.eventmanager.entity.user;

public record User(
        Long id,
        String login,
        String password,
        Integer age,
        UserRole role
) {
}
