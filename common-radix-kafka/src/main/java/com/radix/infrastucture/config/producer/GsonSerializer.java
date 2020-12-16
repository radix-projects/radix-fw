package com.radix.infrastucture.config.producer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.radix.infrastucture.Message;
import com.radix.infrastucture.MessageAdapater;
import org.apache.kafka.common.serialization.Serializer;

public class GsonSerializer<T> implements Serializer<T> {

    private final Gson gson = new GsonBuilder().registerTypeAdapter(Message.class, new MessageAdapater()).create();

    @Override
    public byte[] serialize(String s, T object) {
        return this.gson.toJson(object).getBytes();
    }

}
