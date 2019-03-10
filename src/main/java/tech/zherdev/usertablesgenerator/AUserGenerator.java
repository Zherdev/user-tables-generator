/*
 * AUserGenerator
 *
 * Ivan Zherdev, 2019
 */
package tech.zherdev.usertablesgenerator;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * Абстрактный класс UserGenerator предназначен для генерации пользователей
 *
 * @author Ivan Zherdev
 */
public abstract class AUserGenerator {

    protected static final int maxUsers = 30;
    protected static final int maxAppartNum = 200;
    protected static final int innRegion = 77;
    protected static final int innBound = 52;             /* Максимальный номер отделения налоговой */
    protected static final int maxMailIndex = 200000;
    protected static final int minMailIndex = 100000;

    protected static final SecureRandom random = new SecureRandom();
    protected static final String resourceFolder = "";    /* Ресурсы хранятся в корне .jar-архива */

    protected static InnGenerator innGenerator = new InnGenerator(innRegion, innBound);

    /**
     * Метод generateUser() генерирует пользователя.
     *
     * @return пользователь
     * @throws IOException
     */
    public abstract User generateUser() throws IOException;

    /**
     * Метод generateUsersList(int numOfUsers) генерирует коллекцию
     * из определенного числа пользователей.
     *
     * @param numOfUsers количество пользователей
     * @return Коллекция пользователей
     * @throws IOException
     */
    public ArrayList<User> generateUsersList(int numOfUsers) throws IOException {
        ArrayList<User> usersList = new ArrayList<User>();
        for (; numOfUsers > 0; numOfUsers--) {
            usersList.add(generateUser());
        }
        return usersList;
    }

    /**
     * Перегруженный метод generateUsersList() для случайного числа пользователей.
     *
     * @return Коллекция пользователей
     * @throws IOException
     */
    public ArrayList<User> generateUsersList() throws IOException {
        return generateUsersList(random.nextInt(maxUsers) + 1);
    }

}
