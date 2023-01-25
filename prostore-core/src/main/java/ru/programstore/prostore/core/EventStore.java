package ru.programstore.prostore.core;

import ru.programstore.prostore.core.impl.JsonEvent;

import java.util.List;

public interface EventStore {
    void save(AggregateEvent event);

    void save(Aggregate aggregate);

    <T extends Aggregate> T get(String aggregateId);

    List<JsonEvent> loadFrom(long timestamp);
}
