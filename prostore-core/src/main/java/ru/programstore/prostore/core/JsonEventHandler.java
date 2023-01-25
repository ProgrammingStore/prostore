package ru.programstore.prostore.core;

import ru.programstore.prostore.core.impl.JsonEvent;

public interface JsonEventHandler {
    void handle(JsonEvent jsonEvent);
}
