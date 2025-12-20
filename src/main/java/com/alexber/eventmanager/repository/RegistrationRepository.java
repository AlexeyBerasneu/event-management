package com.alexber.eventmanager.repository;

import com.alexber.eventmanager.entity.event.EventEntity;
import com.alexber.eventmanager.entity.registration.RegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Long> {
    Optional<RegistrationEntity> findByUser_IdAndEvent_Id(Long userId, Long eventId);

    @Query("""
            select distinct r.event from RegistrationEntity r 
                        join r.event e
                                    left join fetch e.location
                                    where r.user.id = :userId
            """)
    List<EventEntity> findAllByUserId(@Param("userId") Long userId);
}
