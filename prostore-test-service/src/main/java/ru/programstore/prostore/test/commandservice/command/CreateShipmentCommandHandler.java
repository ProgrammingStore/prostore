package ru.programstore.prostore.test.commandservice.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.programstore.prostore.core.CommandHandler;
import ru.programstore.prostore.core.EventBus;
import ru.programstore.prostore.core.EventStore;
import ru.programstore.prostore.test.common.command.CreateShipmentCommand;
import ru.programstore.prostore.test.common.event.ShipmentCreatedEvent;

import java.util.UUID;

@Component
class CreateShipmentCommandHandler implements CommandHandler<CreateShipmentCommand> {
    private final Logger logger = LoggerFactory.getLogger(CreateShipmentCommandHandler.class);

    private final EventStore eventStore;
    private final EventBus eventBus;

    private CreateShipmentCommandHandler(EventStore eventStore, EventBus eventBus) {
        this.eventStore = eventStore;
        this.eventBus = eventBus;
    }

    @Override
    public String handle(CreateShipmentCommand command) {
        logger.debug("command = {}", command);
        ShipmentCreatedEvent event = new ShipmentCreatedEvent(
            UUID.randomUUID().toString(), command.getDestination(), command.getLocation()
        );
        eventStore.save(event);
        eventBus.publish(event);
        return String.format("created: %s", event.getAggregateId());
    }
}
