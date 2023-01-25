package ru.programstore.prostore.test.commandservice.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.programstore.prostore.core.EventStore;
import ru.programstore.prostore.core.QueryHandler;
import ru.programstore.prostore.test.common.model.Shipment;
import ru.programstore.prostore.test.common.query.GetShipmentByIdQuery;

@Component
@RequiredArgsConstructor
class GetShipmentsQueryHandler implements QueryHandler<GetShipmentByIdQuery> {
    private final EventStore eventStore;

    @Override
    public Object handle(GetShipmentByIdQuery query) {
        return eventStore.<Shipment>get(query.getAggregateId());
    }
}
