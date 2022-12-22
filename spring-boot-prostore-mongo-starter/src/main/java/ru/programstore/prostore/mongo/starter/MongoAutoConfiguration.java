package ru.programstore.prostore.mongo.starter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import ru.programstore.prostore.core.Cache;
import ru.programstore.prostore.core.EventStore;
import ru.programstore.prostore.mongo.MongoEventRepository;
import ru.programstore.prostore.mongo.MongoEventStore;

@Configuration
@EnableConfigurationProperties(MongoProperties.class)
@EnableMongoRepositories(basePackageClasses = MongoEventStore.class)
public class MongoAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public EventStore eventStore(MongoEventRepository mongoEventRepository, Cache cache) {
        return new MongoEventStore(mongoEventRepository, new ObjectMapper(), cache);
    }
}
