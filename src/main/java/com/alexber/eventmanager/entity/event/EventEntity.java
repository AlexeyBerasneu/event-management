package com.alexber.eventmanager.entity.event;

import com.alexber.eventmanager.entity.eventlocation.EventLocationEntity;
import com.alexber.eventmanager.entity.registration.RegistrationEntity;
import com.alexber.eventmanager.entity.user.UserEntity;
import com.alexber.eventmanager.exception.customexception.AmountRegistrationException;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Event name must not be empty")
    @Column(name = "event_name")
    private String eventName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private EventLocationEntity location;

    @NotNull(message = "Max places is required")
    @Positive(message = "Max places must be greater than zero")
    @Column(name = "max_places", nullable = false)
    private Integer maxPlaces;

    @NotNull
    @PositiveOrZero
    @Column(name = "occupied_places", nullable = false)
    private Integer occupiedPlaces = 0;

    @NotNull(message = "Event date is required")
    @FutureOrPresent(message = "Event date must be in the present or future")
    private OffsetDateTime date;

    @NotNull(message = "Event cost is required")
    @Min(value = 0, message = "Event cost must be zero or a positive number")
    private Integer cost;

    @NotNull(message = "Event duration is required")
    @Min(value = 30, message = "Event duration must be at least 30 minutes")
    private Integer duration;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<RegistrationEntity> registrations = new ArrayList<>();

    public EventEntity() {
    }

    public EventEntity(Long id, String eventName, UserEntity owner, EventLocationEntity location, Integer maxPlaces, Integer occupiedPlaces, OffsetDateTime date, Integer cost, Integer duration, EventStatus status) {
        this.id = id;
        this.eventName = eventName;
        this.owner = owner;
        this.location = location;
        this.maxPlaces = maxPlaces;
        this.occupiedPlaces = occupiedPlaces;
        this.date = date;
        this.cost = cost;
        this.duration = duration;
        this.status = status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public EventLocationEntity getLocation() {
        return location;
    }

    public void setLocation(EventLocationEntity location) {
        this.location = location;
    }

    public Integer getMaxPlaces() {
        return maxPlaces;
    }

    public void setMaxPlaces(Integer maxPlaces) {
        this.maxPlaces = maxPlaces;
    }

    public Integer getOccupiedPlaces() {
        return occupiedPlaces;
    }

    private void increaseOccupiedPlaces() {
        this.occupiedPlaces++;
    }

    private void decreaseOccupiedPlaces() {
        this.occupiedPlaces--;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public List<RegistrationEntity> getRegistrations() {
        return registrations;
    }

    public void addRegistration(RegistrationEntity registration) {
        if (occupiedPlaces >= maxPlaces) {
            throw new IllegalStateException("No free places");
        }
        registrations.add(registration);
        registration.setEvent(this);
        increaseOccupiedPlaces();
    }

    public void removeRegistration(RegistrationEntity registration) {
        if (!registrations.contains(registration)) {
            throw new AmountRegistrationException("Event does not have any registrations");
        }
        registrations.remove(registration);
        decreaseOccupiedPlaces();
    }
}
