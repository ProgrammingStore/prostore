package ru.programstore.prostore.test.common.model;

import lombok.*;
import ru.programstore.prostore.core.impl.AbstractAggregate;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class Shipment extends AbstractAggregate {
    private String destination;
    private String location;
}
