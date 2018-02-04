package io.github.biezhi.excel.plus.utils;

import io.github.biezhi.excel.plus.Constant;
import io.github.biezhi.excel.plus.annotation.ExcelField;
import io.github.biezhi.excel.plus.annotation.ExcelSheet;
import io.github.biezhi.excel.plus.converter.Converter;

import java.lang.reflect.Field;
import java.math.BigDecimal;
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
                if (excelField.writeOrder() == excelField.order() && excelField.order() == Constant.DEFAULT_ORDER) {
                    System.err.println("[" + type.getName() + "." + field.getName() + "] order config error.");
                }
                list.add(excelField);
            }
        }

        list.sort((o1, o2) -> {
            int order1 = o1.writeOrder() != Constant.DEFAULT_ORDER ? o1.writeOrder() : o1.order();
            int order2 = o2.writeOrder() != Constant.DEFAULT_ORDER ? o2.writeOrder() : o2.order();
            return Integer.compare(order1, order2);
        });

        return list.stream().map(ExcelField::columnName).collect(Collectors.toList());
    }

    public static String getColumnValue(Object item, int order) {
        List<Field> fields = getAndSaveFields(item.getClass());
        for (Field field : fields) {
            ExcelField excelField = field.getAnnotation(ExcelField.class);
            if (null == excelField) {
                continue;
            }
            if (excelField.readOrder() == order || excelField.order() == order) {
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

    private static List<Field> getAndSaveFields(Class<?> type) {
        List<Field> fields = FIELD_CACHE.getOrDefault(type.getName(), Arrays.asList(type.getDeclaredFields()));

        FIELD_CACHE.putIfAbsent(type.getClass().getName(), fields);
        return fields;
    }

    public static <T> T newInstance(Class<T> type) {
        try {
//            T obj = type.getConstructor().newInstance();
            return type.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Field getFieldByCols(Class<?> type, int col) {
        List<Field> fields = getAndSaveFields(type);

        for (Field field : fields) {
            ExcelField excelField = field.getAnnotation(ExcelField.class);
            if (null == excelField) {
                continue;
            }
            if (excelField.readOrder() == col || excelField.order() == col) {
                return field;
            }
        }
        return null;
    }

    public static void writeToField(Object item, int col, String value) {
        Field field = ExcelUtils.getFieldByCols(item.getClass(), col);
        if (null != field) {
            try {
                field.setAccessible(true);
                Object newValue = asObject(field, value);
                field.set(item, newValue);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static Object asObject(Field field, String value) {
        ExcelField excelField = field.getAnnotation(ExcelField.class);
        if (!excelField.convertType().equals(Converter.class)) {
            Converter converter = newInstance(excelField.convertType());
            return converter.read(value);
        }

        if (field.getType().equals(String.class)) {
            return value;
        }
        if (field.getType().equals(BigDecimal.class)) {
            return new BigDecimal(value);
        }
        if (field.getType().equals(Long.class)) {
            return Long.valueOf(value);
        }
        if (field.getType().equals(Integer.class)) {
            return Integer.valueOf(value);
        }
        if (field.getType().equals(Double.class)) {
            return Double.valueOf(value);
        }
        if (field.getType().equals(Float.class)) {
            return Float.valueOf(value);
        }
        if (field.getType().equals(Short.class)) {
            return Short.valueOf(value);
        }
        if (field.getType().equals(Byte.class)) {
            return Byte.valueOf(value);
        }
        return value;
    }

    private static String asString(Field field, Object value) {
        if (null == value) {
            return "";
        }
        ExcelField excelField = field.getAnnotation(ExcelField.class);
        if (!excelField.convertType().equals(Converter.class)) {
            Converter converter = newInstance(excelField.convertType());
            return converter.write(value);
        }
        return value.toString();
    }


    public static long getReadFieldOrders(Class<?> type) {
        return getAndSaveFields(type).stream()
                .map(field -> field.getAnnotation(ExcelField.class))
                .filter(Objects::nonNull)
                .count();
    }
}
