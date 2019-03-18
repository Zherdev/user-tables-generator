/*
 * UserTablesGenerator
 *
 * Ivan Zherdev, 2019
 */
package tech.zherdev.usertablesgenerator;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Основной класс, предназначен для инициализации
 * и запуска приложения User Tables Generator.
 *
 * @author Ivan Zherdev
 */
public class UserTablesGenerator {

    /* Логгер log4j */
    public static final Logger logger = Logger.getLogger(UserTablesGenerator.class);

    /* Названия столбцов таблицы */
    private static final String[] TITLES = {"Имя", "Фамилия", "Отчество",
                                            "Возраст", "Пол", "Дата рождения",
                                            "ИНН", "Индекс", "Страна", "Область",
                                            "Город", "Улица", "Дом", "Квартира"};

    /* Заголовок таблицы */
    private static final String TABLE_NAME = "Пользовательские данные";

    /* Коллекция генераторов пользователей */
    private ArrayList<AUserGenerator> generatorsList;

    /* Массив, содержащий экспортеры таблиц */
    private ITableExporter[] exportersArr;

    /**
     * Основной метод.
     *
     * @param args Входные параметры не ожидаются
     * @throws IOException в случае ошибки при работе с файлами
     */
    public static void main(String[] args) throws IOException {
        UserTablesGenerator utg = new UserTablesGenerator();

        try {
            utg.init();
            utg.run();
        } catch (IOException e) {
            logger.error(e);
            throw e;
        }
    }

    /**
     * Метод init() инициализирует объекты, необходимые для работы
     * приложения User Tables Generator.
     *
     * @throws IOException в случае ошибки при чтении из файла
     */
    private void init() throws IOException {

        generatorsList = new ArrayList<AUserGenerator>();

        /* Добавляем генераторы пользователей в порядке убывания приоритета */
        generatorsList.add(new APIUserGenerator());
        try {
            DBUserGenerator dbGen = new DBUserGenerator();
            generatorsList.add(dbGen);
        } catch (Exception e) {
            /* Не используем БД в случае любой ошибки при подключении */
            logger.error(e);
        }
        generatorsList.add(new RandomUserGenerator());

        /* Добавляем экспортер в XLSX и экспортер в PDF */
        exportersArr = new ITableExporter[] {new XLSXTableExporter(TITLES.length),
                                             new PDFTableExporter(TITLES.length)};
    }

    /**
     * Метод run() выполняет генерацию данных и их вывод в таблицу(-ы).
     *
     * @throws IOException в случае ошибки при записи в файл
     */
    private void run() throws IOException {
        ArrayList<User> usersList = null;

        /* Цикл по генераторам пользователей */
        for (AUserGenerator generator: generatorsList) {
            try {
                usersList = generator.generateUsersList();

                /* Если генерация прошла успешно - выходим из цикла */
                break;
            } catch (UserGeneratorException e) {
                /* Выводим ошибку в лог */
                logger.error(e);
            }
        }

        /* Цикл по экспортерам таблиц */
        for (ITableExporter tabExp: exportersArr) {
            /* Форматируем таблицу */
            tabExp.setHead(TITLES);
            tabExp.setTableName(TABLE_NAME);

            /* Добавляем пользовательские записи */
            for (User user: usersList) {
                tabExp.addRowToEnd(user.toStringArray());
            }

            /* Вывод в файл */
            try {
                tabExp.exportToFile();
            } catch (IOException e) {
                throw new IOException(e.getMessage() + " - Ошибка при экспорте данных.");
            }
        }
    }

}
