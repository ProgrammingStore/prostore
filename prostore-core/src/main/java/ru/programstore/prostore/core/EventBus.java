package ru.programstore.prostore.core;

import ru.programstore.prostore.core.impl.JsonEvent;

public interface EventBus {
    void publish(Event event);

    void publish(JsonEvent event);
}
