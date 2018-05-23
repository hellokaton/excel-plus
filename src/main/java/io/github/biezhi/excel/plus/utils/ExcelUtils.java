package io.github.biezhi.excel.plus.utils;

import io.github.biezhi.excel.plus.Constant;
import io.github.biezhi.excel.plus.annotation.ExcelField;
import io.github.biezhi.excel.plus.annotation.ReadField;
import io.github.biezhi.excel.plus.annotation.WriteField;
import io.github.biezhi.excel.plus.converter.Converter;
import io.github.biezhi.excel.plus.converter.EmptyConverter;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static io.github.biezhi.excel.plus.Constant.TIP_MSG;

/**
 * Excel utils
 *
 * @author biezhi
 * @date 2018/2/4
 */
public class ExcelUtils {

    private static final Map<String, List<Field>> FIELD_CACHE = new HashMap<>(8);

    public static boolean isEmpty(String value) {
        return null == value || value.trim().isEmpty();
    }

    public static boolean isNotEmpty(String value) {
        return null != value && !value.trim().isEmpty();
    }

    public static boolean isNumber(String value) {
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isDigit(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static List<Pair<Integer, String>> getWriteFieldNames(Class<?> type) {
        List<Field>                 fields = getAndSaveFields(type);
        List<Pair<Integer, String>> pairs  = new ArrayList<>(fields.size());

        for (Field field : fields) {

            WriteField writeField = field.getAnnotation(WriteField.class);
            if (null != writeField) {
                Pair<Integer, String> pair = new Pair<>();
                pair.setV(writeField.columnName());
                if (writeField.order() != Constant.DEFAULT_ORDER) {
                    pair.setK(writeField.order());
                } else {
                    System.err.println(String.format("[%s.%s] order config error, %s", type.getName(), field.getName(), TIP_MSG));
                }
                pairs.add(pair);
            } else {
                ExcelField excelField = field.getAnnotation(ExcelField.class);
                if (null != excelField) {
                    Pair<Integer, String> pair = new Pair<>();
                    pair.setV(excelField.columnName());
                    if (excelField.order() != Constant.DEFAULT_ORDER) {
                        pair.setK(excelField.order());
                    } else {
                        System.err.println(String.format("[%s.%s] order config error, %s", type.getName(), field.getName(), TIP_MSG));
                    }
                    pairs.add(pair);
                }
            }
        }
        return pairs;
    }

    public static String getColumnValue(Object item, int order) {
        List<Field> fields = getAndSaveFields(item.getClass());
        for (Field field : fields) {
            try {
                ReadField readField = field.getAnnotation(ReadField.class);
                if (null != readField) {
                    if (readField.order() == order) {
                        field.setAccessible(true);
                        Object value = field.get(item);
                        return asString(field, value);
                    }
                } else {
                    ExcelField excelField = field.getAnnotation(ExcelField.class);
                    if (null == excelField) {
                        WriteField writeField = field.getAnnotation(WriteField.class);
                        if(null == writeField || writeField.order() != order){
                            return "";
                        }
                        field.setAccessible(true);
                        Object value = field.get(item);
                        return asString(field, value);
                    } else {
                        if (excelField.order() == order) {
                            field.setAccessible(true);
                            Object value = field.get(item);
                            return asString(field, value);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
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

    private static Field getFieldByCols(Class<?> type, int col) {
        List<Field> fields = getAndSaveFields(type);

        for (Field field : fields) {
            ReadField readField = field.getAnnotation(ReadField.class);
            if (null != readField) {
                if (readField.order() == col) {
                    return field;
                }
            } else {
                ExcelField excelField = field.getAnnotation(ExcelField.class);
                if (null == excelField) {
                    continue;
                }
                if (excelField.order() == col) {
                    return field;
                }
            }
        }
        return null;
    }

    public static void writeToField(Object item, int col, String value) {
        Field field = ExcelUtils.getFieldByCols(item.getClass(), col);
        if (null != field) {
            writeToField(item, field, value);
        }
    }

    public static void writeToField(Object item, Field field, String value) {
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
        if (null == value || "".equals(value)) {
            return null;
        }
        ExcelField excelField = field.getAnnotation(ExcelField.class);
        if (null != excelField && !excelField.convertType().equals(EmptyConverter.class)) {
            Converter converter = newInstance(excelField.convertType());
            if (null != converter) {
                return converter.read(value);
            }
        }
        if (field.getType().equals(String.class)) {
            return value;
        }
        if (field.getType().equals(BigDecimal.class)) {
            return new BigDecimal(value);
        }
        if (field.getType().equals(Long.class) || field.getType().equals(long.class)) {
            return new BigDecimal(value).longValue();
        }
        if (field.getType().equals(Integer.class) || field.getType().equals(int.class)) {
            return new BigDecimal(value).intValue();
        }
        if (field.getType().equals(Double.class) || field.getType().equals(double.class)) {
            return Double.valueOf(value);
        }
        if (field.getType().equals(Float.class) || field.getType().equals(float.class)) {
            return Float.valueOf(value);
        }
        if (field.getType().equals(Short.class) || field.getType().equals(short.class)) {
            return Short.valueOf(value);
        }
        if (field.getType().equals(Byte.class) || field.getType().equals(byte.class)) {
            return Byte.valueOf(value);
        }
        if (field.getType().equals(Boolean.class) || field.getType().equals(boolean.class)) {
            return Boolean.valueOf(value);
        }

        WriteField writeField = field.getAnnotation(WriteField.class);
        if (field.getType().equals(Date.class)) {
            if (null != writeField && !"".equals(writeField.datePattern())) {
                return DateUtils.toDate(value, writeField.datePattern());
            }
            if ((null != excelField) && !"".equals(excelField)) {
                return DateUtils.toDate(value, excelField.datePattern());
            }
        }
        if (field.getType().equals(LocalDate.class)) {
            if (null != writeField && !"".equals(writeField.datePattern())) {
                return DateUtils.toLocalDate(value, writeField.datePattern());
            }
            if (null != excelField && !"".equals(excelField)) {
                return DateUtils.toLocalDate(value, excelField.datePattern());
            }
        }
        if (field.getType().equals(LocalDateTime.class)) {
            if (null != writeField && !"".equals(writeField.datePattern())) {
                return DateUtils.toLocalDateTime(value, writeField.datePattern());
            }
            if (null != excelField && !"".equals(excelField)) {
                return DateUtils.toLocalDateTime(value, excelField.datePattern());
            }
        }
        return value;
    }

    private static <T> String asString(Field field, T value) {
        if (null == value) {
            return "";
        }
        ExcelField excelField = field.getAnnotation(ExcelField.class);
        if (null != excelField && !excelField.convertType().equals(EmptyConverter.class)) {
            Converter converter = newInstance(excelField.convertType());
            if (null != converter) {
                return converter.write(value);
            }
        }

        ReadField readField = field.getAnnotation(ReadField.class);

        if (value instanceof Date) {
            if (null != readField && !"".equals(readField.datePattern())) {
                return DateUtils.toString((Date) value, readField.datePattern());
            }
            if (null != excelField && !"".equals(excelField)) {
                return DateUtils.toString((Date) value, excelField.datePattern());
            }
        }

        if (value instanceof LocalDate) {
            if (null != readField && !"".equals(readField.datePattern())) {
                return DateUtils.toString((LocalDate) value, readField.datePattern());
            }
            if (null != excelField && !"".equals(excelField)) {
                return DateUtils.toString((LocalDate) value, excelField.datePattern());
            }
        }

        if (value instanceof LocalDateTime) {
            if (null != readField && !"".equals(readField.datePattern())) {
                return DateUtils.toString((LocalDateTime) value, readField.datePattern());
            }
            if (null != excelField && !"".equals(excelField)) {
                return DateUtils.toString((LocalDateTime) value, excelField.datePattern());
            }
        }

        return value.toString();
    }

    private static ThreadLocal<DecimalFormat> decimalFormatThreadLocal = ThreadLocal.withInitial(() -> new DecimalFormat("#"));

    public static String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        switch (cell.getCellTypeEnum()) {
            case NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    try {
                        return DateUtils.toString(cell.getDateCellValue(), null);
                    } catch (Exception e) {
                        return cell.getDateCellValue().getTime() + "";
                    }
                }
                cellValue = String.valueOf(cell.getNumericCellValue());
                if (cellValue.contains("E")) {
                    cellValue = decimalFormatThreadLocal.get().format(cell.getNumericCellValue());
                }
                break;
            case STRING:
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA:
                try {
                    cellValue = String.valueOf(cell.getStringCellValue());
                } catch (Exception e) {
                    cellValue = String.valueOf(cell.getNumericCellValue());
                }
                break;
            case BLANK:
                cellValue = "";
                break;
            case ERROR:
                cellValue = "illegal character";
                break;
            default:
                cellValue = "Unknown type";
                break;
        }
        return cellValue;
    }

    public static int getMaxOrder(Class<?> type) {
        return Arrays.stream(type.getDeclaredFields())
                .filter(field -> null != field.getAnnotation(ExcelField.class))
                .map(field -> field.getAnnotation(ExcelField.class))
                .map(ExcelField::order)
                .max(Integer::compareTo).orElse(0);
    }

}
