package com.radix.infrastructure.messaging.kafka.config.consumer;


import com.radix.infrastructure.messaging.kafka.Message;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class KafkaConsumerConfig<T> {

    @Value("${config.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${config.kafka.group-id}")
    private String groupID;

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupID);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest"); // earliest - latest
        return props;
    }

    @Bean
    public ConsumerFactory<String, Message<T>> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Message<T>>> kafkaListenerContainerFactory(KafkaTemplate<String, Message<T>> kafkatemplate) {
        ConcurrentKafkaListenerContainerFactory<String, Message<T>> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setReplyTemplate(kafkatemplate);
        return factory;
    }


}
