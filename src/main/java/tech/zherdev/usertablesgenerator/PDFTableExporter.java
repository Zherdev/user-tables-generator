/*
 * PDFTableExporter
 *
 * Ivan Zherdev, 2019
 */
package tech.zherdev.usertablesgenerator;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.File;
import java.io.IOException;

/**
 * Класс PDFTableExporter предназначен для вывода пользовательских
 * данных в PDF таблицу. Реализует интерфейс TableExporter.
 *
 * @author Ivan Zherdev
 */
public class PDFTableExporter implements TableExporter {
    /* Используется библиотека itextpdf */

    private static final String destination = "UserTable.pdf";
    private static final String FONT = "FreeSans.ttf";
    private Table mainTable;
    private Paragraph head;
    private PdfFont font;

    /**
     * Конструктор класса PDFTableExporter.
     *
     * @param numColumns Количество столбцов в таблице
     * @throws IOException в случае ошибки при загрузке шрифта
     */
    PDFTableExporter(int numColumns) throws IOException{
        font = createFont();
        mainTable = new Table(numColumns);
        mainTable.setFont(font);
        mainTable.setFontSize(5);
        head = null;
    }

    /**
     * Статический метод createFont() возвращает шрифт для выходного файла.
     *
     * @throws IOException в случае ошибки при загрузке шрифта
     * @return Шрифт PdfFont
     */
    private static PdfFont createFont() throws IOException {
        /* Шрифт берется из .ttf файла, заданного константой FONT */
        return PdfFontFactory.createFont(FONT, "Identity-H", true);
    }

    /**
     * Метод setTableName(String name) устанавливает заголовок для PDF таблицы.
     * Должен быть вызван до ввода других данных в таблицу.
     *
     * @param name Имя таблицы
     */
    public void setTableName(String name) {
        /* Добавляет параграф с названием */
        head = new Paragraph(name);
        head.setFont(font);
    }

    /**
     * Метод setHead(String[] titlesArr) устанавливает названия столбцов.
     * Должен быть вызван до начала ввода рядов в таблицу.
     *
     * @param titlesArr Массив названий столбцов
     */
    public void setHead(String[] titlesArr) {
        addRowToEnd(titlesArr);
    }

    /**
     * Метод addRowToEnd(String[] cellsArr) добавляет новый ряд вниз таблицы.
     *
     * @param cellsArr Массив значений ячеек в ряду
     */
    public void addRowToEnd(String[] cellsArr) {
        for (String cellContent: cellsArr) {
            mainTable.addCell(cellContent);
        }
    }

    /**
     * Метод exportToFile(File file) осуществляет экспорт таблицы в PDF-файл.
     *
     * @param file Выходной файл
     * @throws IOException в случае ошибки записи в файл
     */
    public void exportToFile(File file) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(file));
        Document doc = new Document(pdfDoc);

        if (head != null) {
            doc.add(head);
        }
        doc.add(mainTable);
        doc.close();

        UserTablesGenerator.logger.info("Файл создан. Путь: "
                                        + file.getAbsolutePath());
    }

    /**
     * Перегруженный метод exportToFile(String fileName) осуществляет
     * экспорт таблицы в PDF файл.
     *
     * @param fileName Имя выходного файла
     * @throws IOException в случае ошибки записи в файл
     */
    public void exportToFile(String fileName) throws IOException {
        File file = new File(fileName);
        exportToFile(file);
    }

    /**
     * Перегруженный метод exportToFile() осуществляет экспорт таблицы в PDF файл.
     * Устаналивается выходной файл по умолчанию.
     *
     * @throws IOException в случае ошибки записи в файл
     */
    public void exportToFile() throws IOException {
        exportToFile(destination);
    }
}
