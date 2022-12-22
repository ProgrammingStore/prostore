package ru.programstore.prostore.eureka;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.programstore.prostore.core.impl.JsonCommand;
import ru.programstore.prostore.core.JsonCommandHandler;

@RestController
@RequestMapping("/prostore/command")
public class EurekaCommandListener {
    private final JsonCommandHandler jsonCommandHandler;

    public EurekaCommandListener(JsonCommandHandler jsonCommandHandler) {
        this.jsonCommandHandler = jsonCommandHandler;
    }

    @PostMapping
    private String receiveCommand(@RequestBody JsonCommand jsonCommand) {
        return jsonCommandHandler.handle(jsonCommand);
    }
}
