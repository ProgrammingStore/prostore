package ru.programstore.prostore.mongo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.programstore.prostore.core.Cache;
import ru.programstore.prostore.core.impl.JsonEvent;
import ru.programstore.prostore.core.Aggregate;
import ru.programstore.prostore.core.AggregateEvent;
import ru.programstore.prostore.core.EventStore;

import java.util.List;
import java.util.stream.Collectors;

public class MongoEventStore implements EventStore {
    private final MongoEventRepository mongoEventRepository;
    private final ObjectMapper objectMapper;
    private final Cache cache;

    public MongoEventStore(MongoEventRepository mongoEventRepository, ObjectMapper objectMapper, Cache cache) {
        this.mongoEventRepository = mongoEventRepository;
        this.objectMapper = objectMapper;
        this.cache = cache;
    }

    @Override
    public void save(AggregateEvent event) {
        mongoEventRepository.save(
            new MongoEvent(event.getAggregateId(), event.getClass().getName(), asJsonString(event), System.nanoTime())
        );
    }

    @Override
    public void save(Aggregate aggregate) {
        cache.put(aggregate.getId(), aggregate);
    }

    @Override
    public <T extends Aggregate> T get(String aggregateId) {
        return cache.get(aggregateId);
    }

    @Override
    public List<JsonEvent> loadFrom(long timestamp) {
        return mongoEventRepository.findByTimestampGreaterThanOrderByTimestampAsc(timestamp).stream()
            .map(mongoEvent -> new JsonEvent(mongoEvent.getEventType(), mongoEvent.getEventJson()))
            .collect(Collectors.toList());
    }

    private String asJsonString(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
