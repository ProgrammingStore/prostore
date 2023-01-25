package ru.programstore.prostore.mongo.starter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "prostore.mongo")
public class MongoProperties {
}
