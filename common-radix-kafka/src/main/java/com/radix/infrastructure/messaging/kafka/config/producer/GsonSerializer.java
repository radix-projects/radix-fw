package com.radix.infrastructure.messaging.kafka.config.producer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.radix.infrastructure.messaging.kafka.Message;
import com.radix.infrastructure.messaging.kafka.MessageAdapater;
import org.apache.kafka.common.serialization.Serializer;

public class GsonSerializer<T> implements Serializer<Message<T>> {

    private final Gson gson = new GsonBuilder().registerTypeAdapter(Message.class, new MessageAdapater()).create();

    @Override
    public byte[] serialize(String s, Message<T> message) {
        return this.gson.toJson(message).getBytes();
    }

}
