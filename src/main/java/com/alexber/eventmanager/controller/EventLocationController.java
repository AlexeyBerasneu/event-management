package com.alexber.eventmanager.controller;

import com.alexber.eventmanager.entity.eventlocation.EventLocation;
import com.alexber.eventmanager.entity.eventlocation.EventLocationDto;
import com.alexber.eventmanager.service.EventLocationService;
import com.alexber.eventmanager.util.EventLocationDtoConverter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/locations")
public class EventLocationController {

    private final Logger logger = LoggerFactory.getLogger(EventLocationController.class);
    private final EventLocationService eventLocationService;
    private final EventLocationDtoConverter eventLocationDtoConverter;

    public EventLocationController(EventLocationService eventLocationService, EventLocationDtoConverter eventLocationDtoConverter) {
        this.eventLocationService = eventLocationService;
        this.eventLocationDtoConverter = eventLocationDtoConverter;
    }

    @GetMapping
    public List<EventLocationDto> getAllEventLocations() {
        logger.info("Get all locations");
        return eventLocationService.findAllEventLocations()
                .stream()
                .map(eventLocationDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<EventLocationDto> createEventLocation(@RequestBody
                                                                @Valid
                                                                EventLocationDto eventLocationDto) {
        logger.info("Create new location");
        EventLocation createdEventLocation = eventLocationService.createLocation(eventLocationDtoConverter.toDomain(eventLocationDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(eventLocationDtoConverter.toDto(createdEventLocation));
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<EventLocationDto> getEventLocation(@PathVariable
                                                             @NotNull
                                                             @Positive
                                                             Long locationId) {
        logger.info("Get location by id");
        EventLocation foundEventLocation = eventLocationService.getEventLocation(locationId);
        return ResponseEntity.ok(eventLocationDtoConverter.toDto(foundEventLocation));
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<Void> deleteEventLocation(@PathVariable
                                                    @NotNull
                                                    @Positive
                                                    Long locationId) {
        logger.info("Delete location");
        eventLocationService.deleteEventLocation(locationId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{locationId}")
    public ResponseEntity<EventLocationDto> updateEventLocation(@PathVariable
                                                                @NotNull
                                                                @Positive
                                                                Long locationId,
                                                                @RequestBody
                                                                @Valid
                                                                EventLocationDto eventLocationDto

    ) {
        logger.info("Update location");
        EventLocation updatedEventLocation = eventLocationService.updateEventLocation(locationId, eventLocationDtoConverter.toDomain(eventLocationDto));
        return ResponseEntity.ok(eventLocationDtoConverter.toDto(updatedEventLocation));
    }
}
