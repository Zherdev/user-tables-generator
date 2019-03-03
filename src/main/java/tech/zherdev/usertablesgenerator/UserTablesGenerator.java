/*
 * UserTablesGenerator
 *
 * Ivan Zherdev, 2019
 */
package tech.zherdev.usertablesgenerator;

import org.apache.log4j.Logger;

import java.io.IOException;
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
    private static final String[] titles = {"Имя", "Фамилия", "Отчество",
                                            "Возраст", "Пол", "Дата рождения",
                                            "ИНН", "Индекс", "Страна", "Область",
                                            "Город", "Улица", "Дом", "Квартира"};

    /* Заголовок таблицы */
    private static final String tableName = "Пользовательские данные";

    /* Генератор пользовательских данных */
    private UserAttrGenerator userAttrGenerator;

    /* Массив, содержащий экспортеры таблиц */
    private TableExporter[] exportersArr;

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
            /* Выводим ошибку в лог */
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
        userAttrGenerator = new UserAttrGenerator();

        /* Добавляем экспортер в XLSX и экспортер в PDF */
        exportersArr = new TableExporter[] {new XLSXTableExporter(titles.length),
                                            new PDFTableExporter(titles.length)};
    }

    /**
     * Метод run() выполняет генерацию данных и их вывод в таблицу(-ы).
     *
     * @throws IOException в случае ошибки при записи в файл
     */
    private void run() throws IOException {

        /* Коллекция наборов пользовательских данных */
        ArrayList<String[]> usersList = userAttrGenerator.generateUsersList();

        /* Цикл по экспортерам таблиц */
        for (TableExporter tabExp: exportersArr) {
            /* Форматируем таблицу */
            tabExp.setHead(titles);
            tabExp.setTableName(tableName);

            /* Добавляем пользовательские записи */
            for (String[] userAttributes : usersList) {
                tabExp.addRowToEnd((userAttributes));
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
