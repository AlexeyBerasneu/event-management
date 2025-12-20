package com.alexber.eventmanager.entity.event;

import com.alexber.eventmanager.util.datevalidation.IsoDateTime;
import jakarta.validation.constraints.*;

public record EventCreateRequestDto(

        @NotBlank
        String name,
        @NotNull
        @Positive
        Integer maxPlaces,
        @NotBlank
        @IsoDateTime
        String date,
        @NotNull
        @PositiveOrZero
        Integer cost,
        @NotNull
        @Positive
        Integer duration,
        @NotNull
        @Positive
        Long locationId
) {
}
