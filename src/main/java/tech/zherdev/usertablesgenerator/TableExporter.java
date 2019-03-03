/*
 * TableExporter
 *
 * Ivan Zherdev, 2019
 */
package tech.zherdev.usertablesgenerator;

import java.io.File;
import java.io.IOException;

/**
 * Интерфейс TableExporter предназначен для вывода пользовательских
 * данных во внешнюю таблицу.
 *
 * @author Ivan Zherdev
 */
public interface TableExporter {
    /**
     * Метод addRowToEnd(String[] cellsList) добавляет новый ряд
     * в конец таблицы.
     *
     * @param cellsList Массив значений ячеек в ряду
     */
    void addRowToEnd(String[] cellsList);

    /**
     * Метод setHead(String[] titlesList) устанавливает названия столбцов.
     *
     * @param titlesArr
     */
    void setHead(String[] titlesArr);

    /**
     * Метод setTableName(String name) устанавливает название таблицы.
     *
     * @param name
     */
    void setTableName(String name);

    /**
     * Метод exportToFile(File file) осуществляет экспорт таблицы
     * во внешний файл.
     *
     * @param file
     * @throws IOException в случае ошибки записи в файл
     */
    void exportToFile(File file) throws IOException;

    /**
     * Перегруженный метод exportToFile(String fileName) осуществляет экспорт таблицы
     * во внешний файл по имени.
     *
     * @param fileName
     * @throws IOException в случае ошибки записи в файл
     */
    void exportToFile(String fileName) throws IOException;

    /**
     * Перегруженный метод exportToFile(String fileName) осуществляет экспорт таблицы
     * во внешний файл. Устаналивается выходной файл по умолчанию.
     *
     * @throws IOException в случае ошибки записи в файл
     */
    void exportToFile() throws IOException;
}
