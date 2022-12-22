package ru.programstore.prostore.mongo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongoEventRepository extends CrudRepository<MongoEvent, String> {
    List<MongoEvent> findByTimestampGreaterThanOrderByTimestampAsc(long timestamp);
}
