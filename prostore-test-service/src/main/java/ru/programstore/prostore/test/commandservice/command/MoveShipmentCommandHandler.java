package ru.programstore.prostore.test.commandservice.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.programstore.prostore.core.CommandHandler;
import ru.programstore.prostore.core.EventBus;
import ru.programstore.prostore.core.EventStore;
import ru.programstore.prostore.test.common.command.MoveShipmentCommand;
import ru.programstore.prostore.test.common.event.ShipmentMovedEvent;

@Component
class MoveShipmentCommandHandler implements CommandHandler<MoveShipmentCommand> {
    private final Logger logger = LoggerFactory.getLogger(MoveShipmentCommandHandler.class);

    private final EventStore eventStore;
    private final EventBus eventBus;

    private MoveShipmentCommandHandler(EventStore eventStore, EventBus eventBus) {
        this.eventStore = eventStore;
        this.eventBus = eventBus;
    }

    @Override
    public String handle(MoveShipmentCommand command) {
        logger.debug("command = {}", command);
        ShipmentMovedEvent event = new ShipmentMovedEvent(command.getAggregateId(), command.getLocation());
        eventStore.save(event);
        eventBus.publish(event);
        return String.format("moved: %s", event.getAggregateId());
    }
}
