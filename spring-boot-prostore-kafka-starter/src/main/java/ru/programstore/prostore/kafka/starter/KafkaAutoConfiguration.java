package ru.programstore.prostore.kafka.starter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.util.backoff.FixedBackOff;
import ru.programstore.prostore.core.EventBus;
import ru.programstore.prostore.core.JsonEventHandler;
import ru.programstore.prostore.kafka.KafkaEventBus;
import ru.programstore.prostore.kafka.ProstoreKafkaListener;

@EnableKafka
@Configuration
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public EventBus eventBus(KafkaTemplate<String, String> kafkaTemplate, KafkaProperties kafkaProperties) {
        return new KafkaEventBus(
            kafkaTemplate, kafkaProperties.getTopic(), new ObjectMapper()
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public ProstoreKafkaListener prostoreKafkaListener(JsonEventHandler jsonEventHandler) {
        return new ProstoreKafkaListener(jsonEventHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultErrorHandler errorHandler(KafkaOperations<Object, Object> template) {
        return new DefaultErrorHandler(
            new DeadLetterPublishingRecoverer(template), new FixedBackOff(1000L, 2)
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public RecordMessageConverter recordMessageConverter() {
        return new JsonMessageConverter();
    }

    @Bean
    @ConditionalOnMissingBean
    public NewTopic newTopic(KafkaProperties kafkaProperties) {
        return new NewTopic(
            kafkaProperties.getTopic(), kafkaProperties.getPartitions(), kafkaProperties.getReplicationFactor()
        );
    }
}
