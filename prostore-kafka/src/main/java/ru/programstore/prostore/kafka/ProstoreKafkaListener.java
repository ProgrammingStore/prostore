package ru.programstore.prostore.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.annotation.KafkaListener;
import ru.programstore.prostore.core.impl.JsonEvent;
import ru.programstore.prostore.core.JsonEventHandler;

public class ProstoreKafkaListener {
    private final Logger logger = LoggerFactory.getLogger(ProstoreKafkaListener.class);

    private final TaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
    private final JsonEventHandler jsonEventHandler;

    public ProstoreKafkaListener(JsonEventHandler jsonEventHandler) {
        this.jsonEventHandler = jsonEventHandler;
    }

    @KafkaListener(topics = "#{'${prostore.kafka.topic:events}'}")
    public void listen(JsonEvent jsonEvent) {
        logger.debug("Got jsonEvent: {}", jsonEvent);
        taskExecutor.execute(() -> jsonEventHandler.handle(jsonEvent));
    }
}
