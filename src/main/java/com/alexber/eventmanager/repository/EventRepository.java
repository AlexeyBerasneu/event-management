package com.alexber.eventmanager.repository;

import com.alexber.eventmanager.entity.event.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<EventEntity, Long>, JpaSpecificationExecutor<EventEntity> {

    @Modifying
    @Query(value = """
            update events e set status= :newStatus where e.status = :oldStatus and e.date<=now() 
            """, nativeQuery = true)
    int markEventsStarted(@Param("newStatus") String newStatus, @Param("oldStatus") String oldStatus);

    @Modifying
    @Query(value = """
            update events e set status = :newStatus
            where e.status = :oldStatus and (e.date+(e.duration* INTERVAL '1 minute'))<=now()
            """, nativeQuery = true
    )
    int markEventsFinished(@Param("newStatus") String newStatus, @Param("oldStatus") String oldStatus);

}
