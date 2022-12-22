package ru.programstore.prostore.core;

import ru.programstore.prostore.core.impl.JsonQuery;

public interface JsonQueryHandler {
    Object handle(JsonQuery jsonQuery);
}
