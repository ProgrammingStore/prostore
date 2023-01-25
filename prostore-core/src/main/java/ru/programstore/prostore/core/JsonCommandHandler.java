package ru.programstore.prostore.core;

import ru.programstore.prostore.core.impl.JsonCommand;

public interface JsonCommandHandler {
    String handle(JsonCommand jsonCommand);
}
