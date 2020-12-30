package com.radix.infrastructure.messaging.kafka.config.consumer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.radix.infrastructure.messaging.kafka.Message;
import com.radix.infrastructure.messaging.kafka.MessageAdapater;

import org.springframework.core.serializer.Deserializer;

public class GsonDeserializer<T> implements Deserializer<Message<T>> {

    private final Gson gson =  new GsonBuilder().registerTypeAdapter(Message.class, new MessageAdapater()).create();

    @Override
    public Message<T> deserialize(InputStream inputStream) throws IOException {
        return this.gson.fromJson(new String(inputStream.readAllBytes()), Message.class);
    }
}
