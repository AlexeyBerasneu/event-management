package com.alexber.eventmanager.repository;

import com.alexber.eventmanager.entity.event.EventEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<EventEntity, Long> {
}
