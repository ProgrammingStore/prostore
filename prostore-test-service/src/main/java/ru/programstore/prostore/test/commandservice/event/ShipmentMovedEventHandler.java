package ru.programstore.prostore.test.commandservice.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.programstore.prostore.core.EventHandler;
import ru.programstore.prostore.core.EventStore;
import ru.programstore.prostore.test.common.event.ShipmentMovedEvent;
import ru.programstore.prostore.test.common.model.Shipment;

@Component
class ShipmentMovedEventHandler implements EventHandler<ShipmentMovedEvent> {
    private final Logger logger = LoggerFactory.getLogger(ShipmentMovedEventHandler.class);

    private final EventStore eventStore;

    private ShipmentMovedEventHandler(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public void handle(ShipmentMovedEvent event) {
        logger.debug("Got event: {}", event);
        Shipment shipment = eventStore.get(event.getAggregateId());
        shipment.setLocation(event.getLocation());
    }
}
