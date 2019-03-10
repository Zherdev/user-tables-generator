/*
 * XLSXTableExporter
 *
 * Ivan Zherdev, 2019
 */
package tech.zherdev.usertablesgenerator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Класс XLSXTableExporter предназначен для вывода пользовательских
 * данных в XLSX таблицу. Реализует интерфейс ITableExporter.
 *
 * @author Ivan Zherdev
 */
public class XLSXTableExporter implements ITableExporter {
    /* Используется библиотека appache poi */

    private static final String destination = "UserTable.xlsx";
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private XSSFCellStyle titleStyle;
    private int numCol;

    /**
     * Конструктор класса XLSXTableExporter.
     *
     * @param numColumns Количество столбцов в таблице
     */
    XLSXTableExporter(int numColumns) {
        numCol = numColumns;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet();
        titleStyle = createStyleForTitle(workbook);
    }

    /**
     * Статический метод createStyleForTitle(XSSFWorkbook workbook) возвращает
     * стиль для названий столбцов таблицы. Отличается жирным текстом.
     *
     * @return Стиль XSSFCellStyle
     */
    private static XSSFCellStyle createStyleForTitle(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }

    /**
     * Метод newRow(...) заполняет указанный ряд таблицы переданными значениями.
     *
     * @param cellsArr Массив значений ячеек в ряду
     * @param cellsStyle Стиль ячеек
     * @param rowNum Номер ряда
     */
    private void newRow(String[] cellsArr, XSSFCellStyle cellsStyle, int rowNum) {
        Row newRow = sheet.createRow(rowNum);
        int cellNum = 0;
        for (String cellContent: cellsArr) {
            Cell cell = newRow.createCell(cellNum, CellType.STRING);
            cell.setCellValue(cellContent);
            cell.setCellStyle(cellsStyle);
            cellNum++;
        }
    }

    /**
     * Метод setTableName(String name) устанавливает название страницы
     * с таблицей в XLSX-файле.
     *
     * @param name Имя таблицы
     */
    public void setTableName(String name) {
        workbook.setSheetName(0, name);
    }

    /**
     * Метод addRowToEnd(String[] cellsArr) добавляет новый ряд вниз таблицы.
     *
     * @param cellsArr Массив значений ячеек в ряду
     */
    public void addRowToEnd(String[] cellsArr) {
        newRow(cellsArr, workbook.createCellStyle(), sheet.getLastRowNum() + 1);
    }

    /**
     * Метод setHead(String[] titlesArr) устанавливает названия столбцов.
     *
     * @param titlesArr
     */
    public void setHead(String[] titlesArr) {
        newRow(titlesArr, createStyleForTitle(workbook), 0);
    }

    /**
     * Метод exportToFile(File file) осуществляет экспорт таблицы в XLSX-файл.
     *
     * @param file Выходной файл
     * @throws IOException в случае ошибки записи в файл
     */
    public void exportToFile(File file) throws IOException {
        FileOutputStream outFile = new FileOutputStream(file);
        for (int i = 0; i < numCol; i++) {
            workbook.getSheetAt(0).autoSizeColumn(i);
        }
        workbook.write(outFile);
        outFile.close();
        UserTablesGenerator.logger.info("Файл создан. Путь: "
                                        + file.getAbsolutePath());
    }

    /**
     * Перегруженный метод exportToFile(String fileName) осуществляет
     * экспорт таблицы в XLSX-файл по имени.
     *
     * @param fileName Имя выходного файла
     * @throws IOException в случае ошибки записи в файл
     */
    public void exportToFile(String fileName) throws IOException {
        File file = new File(fileName);
        exportToFile(file);
    }

    /**
     * Перегруженный метод exportToFile() осуществляет экспорт
     * таблицы в XLSX-файл. Устаналивается выходной файл по умолчанию.
     *
     * @throws IOException в случае ошибки записи в файл
     */
    public void exportToFile() throws IOException {
        exportToFile(destination);
    }

}
