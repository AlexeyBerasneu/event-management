package com.alexber.eventmanager.exception;

import java.time.LocalDateTime;

public record ServerErrorDto(
        String message,
        String detailMessage,
        LocalDateTime localDateTime
) {
}
