package com.alexber.eventmanager.entity.user;

import jakarta.validation.constraints.NotBlank;

public record UserCredentials(
        @NotBlank
        String login,
        @NotBlank
        String password
) {
}
