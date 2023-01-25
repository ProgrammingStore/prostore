package ru.programstore.prostore.core.impl;

import lombok.RequiredArgsConstructor;
import ru.programstore.prostore.core.Event;
import ru.programstore.prostore.core.EventBus;
import ru.programstore.prostore.core.JsonEventHandler;

@RequiredArgsConstructor
public class LocalEventBus implements EventBus {
    private final JsonEventHandler jsonEventHandler;

    @Override
    public void publish(Event event) {
        publish(JsonEvent.from(event));
    }

    @Override
    public void publish(JsonEvent event) {
        jsonEventHandler.handle(event);
    }
}
