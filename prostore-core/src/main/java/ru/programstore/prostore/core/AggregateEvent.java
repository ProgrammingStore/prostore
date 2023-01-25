package ru.programstore.prostore.core;

public interface AggregateEvent extends Event {
    String getAggregateId();
}
