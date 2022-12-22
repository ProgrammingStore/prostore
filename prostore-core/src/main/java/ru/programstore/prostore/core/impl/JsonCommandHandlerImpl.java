package ru.programstore.prostore.core.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.programstore.prostore.core.Command;
import ru.programstore.prostore.core.CommandHandler;
import ru.programstore.prostore.core.JsonCommandHandler;
import ru.programstore.prostore.core.util.ReflectionUtil;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("rawtypes")
public class JsonCommandHandlerImpl implements JsonCommandHandler {
    private final Logger logger = LoggerFactory.getLogger(JsonCommandHandlerImpl.class);

    private final Map<String, Pair<Class, CommandHandler>> cache;
    private final ObjectMapper objectMapper;

    public JsonCommandHandlerImpl(List<CommandHandler> commandHandlerList, ObjectMapper objectMapper) {
        this.cache = initCache(commandHandlerList);
        this.objectMapper = objectMapper;
    }

    @Override
    public String handle(JsonCommand jsonCommand) {
        logger.debug("Handling jsonCommand: {}", jsonCommand);
        if (!cache.containsKey(jsonCommand.getType())) {
            throw new IllegalStateException(
                String.format("CommandHandler for given type does not exists: '%s'", jsonCommand.getType())
            );
        }
        Pair<Class, CommandHandler> pair = cache.get(jsonCommand.getType());
        Class commandType = pair.getValue0();
        CommandHandler commandHandler =  pair.getValue1();
        Command command = asCommand(jsonCommand.getJson(), commandType);
        return commandHandler.handle(command);
    }

    private Command asCommand(String json, Class type) {
        try {
            return (Command) objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Pair<Class, CommandHandler>> initCache(List<CommandHandler> commandHandlerList) {
        return commandHandlerList.stream().collect(
            Collectors.toMap(
                o -> getCommandType(o).getTypeName(), o -> Pair.with((Class) getCommandType(o), o)
            )
        );
    }

    private Type getCommandType(CommandHandler commandHandler) {
        return ReflectionUtil.getGenericType(commandHandler, 0);
    }
}
