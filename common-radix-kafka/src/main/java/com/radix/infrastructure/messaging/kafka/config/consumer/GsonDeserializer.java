package com.radix.infrastructure.messaging.kafka.config.consumer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.radix.infrastructure.messaging.kafka.Message;
import com.radix.infrastructure.messaging.kafka.MessageAdapater;
import org.apache.kafka.common.serialization.Deserializer;

public class GsonDeserializer<T> implements Deserializer<Message> {

    private final Gson gson =  new GsonBuilder().registerTypeAdapter(Message.class, new MessageAdapater()).create();

    @Override
    public Message deserialize(String s, byte[] bytes) {
        return this.gson.fromJson(new String(bytes), Message.class);
    }

}
