package ru.programstore.prostore.core.impl;

import ru.programstore.prostore.core.EntryListener;

public class EntryListenerAdapter implements EntryListener {
    @Override
    public void onEntryExpired(Object key) {
    }

    @Override
    public void onEntryRemoved(Object key) {
    }

    @Override
    public void onEntryUpdated(Object key, Object value) {
    }

    @Override
    public void onEntryCreated(Object key, Object value) {
    }

    @Override
    public void onEntryRead(Object key, Object value) {
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
