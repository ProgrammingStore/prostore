package ru.programstore.prostore.core;

public interface QueryHandler<T extends Query> {
    Object handle(T query);
}
