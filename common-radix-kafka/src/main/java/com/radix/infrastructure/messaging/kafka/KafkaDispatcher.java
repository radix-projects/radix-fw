package com.radix.infrastructure.messaging.kafka;

import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.PreDestroy;
import java.util.Objects;

@Log
@Component
public class KafkaDispatcher<T> {

	@Autowired
	private KafkaTemplate<String, Message<T>> kafkaTemplate;

	@SneakyThrows public void send(String topic, String key, CorrelationId correlationId, T payLoad) {

		if (Objects.nonNull(correlationId)) {
			correlationId.continueWith("_" + topic);
		}

		Message<T> message = new Message<>(correlationId, payLoad);

		log.info(String.format("Sending : %s", message));
		log.info("--------------------------------");

		var future = kafkaTemplate.send(topic, key, message);

		future.addCallback(new ListenableFutureCallback<>() {

			@Override
			public void onSuccess(SendResult<String, Message<T>> result) {

				log.info("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
			}

			@Override
			public void onFailure(Throwable ex) {

				log.warning("Unable to send message=[" + message + "] due to : " + ex.getMessage());
			}

		});

	}

	@PreDestroy
	public void flush() {

		this.kafkaTemplate.flush();
	}

}


