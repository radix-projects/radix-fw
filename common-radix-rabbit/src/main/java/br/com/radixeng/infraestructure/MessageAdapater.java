package br.com.radixeng.infraestructure;


import com.google.gson.*;

import java.lang.reflect.Type;

public class MessageAdapater implements JsonSerializer<Message<?>>, JsonDeserializer<Message<?>> {

   @Override
   public JsonElement serialize(Message<?> message, Type type, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        obj.addProperty("type", message.getPayLoad().getClass().getName());
        obj.add("payLoad", context.serialize(message.getPayLoad()));

        return obj;
   }

    @Override
    public Message<?> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) {

        var obj = jsonElement.getAsJsonObject();
        var payLoadType = obj.get("type").getAsString();

        try {
            var payLoad = context.deserialize(obj.get("payLoad"), Class.forName(payLoadType));
            return new Message<>(payLoad);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e);
        }

    }
}
