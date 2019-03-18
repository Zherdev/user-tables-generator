/*
 * CustomDate
 *
 * Ivan Zherdev, 2019
 */
package tech.zherdev.usertablesgenerator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.security.SecureRandom;

/**
 * Класс CustomDate предназначен работы с датами.
 *
 * @author Ivan Zherdev
 */
public class CustomDate {

    private static final SecureRandom random = new SecureRandom();
    private static final int LOWEST_DATE = 1918;
    private static final int HIGHEST_BOUND = 100;

    /* Работа с датой ведется при помощи класса GregorianCalendar */
    private GregorianCalendar calendar;

    /** CustomDate для случайной даты */
    CustomDate() {
        int year = random.nextInt(HIGHEST_BOUND) + LOWEST_DATE;
        int day;
        calendar = new GregorianCalendar();
        calendar.set(calendar.YEAR, year);
        day = random.nextInt(calendar.getActualMaximum(calendar.DAY_OF_YEAR));
        calendar.set(calendar.DAY_OF_YEAR, day);
    }

    /**
     * CustomDate для заданной в секундах даты
     * @param timestamp дата в секундах
     */
    CustomDate(long timestamp) {
        calendar = new GregorianCalendar();
        calendar.setTimeInMillis(timestamp * 1000);
    }

    /**
     * CustomDate для даты, заданной в виде ГГГГ-ММ-ДД
     *
     * @param dateReverse
     * @throws ParseException в случае ошибки при парсинге даты
     */
    CustomDate(String dateReverse) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(dateReverse);
        calendar = new GregorianCalendar();
        calendar.setTime(date);
    }

    /** @return возвращает хранящуюся дату в календаре */
    public GregorianCalendar getCalendar() {
        return calendar;
    }

    /**
     * Метод countPassedYears() вычисляет, сколько лет прошло с даты,
     * хранящейся в объете CustomDate, до сегодняшнего дня.
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
     * Переопределенный метод toString() преобразует дату в строку.
     *
     * @return ДД-ММ-ГГГГ
     */
    @Override
    public String toString() {
        return String.format("%02d", calendar.get(calendar.DAY_OF_MONTH)) + "-"
               + String.format("%02d", calendar.get(calendar.MONTH) + 1) + "-"
               + String.format("%04d", calendar.get(calendar.YEAR));
    }

}
