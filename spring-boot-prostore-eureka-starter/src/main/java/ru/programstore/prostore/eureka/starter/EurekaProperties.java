package ru.programstore.prostore.eureka.starter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "prostore.eureka")
public class EurekaProperties {
}
