package com.alexber.eventmanager.util.converter;

import com.alexber.eventmanager.entity.event.Event;
import com.alexber.eventmanager.entity.event.EventEntity;
import org.springframework.stereotype.Component;

@Component
public class EventEntityConverter {

    public Event toEvent(EventEntity eventEntity) {
        return new Event(
                eventEntity.getId(),
                eventEntity.getEventName(),
                eventEntity.getMaxPlaces(),
                eventEntity.getDate(),
                eventEntity.getCost(),
                eventEntity.getDuration(),
                eventEntity.getOwner().getId(),
                eventEntity.getLocation().getId(),
                eventEntity.getStatus(),
                eventEntity.getOccupiedPlaces()
        );
    }

    public EventEntity toEventEntity(Event event) {
        EventEntity eventEntity = new EventEntity();
        eventEntity.setEventName(event.name());
        eventEntity.setDuration(event.duration());
        eventEntity.setDate(event.date());
        eventEntity.setCost(event.cost());
        eventEntity.setMaxPlaces(event.maxPlaces());
        eventEntity.setStatus(event.status());
        return eventEntity;
    }
}
