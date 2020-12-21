package com.radix.infrastructure.messaging.kafka;

import com.radix.infrastructure.messaging.kafka.config.producer.KafkaProducerConfig;
import lombok.extern.java.Log;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Log
@Component
public class KafkaDispatcher<T> {

    private final KafkaProducerConfig kafkaProducerConfig;

    public KafkaDispatcher(KafkaProducerConfig kafkaProducerConfig) {
        this.kafkaProducerConfig = kafkaProducerConfig;
    }

    public void send(String topic, String key, CorrelationId correlationId, T payLoad) {

        var kafkaTemplate = kafkaTemplate();

        if (Objects.nonNull(correlationId)) {
            correlationId.continueWith("_" + topic);
        }

        Message message = new Message<T>(correlationId, payLoad);

        log.info(String.format("Sending : %s", message));
        log.info("--------------------------------");

        kafkaTemplate.send(topic, key, message);
        kafkaTemplate.flush();
    }

    private KafkaTemplate<String, Message<T>> kafkaTemplate() {
        return kafkaProducerConfig.kafkaTemplate();
    }

}
