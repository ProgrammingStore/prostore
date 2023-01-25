package ru.programstore.prostore.core.impl;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JsonCommand {
    private String type;
    private String json;
}
