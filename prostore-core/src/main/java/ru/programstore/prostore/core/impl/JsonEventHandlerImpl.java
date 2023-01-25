package ru.programstore.prostore.core.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.programstore.prostore.core.JsonEventHandler;
import ru.programstore.prostore.core.Event;
import ru.programstore.prostore.core.EventHandler;
import ru.programstore.prostore.core.util.ReflectionUtil;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("rawtypes")
public class JsonEventHandlerImpl implements JsonEventHandler {
    private final Logger logger = LoggerFactory.getLogger(JsonEventHandlerImpl.class);

    private final Map<String, Pair<Class, EventHandler>> cache;
    private final ObjectMapper objectMapper;

    public JsonEventHandlerImpl(List<EventHandler> eventHandlerList, ObjectMapper objectMapper) {
        this.cache = initCache(eventHandlerList);
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(JsonEvent jsonEvent) {
        logger.debug("Handling jsonEvent: {}", jsonEvent);
        if (!cache.containsKey(jsonEvent.getType())) {
            return;
        }
        Pair<Class, EventHandler> pair = cache.get(jsonEvent.getType());
        Class eventType = pair.getValue0();
        EventHandler eventHandler =  pair.getValue1();
        Event event = asEvent(jsonEvent.getJson(), eventType);
        eventHandler.handle(event);
    }

    private Event asEvent(String json, Class type) {
        try {
            return (Event) objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Pair<Class, EventHandler>> initCache(List<EventHandler> commandHandlerList) {
        return commandHandlerList.stream().collect(
            Collectors.toMap(
                o -> getCommandType(o).getTypeName(), o -> Pair.with((Class) getCommandType(o), o)
            )
        );
    }

    private Type getCommandType(EventHandler eventHandler) {
        return ReflectionUtil.getGenericType(eventHandler, 0);
    }
}
