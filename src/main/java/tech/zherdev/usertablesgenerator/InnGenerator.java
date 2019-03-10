/*
 * InnGenerator
 *
 * Ivan Zherdev, 2019
 */
package tech.zherdev.usertablesgenerator;

import java.security.SecureRandom;

/**
 * Класс InnGenerator предназначен для генерации валидных случайных ИНН.
 *
 * @author Ivan Zherdev
 */
public class InnGenerator {

    private static final SecureRandom random = new SecureRandom();

    private String innRegion;
    private int innBound;             /* Максимальный номер отделения налоговой */

    /** Констркутор класса InnGenerator */
    InnGenerator(int innRegion, int innBound) {
        this.innRegion = String.valueOf(innRegion);
        this.innBound = innBound;
    }

    /** Метод getIntFromStr(...) сокращает код */
    private int getIntFromStr(String str, int index) {
        return Character.getNumericValue(str.charAt(index));
    }

    /**
     * Метод generateINN() генерирует валидный ИНН физического лица.
     *
     * @return строка из 12 чисел
     */
    public String generateINN() {
        String department;
        String record;
        int[] control = new int[2];
        department = String.format("%02d", random.nextInt(innBound));
        record = String.format("%06d", random.nextInt(1000000));

        /* Вычисление контрольных значений */
        control[0] = 7*getIntFromStr(innRegion, 0) + 2*getIntFromStr(innRegion, 1)
                + 4*getIntFromStr(department, 0) + 10*getIntFromStr(department, 1)
                + 3*getIntFromStr(record, 0) + 5*getIntFromStr(record, 1)
                + 9*getIntFromStr(record, 2) + 4*getIntFromStr(record, 3)
                + 6*getIntFromStr(record, 4) + 8*getIntFromStr(record, 5);
        control[0] = (control[0] % 11) % 10;
        control[1] = 3*getIntFromStr(innRegion, 0) + 7*getIntFromStr(innRegion, 1)
                + 2*getIntFromStr(department, 0) + 4*getIntFromStr(department, 1)
                + 10*getIntFromStr(record, 0) + 3*getIntFromStr(record, 1)
                + 5*getIntFromStr(record, 2) + 9*getIntFromStr(record, 3)
                + 4*getIntFromStr(record, 4) + 6*getIntFromStr(record, 5)
                + 8*control[0];
        control[1] = (control[1] % 11) % 10;

        return innRegion + department + record + control[0] + control[1];
    }

}
