package com.alexber.eventmanager.kafka.config;

import com.alexber.eventmanager.kafka.entity.KafkaEvent;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfig {

    @Bean
    public KafkaTemplate<Long, KafkaEvent> kafkaTemplate(KafkaProperties kafkaProperties) {
        var props = kafkaProperties.buildProducerProperties();
        ProducerFactory<Long, KafkaEvent> producerFactory = new DefaultKafkaProducerFactory<>(props);
        return new KafkaTemplate<>(producerFactory);
    }
}
