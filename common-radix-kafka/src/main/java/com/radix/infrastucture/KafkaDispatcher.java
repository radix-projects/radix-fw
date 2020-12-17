package com.radix.infrastucture;

import lombok.extern.java.Log;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Log
@Component
public class KafkaDispatcher<T> {

    private final KafkaTemplate<String, Message<T>> kafkaTemplate;

    public KafkaDispatcher(KafkaTemplate<String, Message<T>> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String topic, String key, CorrelationId correlationId, T payLoad) {

        if (Objects.nonNull(correlationId)) {
            correlationId.continueWith("_" + topic);
        }

        Message<T> message = new Message<>(correlationId, payLoad);

        log.info(String.format("Sending : %s", message));
        log.info("--------------------------------");

        kafkaTemplate.send(topic, key, message);
    }

}
