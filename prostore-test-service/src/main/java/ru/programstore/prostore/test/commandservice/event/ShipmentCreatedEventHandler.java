package ru.programstore.prostore.test.commandservice.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.programstore.prostore.core.EventHandler;
import ru.programstore.prostore.core.EventStore;
import ru.programstore.prostore.test.common.event.ShipmentCreatedEvent;
import ru.programstore.prostore.test.common.model.Shipment;

@Component
class ShipmentCreatedEventHandler implements EventHandler<ShipmentCreatedEvent> {
    private final Logger logger = LoggerFactory.getLogger(ShipmentCreatedEventHandler.class);

    private final EventStore eventStore;

    private ShipmentCreatedEventHandler(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public void handle(ShipmentCreatedEvent event) {
        logger.debug("Got event: {}", event);
        Shipment shipment = new Shipment();
        shipment.setId(event.getAggregateId());
        shipment.setDestination(event.getDestination());
        shipment.setLocation(event.getLocation());
        eventStore.save(shipment);
    }
}
