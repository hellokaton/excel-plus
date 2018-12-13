/**
 *  Copyright (c) 2018, biezhi (biezhi.me@gmail.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.github.biezhi.excel.plus.reader;

import io.github.biezhi.excel.plus.annotation.ExcelColumn;
import io.github.biezhi.excel.plus.conveter.Converter;
import io.github.biezhi.excel.plus.conveter.ConverterCache;
import io.github.biezhi.excel.plus.conveter.NullConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author biezhi
 * @date 2018-12-13
 */
@Slf4j
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

    protected void writeFiledValue(Row row, Object instance, Field field) {
        ExcelColumn column = field.getAnnotation(ExcelColumn.class);

        Cell cell = row.getCell(column.index());
        if (null == cell) {
            return;
        }
        try {
            this.writeToModel(cell.getStringCellValue(), field, instance);
        } catch (Exception e) {
            log.error("write field [%s] value fail", field.getName(), e);
        }
    }

    <T> void writeToModel(String value, Field field, T row) throws Exception {
        Converter converter = fieldConverters.get(field);
        if (null != converter) {
            field.set(row, converter.stringToR(value));
        }
    }

}
