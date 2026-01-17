package com.example.userservice.service.kafka;

import com.example.userservice.event.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    @Value("${app.kafka.topic.user-events}")
    private String userEventsTopic;

    public void sendUserEvent(UserEvent userEvent) {
        try {
            kafkaTemplate.send(userEventsTopic, userEvent);
            log.info("Sent user event to Kafka: {}", userEvent);
        } catch (Exception e) {
            log.error("Failed to send user event to Kafka: {}", userEvent, e);
        }
    }
}