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
import io.github.biezhi.excel.plus.util.ExcelUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.stream.Stream;

/**
 * Excel Reader Factory
 *
 * @author biezhi
 * @date 2018-12-13
 */
@Slf4j
@UtilityClass
public class ReaderFactory {

    public static <T> Stream<T> readByFile(Reader reader) {
        if (ExcelUtil.isXLSX(reader.fromFile())) {
            return new ReaderWith2007(null).readExcel(reader);
        } else {
            if (ExcelUtil.isCSV(reader.fromFile())) {
                try {
                    return new ReaderWithCSV(new FileInputStream(reader.fromFile())).readExcel(reader);
                } catch (FileNotFoundException e) {
                    throw new ReaderException(reader.fromFile().getName() + " not found", e);
                }
            } else if (ExcelUtil.isXLS(reader.fromFile())) {
                return new ReaderWith2003(ExcelUtil.create(reader.fromFile())).readExcel(reader);
            } else {
                throw new ReaderException(reader.fromFile().getName() + " is the wrong format");
            }
        }
    }

    public static <T> Stream<T> readByStream(Reader reader) {
        if (ExcelUtil.isXLSX(reader.fromStream())) {
            return new ReaderWith2007(null).readExcel(reader);
        } else {
            if (ExcelUtil.isXLS(reader.fromStream())) {
                return new ReaderWith2003(ExcelUtil.create(reader.fromStream())).readExcel(reader);
            } else {
                return new ReaderWithCSV(reader.fromStream()).readExcel(reader);
            }
        }
    }

}
