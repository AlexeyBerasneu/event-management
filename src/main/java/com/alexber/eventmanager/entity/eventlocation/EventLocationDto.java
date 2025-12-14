package com.alexber.eventmanager.entity.eventlocation;

import jakarta.validation.constraints.*;

public record EventLocationDto(
        @Null(message = "Id must be null")
        Long id,

        @NotBlank
        @Size(message = "Name must be no more then 150 letters",max = 150)
        String name,

        @NotBlank
        @Size(message = "Name must be no more then 150 letters",max = 150)
        String address,

        @NotNull
        @Min(message = "Capacity must be no less then 5 participates",value = 5)
        Integer capacity,

        @NotBlank
        String description
) {
}
