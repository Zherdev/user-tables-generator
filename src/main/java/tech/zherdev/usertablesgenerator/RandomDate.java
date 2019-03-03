/*
 * RandomDate
 *
 * Ivan Zherdev, 2019
 */
package tech.zherdev.usertablesgenerator;

import java.util.GregorianCalendar;
import java.security.SecureRandom;

/**
 * Класс RandomDate предназначен работы со случайными датами.
 *
 * @author Ivan Zherdev
 */
public class RandomDate {
    private static final SecureRandom random = new SecureRandom();
    private static final int lowestDate = 1918;
    private static final int highestBound = 100;

    /* Работа с датой ведется при помощи класса GregorianCalendar */
    private GregorianCalendar calendar;

    /** Конструктор класса RandomDate */
    RandomDate() {
        int year = random.nextInt(highestBound) + lowestDate;
        int day;
        calendar = new GregorianCalendar();
        calendar.set(calendar.YEAR, year);
        day = random.nextInt(calendar.getActualMaximum(calendar.DAY_OF_YEAR));
        calendar.set(calendar.DAY_OF_YEAR, day);
    }

    /**
     * Метод countPassedYears() вычисляет, сколько лет прошло с даты,
     * хранящейся в объете RandomDate, до сегодняшнего дня.
     *
     * @return Количество лет
     */
    public int countPassedYears() {
        GregorianCalendar today = new GregorianCalendar();
        int diff = today.get(calendar.YEAR) - calendar.get(calendar.YEAR);
        if (calendar.get(calendar.MONTH) > today.get(calendar.MONTH) ||
                (calendar.get(calendar.MONTH) == today.get(calendar.MONTH) && calendar.get(calendar.DATE) > today.get(calendar.DATE))) {
            diff--;
        }
        return diff;
    }

    /**
     * Перегруженный метод toString() преобразует дату в строку.
     *
     * @return ДД-ММ-ГГГГ
     */
    public String toString() {
        return String.format("%02d", calendar.get(calendar.DAY_OF_MONTH)) + "-"
               + String.format("%02d", calendar.get(calendar.MONTH)) + "-"
               + String.format("%04d", calendar.get(calendar.YEAR));
    }
}
