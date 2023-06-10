package mx.lania.g4d.service.Utils;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.LocalDate;

public class LocalDateTypeAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

    @Override
    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
        int year = src.getYear();
        int month = src.getMonthValue();
        int day = src.getDayOfMonth();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("year", year);
        jsonObject.addProperty("month", month);
        jsonObject.addProperty("day", day);

        return jsonObject;
    }

    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        int year = jsonObject.getAsJsonPrimitive("year").getAsInt();
        int month = jsonObject.getAsJsonPrimitive("month").getAsInt();
        int day = jsonObject.getAsJsonPrimitive("day").getAsInt();

        return LocalDate.of(year, month, day);
    }
}
