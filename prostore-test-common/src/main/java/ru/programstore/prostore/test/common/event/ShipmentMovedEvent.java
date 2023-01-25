package ru.programstore.prostore.test.common.event;

import lombok.*;
import ru.programstore.prostore.core.AggregateEvent;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentMovedEvent implements AggregateEvent {
    private String aggregateId;
    private String location;
}
