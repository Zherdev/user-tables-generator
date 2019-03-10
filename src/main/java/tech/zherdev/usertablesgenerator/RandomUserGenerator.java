/*
 * RandomUserGenerator
 *
 * Ivan Zherdev, 2019
 */
package tech.zherdev.usertablesgenerator;

import com.sun.org.apache.regexp.internal.RE;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс RandomUserGenerator предназначен для генерации
 * случайных пользователей на основе локальных (оффлайн) параметров.
 * Производный от абстрактного класса AUserGenerator.
 *
 * @author Ivan Zherdev
 */
public class RandomUserGenerator extends AUserGenerator {

    private static final int MAX_BUILDING_NUM = 100;

    /* Названия атрибутов соответствуют именам ресурсных файлов */
    private static final String[] NAME_ATTRIBUTES = {"Имя", "Фамилия", "Отчество"};
    private static final String[] GENDERS = {"Ж", "М"};
    private static final String[] ADRESS_ATTRIBUTES = {"Страна", "Область",
                                                       "Город", "Улица"};

    private static TextReader textReader  = new TextReader();

    /* Коллекция всех возможных имен пользователей по полу */
    private Map<String, Map<String, ArrayList<String>>> names;

    /* Коллекция всех возможных адресов пользователей */
    private Map<String, ArrayList<String>> addresses;

    /** @throws IOException в случае ошибки при чтении файла */
    RandomUserGenerator() throws IOException {
        /* Осуществляет загрузку данных из ресурсных файлов */

        names = new HashMap<String, Map<String, ArrayList<String>>>();
        addresses = new HashMap<String, ArrayList<String>>();

        /* Загрузка наборов имен. Цикл по мужскому/женскому полу */
        for (String gender: GENDERS) {
            names.put(gender, new HashMap<String, ArrayList<String>>());
            for (String nameType: NAME_ATTRIBUTES) {
                try {
                    /*
                     * Имя ресурсного txt файла составляется из пола
                     * и типа искомого набора пользовательских имен
                     */
                    String fileName = RESOURCE_FOLDER + gender
                                      + nameType.substring(0, 3) + ".txt";
                    names.get(gender).put(nameType,
                                          textReader.readFromFile(fileName));
                } catch (IOException e) {
                    String message = e.getMessage() + " - Ошибка при чтении файла "
                            + gender + nameType.substring(0, 3) + ".txt";
                    throw new IOException(message);
                }
            }
        }
        /* Загрузка адресов. Цикл по типу адреса (страна/область...) */
        for (String adrType: ADRESS_ATTRIBUTES) {
            try {
                String fileName = RESOURCE_FOLDER + adrType + ".txt";
                addresses.put(adrType, textReader.readFromFile(fileName));
            } catch (IOException e) {
                String message = e.getMessage() + " - Ошибка при чтении файла "
                                 + adrType + ".txt";
                throw new IOException(message);
            }
        }
    }

    /**
     * Метод generateUser() генерирует случайного пользователя оффлайн.
     *
     * @return пользователь
     */
    public User generateUser() {
        String gender = GENDERS[random.nextInt(2)];
        int zip = random.nextInt(MAX_MAIL_INDEX - MIN_MAIL_INDEX) + MIN_MAIL_INDEX;
        CustomDate birthDate = new CustomDate();

        return new User(
                names.get(gender).get("Имя")
                        .get(random.nextInt(names.get(gender).get("Имя").size())),
                names.get(gender).get("Фамилия")
                        .get(random.nextInt(names.get(gender).get("Фамилия").size())),
                names.get(gender).get("Отчество")
                        .get(random.nextInt(names.get(gender).get("Отчество").size())),
                birthDate.countPassedYears(),
                gender,
                birthDate,
                innGenerator.generateINN(),
                zip,
                addresses.get("Страна")
                        .get(random.nextInt(addresses.get("Страна").size())),
                addresses.get("Область")
                        .get(random.nextInt(addresses.get("Область").size())),
                addresses.get("Город")
                        .get(random.nextInt(addresses.get("Город").size())),
                addresses.get("Улица")
                        .get(random.nextInt(addresses.get("Улица").size())),
                random.nextInt(MAX_BUILDING_NUM) + 1,
                random.nextInt(MAX_APPART_NUM) + 1
        );
    }

}
