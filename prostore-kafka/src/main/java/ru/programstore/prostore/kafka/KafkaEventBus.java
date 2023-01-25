package ru.programstore.prostore.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import ru.programstore.prostore.core.impl.JsonEvent;
import ru.programstore.prostore.core.Event;
import ru.programstore.prostore.core.EventBus;

public class KafkaEventBus implements EventBus {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic;
    private final ObjectMapper objectMapper;

    public KafkaEventBus(KafkaTemplate<String, String> kafkaTemplate, String topic, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(Event event) {
        publish(new JsonEvent(event.getClass().getName(), asJsonString(event)));
    }

    @Override
    public void publish(JsonEvent event) {
        kafkaTemplate.send(topic, asJsonString(event));
    }

    private String asJsonString(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
