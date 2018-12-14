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
import io.github.biezhi.excel.plus.annotation.ExcelColumn;
import io.github.biezhi.excel.plus.conveter.Converter;
import io.github.biezhi.excel.plus.exception.ReaderException;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

/**
 * CSV Reader
 *
 * @author biezhi
 * @date 2018-12-13
 */
@Slf4j
public class ReaderWithCSV extends ReaderConverter implements ExcelReader {

    private InputStream inputStream;

    public ReaderWithCSV(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public <T> Stream<T> readExcel(Reader reader) throws ReaderException {
        Class type = reader.modelType();

        try {
            this.initFieldConverter(type.getDeclaredFields());
        } catch (Exception e) {
            e.printStackTrace();
        }

        int startRow = reader.startRow();

        Stream.Builder<T> builder = Stream.builder();

        String line;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            int pos = 0;

            while ((line = br.readLine()) != null) {
                if (pos < startRow) {
                    continue;
                }
                Object instance = type.newInstance();

                String[] csvLine = line.split(",");

                for (Field field : fieldIndexes.values()) {
                    ExcelColumn column = field.getAnnotation(ExcelColumn.class);
                    try {
                        Object    cellValue = csvLine[column.index()];
                        Converter converter = fieldConverters.get(field);
                        if (null != converter) {
                            cellValue = converter.stringToR(csvLine[column.index()]);
                        }
                        field.set(instance, cellValue);
                    } catch (Exception e) {
                        log.error("write value {} to field {} failed", csvLine[column.index()], field.getName(), e);
                    }
                }
                builder.add((T) instance);
                pos++;
            }
            return builder.build();
        } catch (Exception e) {
            throw new ReaderException(e);
        }
    }

}
