package com.radix.infrastructure.messaging.kafka;


import com.google.gson.*;

import java.lang.reflect.Type;

public class MessageAdapater implements JsonSerializer<Message>, JsonDeserializer<Message> {

   @Override
   public JsonElement serialize(Message message, Type type, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        obj.addProperty("type", message.getPayLoad().getClass().getName());
        obj.add("correlationId", context.serialize(message.getId()));
        obj.add("payLoad", context.serialize(message.getPayLoad()));

        return obj;
   }

    @Override
    public Message deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {

        var obj = jsonElement.getAsJsonObject();
        var payLoadType = obj.get("type").getAsString();
        var correlationId = (CorrelationId) context.deserialize(obj.get("correlationId"), CorrelationId.class);

        try {
            // maybe you want to use a "accept list"
            var payLoad = context.deserialize(obj.get("payLoad"), Class.forName(payLoadType));
            return new Message(correlationId, payLoad);
        } catch (ClassNotFoundException e) {
            // you might want to deal with this exception
            throw new JsonParseException(e);
        }

    }
}
