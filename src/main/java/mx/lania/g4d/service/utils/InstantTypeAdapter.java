package mx.lania.g4d.service.utils;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.Instant;

public class InstantTypeAdapter implements JsonSerializer<Instant>, JsonDeserializer<Instant> {

    @Override
    public JsonElement serialize(Instant src, Type typeOfSrc, JsonSerializationContext context) {
        long seconds = src.getEpochSecond();
        return new JsonPrimitive(seconds);
    }

    @Override
    public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        long seconds = json.getAsLong();
        return Instant.ofEpochSecond(seconds);
    }
}
