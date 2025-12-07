package com.alexber.eventmanager.service;

import com.alexber.eventmanager.entity.EventLocation;
import com.alexber.eventmanager.entity.EventLocationEntity;
import com.alexber.eventmanager.repository.EventLocationRepository;
import com.alexber.eventmanager.util.EventLocationEntityConverter;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventLocationService {

    private final EventLocationRepository eventLocationRepository;
    private final EventLocationEntityConverter eventLocationEntityConverter;

    public EventLocationService(EventLocationRepository eventLocationRepository, EventLocationEntityConverter eventLocationEntityConverter) {
        this.eventLocationRepository = eventLocationRepository;
        this.eventLocationEntityConverter = eventLocationEntityConverter;
    }

    public List<EventLocation> findAllEventLocations() {
        return eventLocationRepository.findAll()
                .stream()
                .map(eventLocationEntityConverter::toDomain)
                .collect(Collectors.toList());
    }

    public EventLocation createLocation(EventLocation eventLocation) {
        if (eventLocationRepository.existsByName(eventLocation.name())) {
            throw new IllegalArgumentException("Event name already exists");
        }
        EventLocationEntity createdEventLocation = eventLocationRepository.save(eventLocationEntityConverter.toEntity(eventLocation));
        return eventLocationEntityConverter.toDomain(createdEventLocation);
    }

    public EventLocation getEventLocation(Long locationId) {
        EventLocationEntity foundEventLocaton = checkExistingEventLocation(locationId);
        return eventLocationEntityConverter.toDomain(foundEventLocaton);
    }

    public void deleteEventLocation(Long locationId) {
        checkExistingEventLocation(locationId);
        eventLocationRepository.deleteById(locationId);
    }

    public EventLocation updateEventLocation(Long locationId, EventLocation eventLocationToUpdate) {
        checkExistingEventLocation(locationId);
        EventLocationEntity toUpdate = eventLocationEntityConverter.toEntity(eventLocationToUpdate);
        toUpdate.setId(locationId);
        return eventLocationEntityConverter.toDomain(eventLocationRepository.save(toUpdate));
    }

    public EventLocationEntity checkExistingEventLocation(Long locationId) {
        return eventLocationRepository.findById(locationId).orElseThrow(
                () -> new EntityNotFoundException("Event location does not exist")
        );
    }
}
