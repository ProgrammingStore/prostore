package ru.programstore.prostore.core;

public interface CommandHandler<T extends Command> {
    String handle(T command);
}
