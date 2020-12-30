package com.radix.infrastructure.messaging.kafka;

import com.radix.infrastructure.messaging.kafka.config.producer.KafkaProducerConfig;
import lombok.extern.java.Log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Log
@Component
public class KafkaDispatcher<T> {

	@Autowired
    private KafkaTemplate<String, Message<T>> kafkaTemplate;

    public void send(String topic, String key, CorrelationId correlationId, T payLoad) {

        if (Objects.nonNull(correlationId)) {
            correlationId.continueWith("_" + topic);
        }

        Message<T> message = new Message<>(correlationId, payLoad);

        log.info(String.format("Sending : %s", message));
        log.info("--------------------------------");

        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, key, message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
    
            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
                kafkaTemplate.flush();
            }

            @Override
            public void onFailure(Throwable ex) {
                log.warning("Unable to send message=[" + message + "] due to : " + ex.getMessage());
            }
        });
    }

}
