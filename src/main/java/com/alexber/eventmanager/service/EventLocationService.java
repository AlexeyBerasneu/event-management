package com.alexber.eventmanager.service;

import com.alexber.eventmanager.entity.eventlocation.EventLocation;
import com.alexber.eventmanager.entity.eventlocation.EventLocationEntity;
import com.alexber.eventmanager.repository.EventLocationRepository;
import com.alexber.eventmanager.util.converter.EventLocationEntityConverter;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventLocationService {

    private final Logger log = LoggerFactory.getLogger(EventLocationService.class);

    private final EventLocationRepository eventLocationRepository;
    private final EventLocationEntityConverter eventLocationEntityConverter;

    public EventLocationService(EventLocationRepository eventLocationRepository, EventLocationEntityConverter eventLocationEntityConverter) {
        this.eventLocationRepository = eventLocationRepository;
        this.eventLocationEntityConverter = eventLocationEntityConverter;
    }

    public List<EventLocation> findAllEventLocations() {
        log.debug("Fetching all event locations");
        return eventLocationRepository.findAll()
                .stream()
                .map(eventLocationEntityConverter::toDomain)
                .collect(Collectors.toList());
    }

    public EventLocation createLocation(EventLocation eventLocation) {
        log.info("Creating event location with name='{}'", eventLocation.name());
        if (eventLocationRepository.existsByName(eventLocation.name())) {
            throw new IllegalArgumentException("Event name already exists");
        }
        EventLocationEntity createdEventLocation = eventLocationRepository.save(eventLocationEntityConverter.toEntity(eventLocation));
        return eventLocationEntityConverter.toDomain(createdEventLocation);
    }

    public EventLocation getEventLocation(Long locationId) {
        log.debug("Fetching event location id={}", locationId);
        EventLocationEntity foundEventLocation = checkExistingEventLocation(locationId);
        return eventLocationEntityConverter.toDomain(foundEventLocation);
    }

    public void deleteEventLocation(Long locationId) {
        log.info("Deleting event location id={}", locationId);
        checkExistingEventLocation(locationId);
        eventLocationRepository.deleteById(locationId);
    }

    public EventLocation updateEventLocation(Long locationId, EventLocation eventLocationToUpdate) {
        log.info("Updating event location id={}", locationId);
        checkExistingEventLocation(locationId);
        EventLocationEntity toUpdate = eventLocationEntityConverter.toEntity(eventLocationToUpdate);
        toUpdate.setId(locationId);
        return eventLocationEntityConverter.toDomain(eventLocationRepository.save(toUpdate));
    }

    private EventLocationEntity checkExistingEventLocation(Long locationId) {
        return eventLocationRepository.findById(locationId).orElseThrow(
                () -> new EntityNotFoundException("Event location does not exist")
        );
    }
}
