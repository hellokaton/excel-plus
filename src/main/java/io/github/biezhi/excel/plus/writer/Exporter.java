package io.github.biezhi.excel.plus.writer;

import io.github.biezhi.excel.plus.enums.ExcelType;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Collection;
import java.util.function.Function;

/**
 * Excel exporter
 *
 * @author biezhi
 * @date 2018/2/4
 */
public class Exporter<T> {

    private static final Integer BIG_DATA_EXCEL_BOUNDARY = 10000;

    private String        headerTitle;
    private String        templatePath;
    private ExcelType     excelType;
    private Collection<T> data;
    private int startRow = 1;

    private Function<Workbook, CellStyle> titleStyle;
    private Function<Workbook, CellStyle> headerStyle;
    private Function<Workbook, CellStyle> columnStyle;

    public static <T> Exporter<T> create(Collection<T> data) {
        Exporter<T> exporter = new Exporter<>();
        exporter.data = data;
        return exporter;
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

    public String getTemplatePath() {
        return templatePath;
    }

    public Collection<T> getData() {
        return data;
    }

    public String getHeaderTitle() {
        return headerTitle;
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
            return getData().size() > BIG_DATA_EXCEL_BOUNDARY ? new SXSSFWorkbook(): new XSSFWorkbook();
        }
        return new HSSFWorkbook();
    }

}
