package ru.programstore.prostore.kafka.starter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "prostore.kafka")
public class KafkaProperties {
    private String topic = "events";
    private int partitions = 1;
    private short replicationFactor = 1;
}
