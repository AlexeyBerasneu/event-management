package com.alexber.eventmanager.entity.event;

import com.alexber.eventmanager.util.datevalidation.IsoDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Positive;

import java.time.OffsetDateTime;

public record EventSearchRequestDto(
        String name,
        @Positive
        Integer durationMax,
        @Positive
        Integer durationMin,
        @Positive
        Integer costMin,
        @Positive
        Integer costMax,
        @Positive
        Integer placesMin,
        @Positive
        Integer placesMax,
        @Positive
        Long locationId,
        EventStatus eventStatus,
        @IsoDateTime
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS]XXX")
        OffsetDateTime dateStartBefore,
        @IsoDateTime
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS]XXX")
        OffsetDateTime dateStartAfter
) {
}
