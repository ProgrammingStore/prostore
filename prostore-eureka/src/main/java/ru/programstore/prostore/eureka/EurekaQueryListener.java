package ru.programstore.prostore.eureka;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.programstore.prostore.core.impl.JsonQuery;
import ru.programstore.prostore.core.JsonQueryHandler;

@RestController
@RequestMapping("/prostore/query")
public class EurekaQueryListener {
    private final JsonQueryHandler jsonQueryHandler;

    public EurekaQueryListener(JsonQueryHandler jsonQueryHandler) {
        this.jsonQueryHandler = jsonQueryHandler;
    }

    @PostMapping
    private Object receiveQuery(@RequestBody JsonQuery jsonQuery) {
        return jsonQueryHandler.handle(jsonQuery);
    }
}
