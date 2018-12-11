/**
 *  Copyright (c) 2018, biezhi 王爵 (biezhi.me@gmail.com)
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
package io.github.biezhi.excel.plus.writer;

import io.github.biezhi.excel.plus.enums.ExcelType;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Excel exporter
 *
 * @author biezhi
 * @date 2018/2/4
 */
public class Exporter<T> {

    private static final Integer BIG_DATA_EXCEL_BOUNDARY = 10000;

    private String        sheetName;
    private String        headerTitle;
    private String        templatePath;
    private ExcelType     excelType;
    private Collection<T> data;
    private int startRow = 1;

    private Function<Workbook, CellStyle> titleStyle;
    private Function<Workbook, CellStyle> headerStyle;
    private Function<Workbook, CellStyle> columnStyle;
    private Map<Predicate<String>,Function<Workbook, CellStyle>> specialColumn = new ConcurrentHashMap<>(16);

    public static <T> Exporter<T> create(Collection<T> data) {
        Exporter<T> exporter = new Exporter<>();
        exporter.data = data;
        return exporter;
    }

    public Exporter<T> sheetName(String sheetName) {
        this.sheetName = sheetName;
        return this;
    }

    public Exporter<T> title(String title) {
        this.headerTitle = title;
        return this;
    }

    public Exporter<T> titleStyle(Function<Workbook, CellStyle> function) {
        this.titleStyle = function;
        return this;
    }

    public Exporter<T> headerStyle(Function<Workbook, CellStyle> function) {
        this.headerStyle = function;
        return this;
    }

    public Exporter<T> columnStyle(Function<Workbook, CellStyle> function) {
        this.columnStyle = function;
        return this;
    }

    public Exporter<T> specialColumn(Map<Predicate<String>,Function<Workbook, CellStyle>> specialColumn) {
        this.specialColumn.putAll(specialColumn);
        return this;
    }

    public Exporter<T> byTemplate(String templatePath) {
        this.templatePath = templatePath;
        return this;
    }

    public Exporter<T> startRow(int startRow) {
        this.startRow = startRow;
        return this;
    }

    public int startRow() {
        return this.startRow;
    }

    public Function<Workbook, CellStyle> getTitleStyle() {
        return titleStyle;
    }

    public Function<Workbook, CellStyle> getHeaderStyle() {
        return headerStyle;
    }

    public Function<Workbook, CellStyle> getColumnStyle() {
        return columnStyle;
    }

    public Map<Predicate<String>, Function<Workbook, CellStyle>> getSpecialColumn() {
        return specialColumn;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public Collection<T> getData() {
        return data;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setExcelType(ExcelType excelType) {
        this.excelType = excelType;
    }

    public ExcelType getExcelType() {
        if (null == excelType) {
            return ExcelType.XLS;
        }
        return excelType;
    }

    Workbook createWorkbook() {
        if (getExcelType() == ExcelType.XLSX) {
            return getData().size() > BIG_DATA_EXCEL_BOUNDARY ? new SXSSFWorkbook() : new XSSFWorkbook();
        }
        return new HSSFWorkbook();
    }

}
