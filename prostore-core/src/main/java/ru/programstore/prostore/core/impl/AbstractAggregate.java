package ru.programstore.prostore.core.impl;

import ru.programstore.prostore.core.Aggregate;

public abstract class AbstractAggregate implements Aggregate {
    private String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
