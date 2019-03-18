/*
 * DBUserGenerator
 *
 * Ivan Zherdev, 2019
 */
package tech.zherdev.usertablesgenerator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

/**
 * Класс DBUserGenerator предназначен для получения пользователей из БД.
 *
 * @author Ivan Zherdev
 */
public class DBUserGenerator extends AUserGenerator {

    private UserDB db;

    /**
     * @throws IOException            в случае ошибки при чтении файла с данными
     *                                для подключения к БД
     */
    DBUserGenerator() throws IOException {
        db = new UserDB();
    }

    /**
     * Метод generateUser() получает пользователя из БД.
     *
     * @return пользователь
     * @throws UserGeneratorException в случае ошибки при получении
     *                                пользователя от БД
     */
    public User generateUser() throws UserGeneratorException {
        Map<String, Object> userMap;

        try {
            userMap = db.getRandUserMap();
        } catch (SQLException e) {
            String message = e.getMessage() + " - Ошибка при получении " +
                    "генератором пользователя от БД.";
            throw new UserGeneratorException(message);
        } catch (NullPointerException e) {
            throw new UserGeneratorException(e.getMessage());
        }

        CustomDate birthday;
        try {
            birthday = new CustomDate(userMap.get("birthday").toString());
        } catch (Exception e) {
            /* В случае любой ошибки берем случайную дату */
            birthday = new CustomDate();
        }
        int age = birthday.countPassedYears();

        return new User(
                (String) userMap.get("name"),
                (String) userMap.get("surname"),
                (String) userMap.get("middlename"),
                age,
                (String) userMap.get("gender"),
                birthday,
                (String) userMap.get("inn"),
                Integer.valueOf((String) userMap.get("postcode")),
                (String) userMap.get("country"),
                (String) userMap.get("region"),
                (String) userMap.get("city"),
                (String) userMap.get("street"),
                (Integer) userMap.get("house"),
                (Integer) userMap.get("flat")
        );
    }

}
