package ru.programstore.prostore.test.commandservice.controller;

import org.springframework.web.bind.annotation.*;
import ru.programstore.prostore.core.CommandBus;
import ru.programstore.prostore.core.impl.CommandResponse;
import ru.programstore.prostore.core.QueryBus;
import ru.programstore.prostore.test.commandservice.properties.AppProperties;
import ru.programstore.prostore.test.common.command.CreateShipmentCommand;
import ru.programstore.prostore.test.common.command.MoveShipmentCommand;
import ru.programstore.prostore.test.common.query.GetShipmentByIdQuery;

@RestController
@RequestMapping("/shipment")
class ShipmentController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;
    private final AppProperties appProperties;

    private ShipmentController(CommandBus commandBus, QueryBus queryBus, AppProperties appProperties) {
        this.commandBus = commandBus;
        this.queryBus = queryBus;
        this.appProperties = appProperties;
    }

    @PostMapping
    private CommandResponse create(@RequestBody CreateShipmentCommand command) {
        try {
            return commandBus.send(appProperties.getTestServiceName(), command);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommandResponse(
                false, e.getMessage()
            );
        }
    }

    @PutMapping("/move")
    private CommandResponse move(@RequestBody MoveShipmentCommand command) {
        try {
            return commandBus.send(appProperties.getTestServiceName(), command);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommandResponse(
                false, e.getMessage()
            );
        }
    }

    @GetMapping
    private Object getShipmentById(@RequestBody GetShipmentByIdQuery query) {
        try {
            return queryBus.send(appProperties.getTestServiceName(), query);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
