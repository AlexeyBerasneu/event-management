package com.alexber.eventmanager.kafka.sender;

import com.alexber.eventmanager.kafka.entity.KafkaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventSender {

    private final Logger logger = LoggerFactory.getLogger(EventSender.class);
    private final KafkaTemplate<Long, KafkaEvent> kafkaTemplate;

    public EventSender(KafkaTemplate<Long, KafkaEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(KafkaEvent kafkaEvent) {
        logger.info("Sending event: {}", kafkaEvent);
        kafkaTemplate.send("events-topic", kafkaEvent.eventId(), kafkaEvent);
    }
}
