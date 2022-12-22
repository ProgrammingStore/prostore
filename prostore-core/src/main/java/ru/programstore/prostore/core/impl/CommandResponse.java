package ru.programstore.prostore.core.impl;

import lombok.Data;

@Data
public class CommandResponse {
    private final boolean success;
    private final String message;
}
