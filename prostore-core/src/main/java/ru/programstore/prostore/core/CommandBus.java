package ru.programstore.prostore.core;

import ru.programstore.prostore.core.impl.CommandResponse;

public interface CommandBus {
    CommandResponse send(String serviceName, Command command);
}
