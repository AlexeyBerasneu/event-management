package com.alexber.eventmanager.entity.user;

public record UserDto(
        Long id,
        String login,
        Integer age,
        UserRole role
) {
}
