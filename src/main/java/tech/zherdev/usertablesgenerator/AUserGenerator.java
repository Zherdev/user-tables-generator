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

    protected static final int MAX_USERS = 30;
    protected static final int MAX_APPART_NUM = 200;
    protected static final int INN_REGION = 77;
    protected static final int INN_BOUND = 52;             /* Максимальный номер отделения налоговой */
    protected static final int MAX_MAIL_INDEX = 200000;
    protected static final int MIN_MAIL_INDEX = 100000;

    protected static final SecureRandom random = new SecureRandom();
    protected static final String RESOURCE_FOLDER = "";    /* Ресурсы хранятся в корне .jar-архива */

    protected static InnGenerator innGenerator = new InnGenerator(INN_REGION, INN_BOUND);

    /**
     * Метод generateUser() генерирует пользователя.
     *
     * @return пользователь
     * @throws UserGeneratorException
     */
    public abstract User generateUser() throws UserGeneratorException;

    /**
     * Метод generateUsersList(int numOfUsers) генерирует коллекцию
     * из определенного числа пользователей.
     *
     * @param numOfUsers количество пользователей
     * @return Коллекция пользователей
     * @throws UserGeneratorException
     */
    public ArrayList<User> generateUsersList(int numOfUsers) throws UserGeneratorException {
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
     * @throws UserGeneratorException
     */
    public ArrayList<User> generateUsersList() throws UserGeneratorException {
        return generateUsersList(random.nextInt(MAX_USERS) + 1);
    }

}
