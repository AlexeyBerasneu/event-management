package com.alexber.eventmanager.util;

import com.alexber.eventmanager.entity.eventlocation.EventLocation;
import com.alexber.eventmanager.entity.eventlocation.EventLocationDto;
import org.springframework.stereotype.Component;

@Component
public class EventLocationDtoConverter {

    public EventLocationDto toDto(EventLocation eventLocation) {
        return new EventLocationDto(
                eventLocation.id(),
                eventLocation.name(),
                eventLocation.address(),
                eventLocation.capacity(),
                eventLocation.description()
        );
    }

    public EventLocation toDomain(EventLocationDto eventLocationDto){
        return new EventLocation(
                eventLocationDto.id(),
                eventLocationDto.name(),
                eventLocationDto.address(),
                eventLocationDto.capacity(),
                eventLocationDto.description()
        );
    }
}
