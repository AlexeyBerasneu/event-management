package com.alexber.eventmanager.util.updatestatus;

import com.alexber.eventmanager.entity.event.EventStatus;
import com.alexber.eventmanager.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UpdateEventStatus {

    private final Logger logger = LoggerFactory.getLogger(UpdateEventStatus.class);
    private final EventRepository eventRepository;

    public UpdateEventStatus(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Transactional
    @Scheduled(cron = "${event.stats.cron}")
    public void updateEventStatus() {
        logger.info("Updating event status");
        var startedEventIds = eventRepository.markEventsStarted(EventStatus.STARTED.name(), EventStatus.WAIT_START.name());
        var finishedEventIds = eventRepository.markEventsFinished(EventStatus.FINISHED.name(), EventStatus.STARTED.name());
        logger.info("Updated: started={}, finished={}", startedEventIds, finishedEventIds);
    }
}
