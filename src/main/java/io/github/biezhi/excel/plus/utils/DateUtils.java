/**
 * Copyright (c) 2018, biezhi 王爵 (biezhi.me@gmail.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.biezhi.excel.plus.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Date format utils
 *
 * @author biezhi
 * @date 2018/2/5
 */
public final class DateUtils {

    static final DateTimeFormatter DEFAULT_DATE_TIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private static ThreadLocal<DateFormat> dateFormatThreadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));

    public static Date toDate(String value, String pattern) {
        try {
            if (ExcelUtils.isEmpty(pattern)) {
                return dateFormatThreadLocal.get().parse(value);
            } else {
                return new SimpleDateFormat(pattern).parse(value);
            }
        } catch (Exception e) {
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
        if (ExcelUtils.isEmpty(pattern)) {
            return dateFormatThreadLocal.get().format(value);
        } else {
            return new SimpleDateFormat(pattern).format(value);
        }
    }

    public static String toString(LocalDate value, String pattern) {
        return value.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String toString(LocalDateTime value, String pattern) {
        return value.format(DateTimeFormatter.ofPattern(pattern));
    }


}
