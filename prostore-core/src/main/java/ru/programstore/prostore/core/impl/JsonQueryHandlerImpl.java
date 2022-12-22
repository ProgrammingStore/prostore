package ru.programstore.prostore.core.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.programstore.prostore.core.JsonQueryHandler;
import ru.programstore.prostore.core.Query;
import ru.programstore.prostore.core.QueryHandler;
import ru.programstore.prostore.core.util.ReflectionUtil;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("rawtypes")
public class JsonQueryHandlerImpl implements JsonQueryHandler {
    private final Logger logger = LoggerFactory.getLogger(JsonQueryHandlerImpl.class);

    private final Map<String, Pair<Class, QueryHandler>> cache;
    private final ObjectMapper objectMapper;

    public JsonQueryHandlerImpl(List<QueryHandler> queryHandlerList, ObjectMapper objectMapper) {
        this.cache = initCache(queryHandlerList);
        this.objectMapper = objectMapper;
    }

    @Override
    public Object handle(JsonQuery jsonQuery) {
        logger.debug("Handling jsonQuery: {}", jsonQuery);
        if (!cache.containsKey(jsonQuery.getType())) {
            throw new IllegalStateException(
                String.format("QueryHandler for given type does not exists: '%s'", jsonQuery.getType())
            );
        }
        Pair<Class, QueryHandler> pair = cache.get(jsonQuery.getType());
        Class queryType = pair.getValue0();
        QueryHandler queryHandler =  pair.getValue1();
        Query query = asQuery(jsonQuery.getJson(), queryType);
        return queryHandler.handle(query);
    }

    private Query asQuery(String json, Class type) {
        try {
            return (Query) objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Pair<Class, QueryHandler>> initCache(List<QueryHandler> queryHandlerList) {
        return queryHandlerList.stream().collect(
            Collectors.toMap(
                o -> getQueryType(o).getTypeName(), o -> Pair.with((Class) getQueryType(o), o)
            )
        );
    }

    private Type getQueryType(QueryHandler queryHandler) {
        return ReflectionUtil.getGenericType(queryHandler, 0);
    }
}
