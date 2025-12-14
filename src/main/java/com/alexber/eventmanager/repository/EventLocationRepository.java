package com.alexber.eventmanager.repository;

import com.alexber.eventmanager.entity.eventlocation.EventLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventLocationRepository extends JpaRepository<EventLocationEntity, Long> {
    boolean existsByName(String name);
}
