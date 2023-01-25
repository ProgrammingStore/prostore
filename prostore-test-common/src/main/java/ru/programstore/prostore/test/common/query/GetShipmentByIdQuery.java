package ru.programstore.prostore.test.common.query;

import lombok.*;
import ru.programstore.prostore.core.Query;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GetShipmentByIdQuery implements Query {
    private String aggregateId;
}
