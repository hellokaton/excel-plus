package io.github.biezhi.excel.plus.reader;

import io.github.biezhi.excel.plus.annotation.ExcelColumn;
import io.github.biezhi.excel.plus.conveter.Converter;
import io.github.biezhi.excel.plus.conveter.ConverterCache;
import io.github.biezhi.excel.plus.conveter.NullConverter;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author biezhi
 * @date 2018-12-13
 */
public class ReaderConverter {

    Map<Integer, Field> fieldIndexes;

    private Map<Field, Converter<String, ?>> fieldConverters;

    void initFieldConverter(Field[] fields) throws Exception {
        this.fieldConverters = new HashMap<>();
        this.fieldIndexes = new HashMap<>(fields.length);

        for (Field field : fields) {
            ExcelColumn column = field.getAnnotation(ExcelColumn.class);
            if (null == column) {
                continue;
            }
            field.setAccessible(true);
            fieldIndexes.put(column.index(), field);

            Converter converter;
            if (NullConverter.class.equals(column.converter())) {
                converter = ConverterCache.computeConvert(field);
            } else {
                converter = column.converter().newInstance();
            }
            if (null != converter) {
                fieldConverters.put(field, converter);
            }
        }
    }

    <T> void writeToModel(String value, Field field, T row) throws Exception {
        Converter converter = fieldConverters.get(field);
        if (null != converter) {
            field.set(row, converter.stringToR(value));
        }
    }

}
