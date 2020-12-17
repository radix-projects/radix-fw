package com.radix.infrastucture.config.consumer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.radix.infrastucture.Message;
import com.radix.infrastucture.MessageAdapater;

import org.springframework.core.serializer.Deserializer;

import java.io.IOException;
import java.io.InputStream;

public class GsonDeserializer<T> implements Deserializer<Message<?>> {

    private final Gson gson =  new GsonBuilder().registerTypeAdapter(Message.class, new MessageAdapater()).create();

    @Override
    public Message<?> deserialize(InputStream inputStream) throws IOException {
        return this.gson.fromJson(new String(inputStream.readAllBytes()), Message.class);
    }
}
