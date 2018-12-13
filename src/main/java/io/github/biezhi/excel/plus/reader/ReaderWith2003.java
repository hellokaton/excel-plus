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
import io.github.biezhi.excel.plus.exception.ReaderException;
import io.github.biezhi.excel.plus.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.lang.reflect.Field;
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

            Sheet sheet = StringUtils.isNotEmpty(reader.sheetName()) ?
                    workbook.getSheet(reader.sheetName()) : workbook.getSheetAt(reader.sheetIndex());

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
                    writeFiledValue(row, instance, field);
                }
                builder.add((T) instance);
            }
            return builder.build();
        } catch (Exception e) {
            throw new ReaderException(e);
        }
    }

}
