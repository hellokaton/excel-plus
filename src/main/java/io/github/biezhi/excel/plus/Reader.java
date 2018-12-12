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
package io.github.biezhi.excel.plus;

import io.github.biezhi.excel.plus.exception.ReaderException;
import io.github.biezhi.excel.plus.reader.ReaderWith2003;
import io.github.biezhi.excel.plus.reader.ReaderWith2007;
import io.github.biezhi.excel.plus.utils.ExcelUtils;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author biezhi
 * @date 2018-12-11
 */
public class Reader {

    private Class<?>    type;
    private int         sheetIndex;
    private int         startRow = 2;
    private String      sheetName;
    private File        fromFile;
    private InputStream fromStream;

    public Reader from(File from) {
        this.fromFile = from;
        return this;
    }

    public Reader from(FileInputStream from) {
        this.fromStream = from;
        return this;
    }

    public Reader startRow(int startRow) {
        this.startRow = startRow;
        return this;
    }

    public Reader sheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
        return this;
    }

    public Reader sheetName(String sheetName) {
        this.sheetName = sheetName;
        return this;
    }

    public <T> Stream<T> asStream(Class<T> type) throws ReaderException {
        this.type = type;
        if (this.fromFile == null && this.fromStream == null) {
            throw new ReaderException("Excel source not is null.");
        }

        try {
            if (this.fromFile != null) {
                if (ExcelUtils.isXLSX(fromFile)) {
                    return new ReaderWith2007(null).readExcel(this);
                } else {
                    return new ReaderWith2003(WorkbookFactory.create(fromFile)).readExcel(this);
                }
            } else {
                if (ExcelUtils.isXLSX(fromStream)) {
                    return new ReaderWith2007(null).readExcel(this);
                } else {
                    return new ReaderWith2003(WorkbookFactory.create(fromStream)).readExcel(this);
                }
            }
        } catch (Exception e) {
            throw new ReaderException(e);
        }
    }

    public <T> List<T> asList(Class<T> type) throws ReaderException {
        Stream<T> stream = this.asStream(type);
        return stream.collect(toList());
    }

    public InputStream inputStream() {
        return this.fromStream;
    }

    public File getFromFile() {
        return fromFile;
    }

    public <T> Class<T> getType() {
        return (Class<T>) type;
    }

    public int sheetIndex() {
        return this.sheetIndex;
    }

    public String sheetName() {
        return this.sheetName;
    }

    public int startRow() {
        return this.startRow;
    }

}
