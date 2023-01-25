package ru.programstore.prostore.core;

@FunctionalInterface
public interface Registration extends AutoCloseable {

    @Override
    default void close() {
        cancel();
    }

    boolean cancel();
}