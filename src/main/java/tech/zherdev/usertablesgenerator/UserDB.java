/*
 * UserDB
 *
 * Ivan Zherdev, 2019
 */
package tech.zherdev.usertablesgenerator;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс UserDB представляет базу данных для хранения пользователей.
 *
 * @author Ivan Zherdev
 */
public class UserDB extends CustomDB {

    /**
     * @throws IOException в случае ошибки при чтении файла с данными
     * для подключения к БД
     */
    UserDB() throws IOException {
        super();
    }

    /**
     * Метод updateUser(...) обновляет пользователя в БД.
     *
     * @param user новые данные
     * @param userId id обновляемого пользователя в persons
     * @param addressId id адреса пользователя в address
     * @throws SQLException в случае ошибки при обращении к БД
     */
    private void updateUser(User user, int userId,
                            int addressId) throws SQLException {
        justExecUpd(
                "UPDATE persons SET " +
                        "surname = '" + user.getLastName() + "', " +
                        "name = '" + user.getLastName() + "', " +
                        "middlename = '" + user.getPatrName() + "', " +
                        "birthday = STR_TO_DATE('" + user.getBirthday().toString() +
                        "', '%d-%m-%Y'), " + "gender = '" + user.getGender() +
                        "', " + "inn = '" + user.getInn() + "' " +
                        "WHERE id = " + userId + ";"
        );
        justExecUpd(
                "UPDATE address SET " +
                        "postcode = '" + user.getZip() + "', " +
                        "country = '" + user.getCountry() + "', " +
                        "region = '" + user.getRegion() + "', " +
                        "city = '" + user.getCity() + "', " +
                        "street = '" + user.getStreet() + "', " +
                        "house = " + user.getBuilding() + ", " +
                        "flat = " + user.getAppart() + " " +
                        "WHERE id = " + addressId + ";"
        );
    }

    /**
     * Метод insertUser(User user) вставляет новую
     * пользовательскую запись в БД без проверок.
     *
     * @param user пользователь
     * @throws SQLException в случае ошибки при записи в БД
     */
    private void insertUser(User user) throws SQLException {
        int addressId = keyExecUpd(
                "INSERT INTO address " +
                        "(postcode, country, region, city, street, house, flat) " +
                        "VALUES ('" + user.getZip() + "', \"" + user.getCountry() +
                        "\", '" + user.getRegion() + "', '" + user.getCity() + "', '" +
                        user.getStreet() + "', " + user.getBuilding() + ", " +
                        user.getAppart() + ");"
        );
        justExecUpd(
                "INSERT INTO persons " +
                        "(surname, name, middlename, birthday, gender, inn, address_id)" +
                        "VALUES ('" + user.getLastName() + "', '" + user.getFirstName() +
                        "', '" + user.getPatrName() + "', " +
                        "STR_TO_DATE('" + user.getBirthday().toString() + "','%d-%m-%Y')" +
                        ", '" + user.getGender() + "', '" + user.getInn() +
                        "', " + addressId + ");"
        );
    }

    /**
     * Метод checkUser(User user) проверяет, есть ли пользователь в БД.
     *
     * @param user пользователь
     * @return массив int: [0] = 0, если пользователя нет в БД, иначе 1
     *                     [1] - id пользователя (если есть)
     *                     [2] - address_id пользователя (если есть)
     * @throws SQLException в случае ошибки при обращении к БД
     */
    private int[] checkUser(User user) throws  SQLException {
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        int exists = 0;
        int userId = 0;
        int addressId = 0;
        String query = "SELECT id, address_id FROM persons " +
                "WHERE surname = '" + user.getLastName() +
                "' AND name = '" + user.getFirstName() + "' AND " +
                "middlename = '" + user.getPatrName() + "';";

        try {
            con = getConnection();
            stmt = con.createStatement();
            res = stmt.executeQuery(query);
            if (res.next()) {
                exists = 1;
                userId = res.getInt("id");
                addressId = res.getInt("address_id");
            }
            return new int[] {exists, userId, addressId};
        } catch (SQLException e) {
            String message = e.getMessage() + " - Ошибка при обращении к БД, " +
                    "запрос: " + query;
            throw new SQLException(message);
        } finally {
            if (res != null) {
                res.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    /**
     * Метод putUser(User user) записывает либо обновляет пользователя в БД.
     *
     * @param user пользователь
     * @throws SQLException в случае ошибки при записи в БД
     */
    public void putUser(User user) throws SQLException {
        int[] check = checkUser(user);

        if (check[0] == 1) {
            /* Обновляем имеющегося пользователя */
            updateUser(user, check[1], check[2]);
        } else {
            /* Вставляем нового пользователя */
            insertUser(user);
        }
    }

    /**
     * Метод getRandUserMap() возвращает HashMap с данными случайного
     * пользователя из БД.
     *
     * @return HashMap с данными пользователя
     * @throws SQLException в случае ошибки при обращении к БД
     */
    public Map<String, Object> getRandUserMap() throws SQLException {
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        String query = "SELECT * FROM persons INNER JOIN address " +
                       "ON persons.address_id = address.id ORDER BY RAND() " +
                       "LIMIT 1;";

        try {
            con = getConnection();
            stmt = con.createStatement();
            res = stmt.executeQuery(query);
            ResultSetMetaData md = res.getMetaData();
            if (!res.next()) {
                throw new NullPointerException("БД пуста.");
            }
            int columns = md.getColumnCount();
            Map<String, Object> userMap = new HashMap<String, Object>(columns);
            for(int i = 1; i <= columns; ++i){
                userMap.put(md.getColumnName(i), res.getObject(i));
            }
            return userMap;
        } catch (SQLException e) {
            String message = e.getMessage() + " - Ошибка при обращении к БД, " +
                    "запрос: " + query;
            throw new SQLException(message);
        } finally {
            if (res != null) {
                res.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

}
