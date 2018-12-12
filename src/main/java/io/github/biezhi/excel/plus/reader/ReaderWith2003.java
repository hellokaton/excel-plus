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

import io.github.biezhi.excel.plus.Reader;
import io.github.biezhi.excel.plus.annotation.ExcelColumn;
import io.github.biezhi.excel.plus.exception.ReaderException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.lang.reflect.Field;
import java.util.stream.Stream;

/**
 * @author biezhi
 * @date 2018-12-11
 */
public class ReaderWith2003 implements ExcelReader {

    private Workbook workbook;

    public ReaderWith2003(Workbook workbook) {
        this.workbook = workbook;
    }

    @Override
    public <T> Stream<T> readExcel(Reader reader) throws ReaderException {
        Class             type    = reader.getType();
        Stream.Builder<T> builder = Stream.builder();
        try {
            Sheet sheet = workbook.getSheetAt(reader.sheetIndex());
            for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    T item = (T) rowToObject(type, row);
                    builder.add(item);
                }
            }
            return builder.build();
        } catch (Exception e) {
            throw new ReaderException(e);
        }
    }

    protected <T> T rowToObject(Class<T> type, Row row) throws Exception {
        T       instance       = type.newInstance();
        Field[] declaredFields = type.getDeclaredFields();
        for (Field field : declaredFields) {
            ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
            if (excelColumn == null) {
                continue;
            }
            int  index = excelColumn.index();
            Cell cell  = row.getCell(index);
            if (cell != null) {
                field.setAccessible(true);
                field.set(index, cell.getStringCellValue());
            }
        }
        return instance;
    }

}
