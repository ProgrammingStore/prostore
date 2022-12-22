package ru.programstore.prostore.mongo;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MongoEvent {
    private String aggregateId;
    private String eventType;
    private String eventJson;
    private Long timestamp;
}
