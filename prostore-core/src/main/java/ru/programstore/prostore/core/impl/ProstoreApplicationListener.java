package ru.programstore.prostore.core.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import ru.programstore.prostore.core.EventBus;
import ru.programstore.prostore.core.EventStore;

import java.util.List;

@RequiredArgsConstructor
public class ProstoreApplicationListener implements ApplicationListener<ApplicationReadyEvent> {
    private final Logger logger = LoggerFactory.getLogger(ProstoreApplicationListener.class);

    private final EventStore eventStore;
    private final EventBus eventBus;

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        logger.info("Loading events...");
        List<JsonEvent> events = eventStore.loadFrom(0L);
        logger.info("Events size = {}", events.size());
        for (JsonEvent e : events) {
            eventBus.publish(e);
        }
        logger.info("Loading events: done");
    }
}
