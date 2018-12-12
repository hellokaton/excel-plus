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
package io.github.biezhi.excel.plus;

import io.github.biezhi.excel.plus.enums.ExcelType;
import io.github.biezhi.excel.plus.exception.WriterException;
import io.github.biezhi.excel.plus.writer.WriterWith2003;
import io.github.biezhi.excel.plus.writer.WriterWith2007;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author biezhi
 * @date 2018-12-11
 */
public class Writer {

    private String sheetName = Constant.DEFAULT_SHEET_NAME;

    private List<?>   rows;
    private int       bufferSize = 100;
    private int       startRow;
    private ExcelType excelType;
    private String    headerTitle;
    private String    templatePath;

    private Consumer<CellStyle> titleStyle;
    private Consumer<CellStyle> headerStyle;
    private Consumer<CellStyle> cellStyle;

    private Map<Predicate<String>, Function<Workbook, CellStyle>> specialColumn = new ConcurrentHashMap<>(16);

    public Writer(ExcelType excelType) {
        this.excelType = excelType;
    }

    public Writer withRows(List<?> rows) {
        this.rows = rows;
        return this;
    }

    public void to(File file) throws WriterException {
        try {
            this.to(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            throw new WriterException(e);
        }
    }

    private void to(OutputStream outputStream) throws WriterException {
        if (excelType == ExcelType.XLSX) {
            new WriterWith2007(outputStream).writeWorkbook(this);
        }
        if (excelType == ExcelType.XLS) {
            new WriterWith2003(outputStream).writeWorkbook(this);
        }
    }

    public Writer sheetName(String sheetName) {
        this.sheetName = sheetName;
        return this;
    }

    public String sheetName() {
        return this.sheetName;
    }

    public Writer headerTitle(String headerTitle) {
        this.headerTitle = headerTitle;
        return this;
    }

    public Writer cellStyle(Consumer<CellStyle> function) {
        this.cellStyle = function;
        return this;
    }

    public Writer specialColumn(Map<Predicate<String>, Function<Workbook, CellStyle>> specialColumn) {
        this.specialColumn.putAll(specialColumn);
        return this;
    }

    public Writer byTemplate(String templatePath) {
        this.templatePath = templatePath;
        return this;
    }

    public Writer startRow(int startRow) {
        this.startRow = startRow;
        return this;
    }

    public int startRow() {
        return this.startRow;
    }

    public Consumer<CellStyle> getTitleStyle() {
        return titleStyle;
    }

    public Consumer<CellStyle> getHeaderStyle() {
        return headerStyle;
    }

    public Consumer<CellStyle> getCellStyle() {
        return cellStyle;
    }

    public Map<Predicate<String>, Function<Workbook, CellStyle>> getSpecialColumn() {
        return specialColumn;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public String headerTitle() {
        return headerTitle;
    }

    /**
     * 该设置只对 2007 格式 Excel 生效
     * 默认缓冲区为 100，可根据写入行数适当调整，如不清楚请勿设置
     *
     * @param bufferSize
     * @return
     */
    public Writer bufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
        return this;
    }

    public int bufferSize() {
        return bufferSize;
    }


    public List<?> getRows() {
        return rows;
    }

}
