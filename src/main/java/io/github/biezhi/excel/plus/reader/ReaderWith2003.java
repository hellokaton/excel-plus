/**
 * Copyright (c) 2018, biezhi (biezhi.me@gmail.com)
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
package io.github.biezhi.excel.plus.reader;

import io.github.biezhi.excel.plus.Reader;
import io.github.biezhi.excel.plus.conveter.Converter;
import io.github.biezhi.excel.plus.exception.ConverterException;
import io.github.biezhi.excel.plus.exception.ReaderException;
import io.github.biezhi.excel.plus.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Stream;

/**
 * Reader 2003 Excel
 *
 * @author biezhi
 * @date 2018-12-11
 */
@Slf4j
public class ReaderWith2003 extends ReaderConverter implements ExcelReader {

    private Workbook workbook;

    public ReaderWith2003(Workbook workbook) {
        this.workbook = workbook;
    }

    @Override
    public <T> Stream<T> readExcel(Reader reader) throws ReaderException {
        Class             type    = reader.modelType();
        Stream.Builder<T> builder = Stream.builder();
        try {
            this.initFieldConverter(type.getDeclaredFields());
            Sheet sheet = getSheet(reader);

            int startRow = reader.startRow();
            int totalRow = sheet.getPhysicalNumberOfRows();

            for (int i = 0; i < totalRow; i++) {
                if (i < startRow) {
                    continue;
                }
                Row row = sheet.getRow(i);
                if (null == row) {
                    continue;
                }

                Object instance = type.newInstance();
                for (Field field : fieldIndexes.values()) {
                    this.writeFiledValue(row, instance, field);
                }
                builder.add((T) instance);
            }
            return builder.build();
        } catch (Exception e) {
            throw new ReaderException(e);
        }
    }

    public Sheet getSheet(Reader reader) {
        return StringUtil.isNotEmpty(reader.sheetName()) ?
                workbook.getSheet(reader.sheetName()) : workbook.getSheetAt(reader.sheetIndex());
    }

    public Object getCellValue(Field field, Cell cell) throws ConverterException {
        Converter<String, ?> converter = fieldConverters.get(field);

        if (null == converter) {
            return cell.getStringCellValue();
        }
        if (cell.getCellType() != CellType.NUMERIC) {
            return converter.stringToR(cell.getStringCellValue());
        }
        if (isDateType(field.getType())) {
            Date javaDate = DateUtil.getJavaDate(cell.getNumericCellValue());
            if (field.getType().equals(Date.class)) {
                return javaDate;
            } else if (field.getType().equals(LocalDate.class)) {
                return javaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            } else if (field.getType().equals(LocalDateTime.class)) {
                return javaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            }
            return null;
        } else {
            return converter.stringToR(cell.getNumericCellValue() + "");
        }
    }

    private boolean isDateType(Class<?> type) {
        return Date.class.equals(type) || LocalDate.class.equals(type) || LocalDateTime.class.equals(type);
    }

}
