/*
 * UserGeneratorException
 *
 * Ivan Zherdev, 2019
 */
package tech.zherdev.usertablesgenerator;

/**
 * Исключение UserGeneratorException выбрасывается генератором,
 * реализующим AUserGenerator, в случае ошибки при генерации
 * пользователя. Ошибка может быть связана с чтением из файла,
 * с API или с MySQL.
 *
 * @author Ivan Zherdev
 */
public class UserGeneratorException extends Exception {
    private String message;

    UserGeneratorException(String message) {
        super(message + " - Ошибка при генерации пользователя.");
    }
}
