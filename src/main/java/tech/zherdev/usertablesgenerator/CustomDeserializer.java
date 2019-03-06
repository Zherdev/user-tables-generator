package tech.zherdev.usertablesgenerator;

import com.google.gson.*;

import java.lang.reflect.Type;

public class CustomDeserializer implements JsonDeserializer<User> {

    public User deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        CustomDate birthday = new CustomDate(jsonObject.get("dob").getAsLong());
        int age = birthday.countPassedYears();

        return new User(
                jsonObject.get("first").getAsString(),
                jsonObject.get("last").getAsString(),
                jsonObject.get("middle").getAsString(),
                age,
                jsonObject.get("gender").getAsString(),
                birthday,
                jsonObject.get("zip").getAsInt(),
                jsonObject.get("state").getAsString(),
                jsonObject.get("city").getAsString(),
                jsonObject.get("street").getAsString(),
                jsonObject.get("building").getAsInt()
        );
    }
}
