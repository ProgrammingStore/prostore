package ru.programstore.prostore.core;

public interface EntryListener {
    void onEntryExpired(Object key);

    void onEntryRemoved(Object key);

    void onEntryUpdated(Object key, Object value);

    void onEntryCreated(Object key, Object value);

    void onEntryRead(Object key, Object value);

    Object clone() throws CloneNotSupportedException;
}
