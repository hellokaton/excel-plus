package io.github.biezhi.excel.plus.utils;

import io.github.biezhi.excel.plus.Constant;
import io.github.biezhi.excel.plus.annotation.ExcelField;
import io.github.biezhi.excel.plus.annotation.ExcelSheet;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Excel utils
 *
 * @author biezhi
 * @date 2018/2/4
 */
public class ExcelUtils {

    private static final Map<String, List<Field>> FIELD_CACHE = new HashMap<>(8);

    public static String getSheetName(Object item) {
        ExcelSheet excelSheet = item.getClass().getAnnotation(ExcelSheet.class);
        if (null == excelSheet) {
            return Constant.DEFAULT_SHEET_NAME;
        }
        return excelSheet.value();
    }

    public static List<String> getWriteFieldNames(Class<?> type) {
        List<Field>      fields = getAndSaveFields(type);
        List<ExcelField> list   = new ArrayList<>(fields.size());

        for (Field field : fields) {
            ExcelField excelField = field.getAnnotation(ExcelField.class);
            if (null != excelField) {
                list.add(excelField);
            }
        }
        list.sort(Comparator.comparingInt(ExcelField::writeOrder));
        return list.stream().map(ExcelField::title).collect(Collectors.toList());
    }

    public static String getColumnValue(Object item, int order) {
        List<Field> fields = getAndSaveFields(item.getClass());
        for (Field field : fields) {
            ExcelField ExcelField = field.getAnnotation(ExcelField.class);
            if (null != ExcelField && ExcelField.readOrder() == order) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(item);
                    return asString(field, value);
                } catch (IllegalAccessException e) {
                    return "";
                }
            }
        }
        return "";
    }

    public static List<Field> getAndSaveFields(Class<?> type) {
        List<Field> fields = FIELD_CACHE.getOrDefault(type.getName(), Arrays.asList(type.getDeclaredFields()));

        FIELD_CACHE.putIfAbsent(type.getClass().getName(), fields);
        return fields;
    }

    public static <T> T newInstance(Class<T> type) {
        try {
            return type.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Field getFieldByCols(Class<?> type, int col) {
        List<Field> fields = getAndSaveFields(type);
        for (Field field : fields) {
            ExcelField ExcelField = field.getAnnotation(ExcelField.class);
            if (null != ExcelField && ExcelField.readOrder() == col) {
                return field;
            }
        }
        return null;
    }

    public static void writeToField(Object item, int col, String value) {
        Field field = ExcelUtils.getFieldByCols(item.getClass(), col);
        field.setAccessible(true);
        try {
            field.set(item, asObject(field, value));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Object asObject(Field field, String value) {
        if (field.getType().equals(String.class)) {
            return value;
        }
        if (field.getType().equals(BigDecimal.class)) {
            Object newValue = new BigDecimal(value);
            return newValue;
        }
        if (field.getType().equals(Date.class)) {
            String pattern  = field.getAnnotation(ExcelField.class).datePattern();
            Object newValue = stringToDate(value, pattern);
            return newValue;
        }
        if (field.getType().equals(Long.class)) {
            Object newValue = Long.valueOf(value);
            return newValue;
        }
        if (field.getType().equals(Integer.class)) {
            Object newValue = Integer.valueOf(value);
            return newValue;
        }
        if (field.getType().equals(Double.class)) {
            Object newValue = Double.valueOf(value);
            return newValue;
        }
        if (field.getType().equals(Float.class)) {
            Object newValue = Float.valueOf(value);
            return newValue;
        }
        if (field.getType().equals(Short.class)) {
            Object newValue = Short.valueOf(value);
            return newValue;
        }
        if (field.getType().equals(Byte.class)) {
            Object newValue = Byte.valueOf(value);
            return newValue;
        }
        return value;
    }

    public static String asString(Field field, Object value) {
        if (null == value) {
            return "";
        }
        if (value instanceof BigDecimal) {
            BigDecimal bigDecimal = (BigDecimal) value;
            return bigDecimal.toString();
        }
        if (value instanceof Date) {
            Date   date    = (Date) value;
            String pattern = field.getAnnotation(ExcelField.class).datePattern();
            return dateToString(date, pattern);
        }
        return value.toString();
    }

    private static String dateToString(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);

    }

    private static Date stringToDate(String value, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static long getReadFieldOrders(Class<?> type) {
        return getAndSaveFields(type).stream()
                .map(field -> field.getAnnotation(ExcelField.class))
                .filter(Objects::nonNull)
                .count();
    }
}
