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

import io.github.biezhi.excel.plus.enums.ExcelType;
import io.github.biezhi.excel.plus.exception.WriterException;
import io.github.biezhi.excel.plus.utils.StringUtils;
import io.github.biezhi.excel.plus.writer.WriterWith2003;
import io.github.biezhi.excel.plus.writer.WriterWith2007;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * ExcelPlus Writer
 * <p>
 * Used to write Excel documents
 *
 * @author biezhi
 * @date 2018-12-11
 */
public class Writer {

    /**
     * The name of the Sheet to be written to Excel. The default is Sheet0.
     */
    private String sheetName = Constant.DEFAULT_SHEET_NAME;

    /**
     * Store the row to be written
     */
    private Collection<?> rows;

    /**
     * Write from the first few lines,
     * the default is automatic calculation, calculated by Excel title and column,
     * may be 1 or 2
     */
    private int startRow;

    /**
     * Buffer when writing a document in xlsx format
     */
    private int bufferSize = 100;

    /**
     * Type of excel written, select XLSX, XLS, CSV
     */
    private ExcelType excelType;

    /**
     * Write the title of Excel, optional
     */
    private String headerTitle;

    /**
     * Specify the path to the template by writing data according to the specified template
     */
    private String templatePath;

    /**
     * Custom title style
     */
    private BiConsumer<Workbook, CellStyle> titleStyle;

    /**
     * Custom column header style
     */
    private BiConsumer<Workbook, CellStyle> headerStyle;

    /**
     * Custom row column style
     */
    private BiConsumer<Workbook, CellStyle> cellStyle;

    public Writer(ExcelType excelType) {
        this.excelType = excelType;
    }

    /**
     * Set the data to be written, receive a collection
     *
     * @param rows row data
     * @return Writer
     */
    public Writer withRows(Collection<?> rows) {
        this.rows = rows;
        return this;
    }

    /**
     * Configure the name of the sheet to be written. The default is Sheet0.
     *
     * @param sheetName sheet name
     * @return Writer
     */
    public Writer sheetName(String sheetName) {
        if (StringUtils.isEmpty(sheetName)) {
            throw new IllegalArgumentException("sheetName cannot be empty");
        }
        this.sheetName = sheetName;
        return this;
    }

    /**
     * Set the data to be written from the first few lines.
     * By default, the value is calculated. It is recommended not to modify it.
     *
     * @param startRow start row index
     * @return Writer
     */
    public Writer startRow(int startRow) {
        if (startRow < 0) {
            throw new IllegalArgumentException("startRow cannot be less than 0");
        }
        this.startRow = startRow;
        return this;
    }

    /**
     * Set the title of the Excel table, do not write the title without setting
     *
     * @param title excel title
     * @return Writer
     */
    public Writer headerTitle(String title) {
        this.headerTitle = title;
        return this;
    }

    /**
     * Sets the style of the Excel title,
     * which is modified by the default style to receive a CellStyle object
     *
     * @param titleStyle title style consumer
     * @return Writer
     */
    public Writer titleStyle(BiConsumer<Workbook, CellStyle> titleStyle) {
        this.titleStyle = titleStyle;
        return this;
    }

    /**
     * Sets the style of the Excel column header,
     * which is modified by the default style to receive a CellStyle object
     *
     * @param headerStyle header style consumer
     * @return Writer
     */
    public Writer headerStyle(BiConsumer<Workbook, CellStyle> headerStyle) {
        this.headerStyle = headerStyle;
        return this;
    }

    /**
     * Sets the style of the Excel row column,
     * which is modified by the default style to receive a CellStyle object
     *
     * @param cellStyle row style consumer
     * @return Writer
     */
    public Writer cellStyle(BiConsumer<Workbook, CellStyle> cellStyle) {
        this.cellStyle = cellStyle;
        return this;
    }

    /**
     * Specify to write an Excel table from a template file
     *
     * @param templatePath template file path
     * @return Writer
     */
    public Writer templatePath(String templatePath) {
        this.templatePath = templatePath;
        return this;
    }

    /**
     * This setting is only valid for xlsx format Excel
     * <p>
     * The default buffer is 100, which can be adjusted according to the number of write lines.
     * <p>
     * If you are not sure, please do not set
     *
     * @param bufferSize
     * @return
     */
    public Writer bufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
        return this;
    }

    /**
     * Write an Excel document to a file
     *
     * @param file excel file
     * @throws WriterException
     */
    public void to(File file) throws WriterException {
        try {
            this.to(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            throw new WriterException(e);
        }
    }

    /**
     * Write an Excel document to the output stream
     *
     * @param outputStream outputStream
     * @throws WriterException
     */
    public void to(OutputStream outputStream) throws WriterException {
        if (null == rows || rows.isEmpty()) {
            throw new WriterException("write rows cannot be empty, please check it");
        }
        if (excelType == ExcelType.XLSX) {
            new WriterWith2007(outputStream).writeSheet(this);
        }
        if (excelType == ExcelType.XLS) {
            new WriterWith2003(outputStream).writeSheet(this);
        }
    }

    public int startRow() {
        return this.startRow;
    }

    public String sheetName() {
        return this.sheetName;
    }

    public BiConsumer<Workbook, CellStyle> titleStyle() {
        return this.titleStyle;
    }

    public BiConsumer<Workbook, CellStyle> headerStyle() {
        return this.headerStyle;
    }

    public BiConsumer<Workbook, CellStyle> cellStyle() {
        return this.cellStyle;
    }

    public String templatePath() {
        return this.templatePath;
    }

    public String headerTitle() {
        return this.headerTitle;
    }

    public int bufferSize() {
        return bufferSize;
    }

    public Collection<?> rows() {
        return rows;
    }

}
