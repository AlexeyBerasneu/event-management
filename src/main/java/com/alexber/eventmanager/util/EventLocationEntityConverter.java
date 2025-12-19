package com.alexber.eventmanager.util;

import com.alexber.eventmanager.entity.eventlocation.EventLocation;
import com.alexber.eventmanager.entity.eventlocation.EventLocationEntity;
import org.springframework.stereotype.Component;

@Component
public class EventLocationEntityConverter {

    public EventLocationEntity toEntity(EventLocation eventLocation) {
        return new EventLocationEntity(
                eventLocation.id(),
                eventLocation.name(),
                eventLocation.address(),
                eventLocation.capacity(),
                eventLocation.description()
        );
    }

    public EventLocation toDomain(EventLocationEntity EventLocationEntity) {
        return new EventLocation(
                EventLocationEntity.getId(),
                EventLocationEntity.getName(),
                EventLocationEntity.getAddress(),
                EventLocationEntity.getCapacity(),
                EventLocationEntity.getDescription()
        );
    }
}
