/*
 * RandomUserGenerator
 *
 * Ivan Zherdev, 2019
 */
package tech.zherdev.usertablesgenerator;

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

    private static final SecureRandom random = new SecureRandom();
    private static final String resourceFolder = "";    /* Ресурсы хранятся в корне .jar-архива */
    private static final int maxBuildingNum = 100;

    /* Названия атрибутов соответствуют именам ресурсных файлов */
    private static final String[] nameAttributes = {"Имя", "Фамилия", "Отчество"};
    private static final String[] genders = {"Ж", "М"};
    private static final String[] addressAttributes = {"Страна", "Область",
                                                       "Город", "Улица"};

    private static TextReader textReader  = new TextReader();

    /* Коллекция всех возможных имен пользователей по полу */
    private Map<String, Map<String, ArrayList<String>>> names;

    /* Коллекция всех возможных адресов пользователей */
    private Map<String, ArrayList<String>> addresses;

    /**
     * Конструктор класса UserAttrGenerator
     *
     * @throws IOException в случае ошибки при чтении файла
     */
    RandomUserGenerator() throws IOException {
        /* Осуществляет загрузку данных из ресурсных файлов */

        names = new HashMap<String, Map<String, ArrayList<String>>>();
        addresses = new HashMap<String, ArrayList<String>>();

        /* Загрузка наборов имен. Цикл по мужскому/женскому полу */
        for (String gender: genders) {
            names.put(gender, new HashMap<String, ArrayList<String>>());
            for (String nameType: nameAttributes) {
                try {
                    /*
                     * Имя ресурсного txt файла составляется из пола
                     * и типа искомого набора пользовательских имен
                     */
                    String fileName = resourceFolder + gender
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
        for (String adrType: addressAttributes) {
            try {
                String fileName = resourceFolder + adrType + ".txt";
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
        String gender = genders[random.nextInt(2)];
        int zip = random.nextInt(maxMailIndex - minMailIndex) + minMailIndex;
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
                random.nextInt(maxBuildingNum) + 1,
                random.nextInt(maxAppartNum) + 1
        );
    }

}
