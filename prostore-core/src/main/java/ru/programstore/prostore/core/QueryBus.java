package ru.programstore.prostore.core;

public interface QueryBus {
    Object send(String serviceName, Query query);
}
