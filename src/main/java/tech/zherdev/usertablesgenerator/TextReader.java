/*
 * TextReader
 *
 * Ivan Zherdev, 2019
 */
package tech.zherdev.usertablesgenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Класс TextReader предназначен для чтения текста из файлов.
 *
 * @author Ivan Zherdev
 */
public class TextReader {
    /**
     * Метод getReader(String fileName) создает BufferedReader, который
     * читает файл, находящийся в .jar-архиве.
     *
     * @param fileName Имя (адрес) файла в .jar-архиве
     * @return BufferedReader
     * @throws IOException в случае ошибки чтения файла
     */
    private BufferedReader getReader(String fileName) throws IOException {
        InputStream stream = TextReader.class.getClassLoader()
                             .getResourceAsStream(fileName);
        BufferedReader buffReader;
        try {
            buffReader = new BufferedReader(new InputStreamReader(stream,
                                                      "UTF-8"));
        } catch (NullPointerException e) {
            throw new IOException(e.getMessage() + " - не найден файл "
                                  + fileName);
        }
        return buffReader;
    }

    /**
     * Метод readFromFile(String fileName) осуществляет чтение текста из файла.
     *
     * @param fileName Имя (адрес) файла в .jar-архиве
     * @return ArrayList<String> Массив строк
     * @throws IOException в случае ошибки чтения файла
     */
    public ArrayList<String> readFromFile(String fileName) throws IOException {
        BufferedReader buffReader = getReader(fileName);
        ArrayList<String> listOfLines = new ArrayList<String>();

        String line = buffReader.readLine();
        while (line != null)
        {
            listOfLines.add(line);
            line = buffReader.readLine();
        }
        buffReader.close();
        return listOfLines;
    }
}
