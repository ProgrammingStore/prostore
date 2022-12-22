package ru.programstore.prostore.core.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import ru.programstore.prostore.core.Event;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JsonEvent {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private String type;
    private String json;

    public static JsonEvent from(Event event) {
        return new JsonEvent(
            event.getClass().getName(), asJsonString(event)
        );
    }

    private  static String asJsonString(Object o) {
        try {
            return OBJECT_MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
