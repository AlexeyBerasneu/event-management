package com.alexber.eventmanager.service;

import com.alexber.eventmanager.entity.event.*;
import com.alexber.eventmanager.entity.eventlocation.EventLocationEntity;
import com.alexber.eventmanager.entity.registration.RegistrationEntity;
import com.alexber.eventmanager.entity.user.User;
import com.alexber.eventmanager.entity.user.UserEntity;
import com.alexber.eventmanager.entity.user.UserRole;
import com.alexber.eventmanager.exception.customexception.NotAvailableEventException;
import com.alexber.eventmanager.exception.customexception.StatusEventException;
import com.alexber.eventmanager.kafka.entity.*;
import com.alexber.eventmanager.kafka.sender.EventSender;
import com.alexber.eventmanager.repository.EventLocationRepository;
import com.alexber.eventmanager.repository.EventRepository;
import com.alexber.eventmanager.repository.RegistrationRepository;
import com.alexber.eventmanager.repository.UserRepository;
import com.alexber.eventmanager.util.converter.EventDtoConverter;
import com.alexber.eventmanager.util.converter.EventEntityConverter;
import com.alexber.eventmanager.util.converter.UserEntityConverter;
import com.alexber.eventmanager.util.filter.EventSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventService {

    private final Logger logger = LoggerFactory.getLogger(EventService.class);
    private final EventLocationRepository eventLocationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RegistrationRepository registrationRepository;
    private final EventDtoConverter eventDtoConverter;
    private final EventEntityConverter eventEntityConverter;
    private final EventSender eventSender;
    private final UserEntityConverter userEntityConverter;

    public Event createEvent(Event event) {
        EventLocationEntity eventLocation = getEventLocationEntity(event.locationId());
        if (!(event.maxPlaces() <= eventLocation.getCapacity())) {
            throw new IllegalArgumentException("Event location is too small");
        } else {
            EventEntity eventEntity = eventEntityConverter.toEventEntity(event);
            setLocationAndOwner(event, eventEntity);
            eventRepository.save(eventEntity);
            logger.info("Event was created ");
            return eventEntityConverter.toEvent(eventEntity);
        }
    }

    public void deleteEventById(Long eventId, User user) {
        isAvailable(eventId, user);
        EventEntity eventEntity = getEventEntity(eventId);
        if (eventEntity.getStatus().equals(EventStatus.WAIT_START)) {
            eventEntity.setStatus(EventStatus.CANCELLED);
            logger.info("Event with id {} was soft deleted", eventId);
        } else {
            throw new NotAvailableEventException("Event already started or cancelled");
        }
    }

    public EventResponseDto getEventById(Long eventId) {
        EventEntity foundedEntity = getEventEntity(eventId);
        logger.info("Event with id {} was founded", eventId);
        return eventDtoConverter.toDto(foundedEntity);
    }

    public Event updateEvent(Event toUpdate, Long eventId, User user) {
        EventEntity event = isAvailable(eventId, user);
        //Сделал простую проверку на то, что если приходит Event с одинаковыми полями то мы ничего не делаем
        if (!isDifference(eventEntityConverter.toEvent(event), toUpdate)) {
            return eventEntityConverter.toEvent(event);
        }
        EventLocationEntity eventLocation = getEventLocationEntity(toUpdate.locationId());
        EventEntity oldEvent = deepCopy(eventId);
        if (toUpdate.maxPlaces() <= eventLocation.getCapacity() && event.getOccupiedPlaces() <= toUpdate.maxPlaces()) {
            event.setEventName(toUpdate.name());
            event.setMaxPlaces(toUpdate.maxPlaces());
            event.setDate(toUpdate.date());
            event.setCost(toUpdate.cost());
            event.setDuration(toUpdate.duration());
            setLocationAndOwner(toUpdate, event);
            eventRepository.save(event);
            KafkaEvent kafkaEvent = convertToKafka(oldEvent, event, user);
            eventSender.sendEvent(kafkaEvent);
            logger.info("Event with id {} was updated", eventId);
            return eventEntityConverter.toEvent(event);
        } else {
            throw new IllegalArgumentException("Max places are more then location capacity or registrations amount");
        }
    }

    public List<EventResponseDto> getAllEventsByUser(User user) {
        return eventRepository.findAll()
                .stream()
                .filter(eventEntity -> eventEntity.getOwner().getId().equals(user.id()))
                .map(eventDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void registration(Long eventId, Long userId) {
        EventEntity event = getEventEntity(eventId);
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " doesn't exist"));
        if (event.getStatus().equals(EventStatus.WAIT_START) || event.getStatus().equals(EventStatus.STARTED)) {
            RegistrationEntity registration = new RegistrationEntity(event, user);
            registrationRepository.save(registration);
            event.addRegistration(registration);
            user.addRegistration(registration);
            logger.info("Registration to event id = {} was created", eventId);
        } else {
            throw new StatusEventException("Event already finished or cancelled");
        }
    }

    @Transactional
    public void cancelRegistration(Long eventId, Long userId) {
        EventEntity event = getEventEntity(eventId);
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " doesn't exist"));
        if (event.getStatus().equals(EventStatus.WAIT_START)) {
            RegistrationEntity registration = registrationRepository.findByUser_IdAndEvent_Id(userId, eventId).orElseThrow(() -> new EntityNotFoundException("Registration for user with name = '" + user.getLogin() + "' doesn't exist"));
            registrationRepository.delete(registration);
            event.removeRegistration(registration);
            user.removeRegistration(registration);
            logger.info("Registration for event id = {} was canceled", eventId);
        } else {
            throw new StatusEventException("Event already  started or cancelled or finished");
        }
    }

    public List<Event> searchEventsWithParam(EventSearchRequestDto filter) {
        var eventSpecification = EventSpecification.byFilter(filter);
        return eventRepository.findAll(eventSpecification).stream().map(eventEntityConverter::toEvent).collect(Collectors.toList());
    }

    public List<EventResponseDto> getEventsByUserId(Long userId) {
        List<EventEntity> eventEntities = registrationRepository.findAllByUserId(userId);
        return eventEntities.stream().map(eventDtoConverter::toDto).collect(Collectors.toList());
    }

    private EventEntity setLocationAndOwner(Event event, EventEntity eventEntity) {
        //Can be used if we have feature for changing owner
        //UserEntity user = userRepository.findById(event.ownerId()).orElseThrow(() -> new EntityNotFoundException("User with id " + event.ownerId() + " doesn't exist"));
        EventLocationEntity eventLocation = getEventLocationEntity(event.locationId());
        eventEntity.setLocation(eventLocation);
        //Can be used if we have feature for changing owner
        //eventEntity.setOwner(user);
        return eventEntity;
    }

    private EventLocationEntity getEventLocationEntity(Long locationId) {
        return eventLocationRepository.findById(locationId).orElseThrow(() -> new EntityNotFoundException("Event with id " + locationId + " not found"));
    }

    private EventEntity isAvailable(Long eventId, User user) {
        EventEntity event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException("Event with id " + eventId + " doesn't exist"));
        if (user.role().equals(UserRole.ADMIN) || user.id().equals(event.getOwner().getId())) {
            return event;
        } else {
            throw new AccessDeniedException("User does not have permission to modify event with id = " + eventId);
        }
    }

    private EventEntity getEventEntity(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException("Event with id " + eventId + " doesn't exist"));
    }

    private KafkaEvent convertToKafka(EventEntity oldEvent, EventEntity newEvent, User user) {
        return new KafkaEvent(
                newEvent.getId(),
                user.id(),
                oldEvent.getOwner().getId(),
                new FieldChangeString(oldEvent.getEventName(), newEvent.getEventName()),
                new FieldChangeInteger(oldEvent.getMaxPlaces(), newEvent.getMaxPlaces()),
                new FieldChangeDateTime(oldEvent.getDate().toString(), newEvent.getDate().toString()),
                new FieldChangeInteger(oldEvent.getCost(), newEvent.getCost()),
                new FieldChangeInteger(oldEvent.getDuration(), newEvent.getDuration()),
                new FieldChangeLong(oldEvent.getLocation().getId(), newEvent.getLocation().getId()),
                oldEvent.getRegistrations().stream().map(en -> userEntityConverter.toVisitor(en.getUser())).collect(Collectors.toList())
        );
    }

    private Boolean isDifference(Event event, Event updatedEvent) {
        return !(event.name().trim().toLowerCase().equals(updatedEvent.name().trim().toLowerCase())
                && event.maxPlaces().equals(updatedEvent.maxPlaces())
                && event.date().equals(updatedEvent.date())
                && event.cost().equals(updatedEvent.cost())
                && event.duration().equals(updatedEvent.duration())
                && event.locationId().equals(updatedEvent.locationId()));
    }

    private EventEntity deepCopy(Long eventId) {
        EventEntity eventEntity = eventRepository.findFullForKafka(eventId).orElseThrow(() -> new EntityNotFoundException("Event with id " + eventId + " doesn't exist"));
        EventEntity event = new EventEntity(
                eventEntity.getId(),
                eventEntity.getEventName(),
                eventEntity.getOwner(),
                eventEntity.getLocation(),
                eventEntity.getMaxPlaces(),
                eventEntity.getOccupiedPlaces(),
                eventEntity.getDate(),
                eventEntity.getCost(),
                eventEntity.getDuration(),
                eventEntity.getStatus()
        );
        event.setLocation(eventEntity.getLocation());
        event.setRegistrations(eventEntity.getRegistrations());
        return event;
    }
}
