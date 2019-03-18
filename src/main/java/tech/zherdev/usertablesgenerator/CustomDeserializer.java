/*
 * CustomDeserializer
 *
 * Ivan Zherdev, 2019
 */
package tech.zherdev.usertablesgenerator;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Класс CustomDeserializer реализует интерфейс JsonDeserializer.
 * Предназначен для десериализации объекта User.
 *
 * @author Ivan Zherdev
 */
public class CustomDeserializer implements JsonDeserializer<User> {

    public User deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        JsonObject user = jsonElement.getAsJsonObject().getAsJsonObject("user");
        JsonObject name = user.get("name").getAsJsonObject();
        JsonObject location = user.get("location").getAsJsonObject();

        CustomDate birthday = new CustomDate(user.get("dob").getAsLong());
        int age = birthday.countPassedYears();
        String gender;

        if (user.get("gender").getAsString().equals("male")) {
            gender = "М";
        } else {
            gender = "Ж";
        }

        return new User(
                name.get("first").getAsString(),
                name.get("last").getAsString(),
                name.get("middle").getAsString(),
                age,
                gender,
                birthday,
                location.get("state").getAsString(),
                location.get("city").getAsString(),
                location.get("street").getAsString(),
                location.get("building").getAsInt()
        );
    }

}
