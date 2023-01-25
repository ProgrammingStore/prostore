package ru.programstore.prostore.starter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.programstore.prostore.core.*;
import ru.programstore.prostore.core.impl.*;

import java.util.List;

@SuppressWarnings("rawtypes")
@Configuration
@EnableConfigurationProperties(ProstoreProperties.class)
public class ProstoreAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public JsonCommandHandler jsonCommandHandler(List<CommandHandler> commandHandlerList) {
        return new JsonCommandHandlerImpl(commandHandlerList, objectMapper());
    }

    @Bean
    @ConditionalOnMissingBean
    public JsonEventHandler jsonEventHandler(List<EventHandler> eventHandlerList) {
        return new JsonEventHandlerImpl(eventHandlerList, objectMapper());
    }

    @Bean
    @ConditionalOnMissingBean
    public JsonQueryHandler jsonQueryHandler(List<QueryHandler> queryHandlerList) {
        return new JsonQueryHandlerImpl(queryHandlerList, objectMapper());
    }

    @Bean
    @ConditionalOnMissingBean
    public ProstoreApplicationListener prostoreApplicationListener(EventStore eventStore, JsonEventHandler jsonEventHandler) {
        return new ProstoreApplicationListener(
            eventStore, new LocalEventBus(jsonEventHandler)
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public Cache cache() {
        return new WeakReferenceCache();
    }

    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
