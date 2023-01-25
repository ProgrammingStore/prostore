package ru.programstore.prostore.core.impl;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JsonQuery {
    private String type;
    private String json;
}
