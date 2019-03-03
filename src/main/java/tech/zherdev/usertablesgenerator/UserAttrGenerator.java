/*
 * UserAttrGenerator
 *
 * Ivan Zherdev, 2019
 */
package tech.zherdev.usertablesgenerator;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс UserAttrGenerator предназначен для генерации
 * наборов пользовательских данных (атрибутов).
 *
 * @author Ivan Zherdev
 */
public class UserAttrGenerator {
    private static final SecureRandom random = new SecureRandom();
    private static final String resourceFolder = "";    /* Ресурсы хранятся в корне .jar-архива */
    private static final String innRegion = "77";
    private static final int innBound = 52;             /* Максимальный номер отделения налоговой */
    private static final int maxUsers = 30;
    private static final int maxMailIndex = 200000;
    private static final int minMailIndex = 100000;
    private static final int maxHouseNum = 100;
    private static final int maxAppartNum = 200;

    /* Названия атрибутов соответствуют именам ресурсных файлов */
    private static final String[] nameAttributes = {"Имя", "Фамилия", "Отчество"};
    private static final String[] genders = {"Ж", "М"};
    private static final String[] addressAttributes = {"Страна", "Область",
                                                       "Город", "Улица"};
    private static TextReader textReader;

    /* Коллекция всех возможных имен пользователей по полу */
    private Map<String, Map<String, ArrayList<String>>> names;

    /* Коллекция всех возможных адресов пользователей */
    private Map<String, ArrayList<String>> addresses;

    /**
     * Конструктор класса UserAttrGenerator
     *
     * @throws IOException в случае ошибки при чтении файла
     */
    UserAttrGenerator() throws IOException {
        /* Осуществляет загрузку данных из ресурсных файлов */
        textReader = new TextReader();
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

    /** Метод getIntFromStr(...) сокращает код */
    private int getIntFromStr(String str, int index) {
        return Character.getNumericValue(str.charAt(index));
    }

    /**
     * Метод generateINN() генерирует валидный ИНН физического лица.
     *
     * @return строка из 12 чисел
     */
    private String generateINN() {
        String department;
        String record;
        int[] control = new int[2];
        department = String.format("%02d", random.nextInt(innBound));
        record = String.format("%06d", random.nextInt(1000000));

        /* Вычисление контрольных значений */
        control[0] = 7*getIntFromStr(innRegion, 0) + 2*getIntFromStr(innRegion, 1)
                     + 4*getIntFromStr(department, 0) + 10*getIntFromStr(department, 1)
                     + 3*getIntFromStr(record, 0) + 5*getIntFromStr(record, 1)
                     + 9*getIntFromStr(record, 2) + 4*getIntFromStr(record, 3)
                     + 6*getIntFromStr(record, 4) + 8*getIntFromStr(record, 5);
        control[0] = (control[0] % 11) % 10;
        control[1] = 3*getIntFromStr(innRegion, 0) + 7*getIntFromStr(innRegion, 1)
                     + 2*getIntFromStr(department, 0) + 4*getIntFromStr(department, 1)
                     + 10*getIntFromStr(record, 0) + 3*getIntFromStr(record, 1)
                     + 5*getIntFromStr(record, 2) + 9*getIntFromStr(record, 3)
                     + 4*getIntFromStr(record, 4) + 6*getIntFromStr(record, 5)
                     + 8*control[0];
        control[1] = (control[1] % 11) % 10;

        return innRegion + department + record + control[0] + control[1];
    }

    /**
     * Метод generateUser() генерирует случайный набор атрибутов
     * для одного пользователя.
     *
     * @return массив, содержащий набор значений атрибутов пользователя
     */
    public String[] generateUser() {
        List<String> userAttributes = new ArrayList<String>();
        String gender = genders[random.nextInt(2)];
        int index = random.nextInt(maxMailIndex - minMailIndex) + minMailIndex;
        RandomDate birthDate = new RandomDate();

        for (String nameType: nameAttributes) {
            userAttributes.add(names.get(gender).get(nameType)
                    .get(random.nextInt(names.get(gender).get(nameType).size())));
        }
        userAttributes.add(Integer.toString(birthDate.countPassedYears()));
        userAttributes.add(gender);
        userAttributes.add(birthDate.toString());
        userAttributes.add(generateINN());
        userAttributes.add(String.valueOf(index));
        for (String addressType: addressAttributes) {
            userAttributes.add(addresses.get(addressType)
                    .get(random.nextInt(addresses.get(addressType).size())));
        }
        userAttributes.add(String.valueOf(random.nextInt(maxHouseNum) + 1));
        userAttributes.add(String.valueOf(random.nextInt(maxAppartNum) + 1));

        return userAttributes.toArray(new String[0]);
    }

    /**
     * Метод generateUsersList(int numOfUsers) генерирует коллекцию
     * из определнного числа пользовательских записей.
     *
     * @param numOfUsers количество наборов
     * @return Коллекция массивов, содержащих значения атрибутов пользователей
     */
    public ArrayList<String[]> generateUsersList(int numOfUsers) {
        ArrayList<String[]> usersList = new ArrayList<String[]>();
        for (; numOfUsers > 0; numOfUsers--) {
            usersList.add(generateUser());
        }
        return usersList;
    }

    /**
     * Перегруженный метод generateUsersList() для случайного числа наборов.
     *
     * @return Коллекция массивов, содержащих значения атрибутов пользователей
     */
    public ArrayList<String[]> generateUsersList() {
        return generateUsersList(random.nextInt(maxUsers) + 1);
    }
}
