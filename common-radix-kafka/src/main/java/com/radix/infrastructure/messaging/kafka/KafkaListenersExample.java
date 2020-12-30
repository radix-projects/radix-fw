package com.radix.infrastructure.messaging.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;

import lombok.extern.java.Log;

@Log
//@Component
public class KafkaListenersExample {

    @KafkaListener(topics = "TOPIC_NEW_USER")
    void listener(Message<?> message) {
        log.info(String.format("Listener %s", message));
    }

    @KafkaListener(topics = { "TOPIC_NEW_USER", "reflectoring-2" }, groupId = "reflectoring-group-2")
    void commonListenerForMultipleTopics(Message<?> message) {
        log.info(String.format("MultipleTopicListener - %s", message));
    }

    @KafkaListener(topicPartitions = @TopicPartition(topic = "reflectoring-3", partitionOffsets = {
            @PartitionOffset(partition = "0", initialOffset = "0") }), groupId = "reflectoring-group-3")
    void listenToParitionWithOffset(@Payload Message<?> message, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                    @Header(KafkaHeaders.OFFSET) int offset) {
        log.info(String.format("ListenToPartitionWithOffset %s from partition- %d with offset- %d", message, partition, offset));
    }

    @KafkaListener(topics = "reflectoring-bytes")
    void listenerForRoutingTemplate(Message<?> message) {
        log.info(String.format("RoutingTemplate BytesListener %s", message));
    }

    @KafkaListener(topics = "reflectoring-others")
    @SendTo("reflectoring-2")
    String listenAndReply(Message<?> message) {
        log.info(String.format("ListenAndReply %s", message));
        return "This is a reply sent to 'reflectoring-2' topic after receiving message at 'reflectoring-others' topic";
    }

}

