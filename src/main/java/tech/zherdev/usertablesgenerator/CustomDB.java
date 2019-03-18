/*
 * CustomDB
 *
 * Ivan Zherdev, 2019
 */
package tech.zherdev.usertablesgenerator;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

/**
 * Класс CustomDB предназначен для работы с базой данных.
 *
 * @author Ivan Zherdev
 */
public class CustomDB {

    private static final String RESOURCE_FOLDER = "";    /* Ресурсы хранятся в корне .jar-архива */
    private static final String AUTH_FILE = "db.txt";    /* Данные для подключения к БД */
    private static final String PARAMS = "?useUnicode=true&" +
                                         "useJDBCCompliantTimezoneShift=true&" +
                                         "useLegacyDatetimeCode=false&" +
                                         "serverTimezone=UTC";

    private static TextReader textReader  = new TextReader();

    private String dbName;
    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    /**
     * @throws IOException в случае ошибки при чтении файла AUTH_FILE
     */
    CustomDB() throws IOException {
        ArrayList<String> authData;
        String fileName = RESOURCE_FOLDER + AUTH_FILE;

        try {
            authData = textReader.readFromFile(fileName);
        } catch (IOException e) {
            String message = e.getMessage() + " - Ошибка при чтении файла "
                    + fileName;
            throw new IOException(message);
        }

        dbUrl = authData.get(0);
        dbName = authData.get(1);
        dbUser = authData.get(2);
        dbPassword = authData.get(3);
    }

    /**
     * @return connection с БД
     * @throws SQLException в случае ошибки при подключении к БД
     */
    protected Connection getConnection() throws SQLException {
        Connection con = null;

        try {
            con = DriverManager.getConnection(dbUrl + "/" + dbName + PARAMS,
                    dbUser, dbPassword);
        } catch (SQLException e) {
            if (con != null) {
                con.close();
            }
            String message = e.getMessage() + " - Ошибка при подключении "
                    + "к БД " + dbName;
            throw new SQLException(message);
        }

        return con;
    }

    /**
     * Метод justExecUpd(String query) предназначен для простого
     * обновления строки в таблице.
     *
     * @param query запрос
     * @return количество измененных строк
     * @throws SQLException в случае ошибки при обращении к БД
     */
    protected int justExecUpd(String query) throws SQLException {
        Connection con = null;
        Statement stmt = null;

        try {
            con = getConnection();
            stmt = con.createStatement();
            return stmt.executeUpdate(query);
        } catch (SQLException e) {
            String message = e.getMessage() + " - Ошибка при обращении "
                    + "к БД " + dbName + ", запрос: " + query;
            throw new SQLException(message);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    /**
     * Метод keyExecUpd(String query) предназначен для вставки строки
     * в таблицу, возвращает id вставленной записи.
     *
     * @param query запрос
     * @return id
     * @throws SQLException в случае ошибки при обращении к БД
     */
    protected int keyExecUpd(String query) throws SQLException {
        ResultSet keys = null;
        Connection con = null;
        PreparedStatement pStmt = null;

        try {
            con = getConnection();
            pStmt = con.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);
            pStmt.executeUpdate();
            keys = pStmt.getGeneratedKeys();
            keys.next();
            return keys.getInt(1);
        } catch (SQLException e) {
            String message = e.getMessage() + " - Ошибка при обращении "
                    + "к БД " + dbName + ", запрос: " + query;
            throw new SQLException(message);
        } finally {
            if (keys != null) {
                keys.close();
            }
            if (pStmt != null) {
                pStmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

}
