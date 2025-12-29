package com.alexber.eventmanager.entity.event;

import com.alexber.eventmanager.util.datevalidation.IsoDateTime;
import jakarta.validation.constraints.*;

public record EventCreateRequestDto(

        @NotBlank(message = "Event name must not be empty")
        String name,

        @NotNull(message = "Max places is required")
        @Positive(message = "Max places must be greater than zero")
        Integer maxPlaces,

        @NotBlank(message = "Event date is required")
        @IsoDateTime(message = "Event date must be in ISO-8601 format and not in the past")
        String date,

        @NotNull(message = "Event cost is required")
        @PositiveOrZero(message = "Event cost must be zero or a positive number")
        Integer cost,

        @NotNull(message = "Event duration is required")
        @Positive(message = "Event duration must be greater than zero")
        Integer duration,

        @NotNull(message = "Location id is required")
        @Positive(message = "Location id must be a positive number")
        Long locationId
) {
}

