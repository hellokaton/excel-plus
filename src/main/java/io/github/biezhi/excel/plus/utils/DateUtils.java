package io.github.biezhi.excel.plus.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Date format utils
 *
 * @author biezhi
 * @date 2018/2/5
 */
public final class DateUtils {

    public static Date toDate(String value, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static LocalDate toLocalDate(String value, String pattern) {
        return LocalDate.parse(value, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime toLocalDateTime(String value, String pattern) {
        return LocalDateTime.parse(value, DateTimeFormatter.ofPattern(pattern));
    }

    public static String toString(Date value, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(value);
    }

    public static String toString(LocalDate value, String pattern) {
        return value.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String toString(LocalDateTime value, String pattern) {
        return value.format(DateTimeFormatter.ofPattern(pattern));
    }


}
