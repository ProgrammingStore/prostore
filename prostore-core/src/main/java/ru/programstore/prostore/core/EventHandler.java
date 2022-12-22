package ru.programstore.prostore.core;

public interface EventHandler<T extends Event> {
    void handle(T event);
}
