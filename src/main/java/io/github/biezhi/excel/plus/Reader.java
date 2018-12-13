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
import io.github.biezhi.excel.plus.utils.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * ExcelPlus Reader
 * <p>
 * Used to read Excel documents
 *
 * @author biezhi
 * @date 2018-12-11
 */
public class Reader {

    /**
     * Java entity type to which the Excel row is mapped
     */
    private Class<?> modelType;

    /**
     * The index of the sheet to be read, default is 0
     */
    private int sheetIndex;

    /**
     * Sheet name to be read, sheetIndex does not take effect if sheetName is configured
     */
    private String sheetName;

    /**
     * Reading data from the first few lines should start with an index that really has data,
     * otherwise an error will occur. please confirm that the parameter is correct when reading.
     * <p>
     * Index starts at 0 instead of 1.
     */
    private int startRow = 2;

    /**
     * Read a excel row from a file
     */
    private File fromFile;

    /**
     * Read a excel row from a InputStream
     */
    private InputStream fromStream;

    public static Reader create(){
        return new Reader();
    }

    public static Reader create(File fromFile){
        return new Reader().from(fromFile);
    }

    public static Reader create(InputStream fromStream){
        return new Reader().from(fromStream);
    }

    /**
     * Read row data from an Excel file
     *
     * @param fromFile excel file object
     * @return Reader
     */
    public Reader from(File fromFile) {
        this.fromFile = fromFile;
        return this;
    }

    /**
     * Read row data from an InputStream
     *
     * @param fromStream excel InputStream
     * @return Reader
     */
    public Reader from(InputStream fromStream) {
        this.fromStream = fromStream;
        return this;
    }

    /**
     * Set the reading from the first few lines, the index starts from 0
     *
     * @param startRow start row index
     * @return Reader
     */
    public Reader startRow(int startRow) {
        if (startRow < 0) {
            throw new IllegalArgumentException("startRow cannot be less than 0");
        }
        this.startRow = startRow;
        return this;
    }

    /**
     * The setting is read from the first sheet, the default is 0
     *
     * @param sheetIndex sheet index
     * @return Reader
     */
    public Reader sheetIndex(int sheetIndex) {
        if (sheetIndex < 0) {
            throw new IllegalArgumentException("sheetIndex cannot be less than 0");
        }
        this.sheetIndex = sheetIndex;
        return this;
    }

    /**
     * Set the name of the sheet to be read. If you set the name, sheetIndex will be invalid.
     *
     * @param sheetName sheet name
     * @return
     */
    public Reader sheetName(String sheetName) {
        if (StringUtils.isEmpty(sheetName)) {
            throw new IllegalArgumentException("sheetName cannot be empty");
        }
        this.sheetName = sheetName;
        return this;
    }

    /**
     * Return the read result as a Stream
     *
     * @param modelType java model type
     * @param <T>       Model Class Type
     * @return Stream
     * @throws ReaderException Thrown when an exception occurs during reading
     */
    public <T> Stream<T> asStream(Class<T> modelType) throws ReaderException {
        this.modelType = modelType;
        if (fromFile == null && fromStream == null) {
            throw new IllegalArgumentException("Excel source not is null.");
        }

        if (fromFile != null) {
            if (ExcelUtils.isXLSX(fromFile)) {
                return new ReaderWith2007(null).readExcel(this);
            } else {
                return new ReaderWith2003(ExcelUtils.create(fromFile)).readExcel(this);
            }
        } else {
            if (ExcelUtils.isXLSX(fromStream)) {
                return new ReaderWith2007(null).readExcel(this);
            } else {
                return new ReaderWith2003(ExcelUtils.create(fromStream)).readExcel(this);
            }
        }
    }

    /**
     * Convert the read result to a List
     *
     * @param modelType java model type
     * @param <T>       Model Class Type
     * @return List
     * @throws ReaderException Thrown when an exception occurs during reading
     */
    public <T> List<T> asList(Class<T> modelType) throws ReaderException {
        Stream<T> stream = this.asStream(modelType);
        return stream.collect(toList());
    }

    public InputStream fromStream() {
        return this.fromStream;
    }

    public File fromFile() {
        return fromFile;
    }

    public <T> Class<T> modelType() {
        return (Class<T>) modelType;
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
