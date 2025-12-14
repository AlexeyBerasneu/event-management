package com.alexber.eventmanager.entity.eventlocation;

import jakarta.validation.constraints.*;

public record EventLocationDto(
        @Null
        Long id,

        @NotBlank
        @Size(max = 150)
        String name,

        @NotBlank
        @Size(max = 150)
        String address,

        @NotNull
        @Min(5)
        Integer capacity,

        String description
) {
}
