package io.github.biezhi.excel.plus.writer;

import io.github.biezhi.excel.plus.enums.ExcelType;
import org.apache.poi.ss.usermodel.CellStyle;

import java.util.Collection;

/**
 * Excel exporter
 *
 * @author biezhi
 * @date 2018/2/4
 */
public class Exporter<T> {

    private Collection<T> data;
    private ExcelType     excelType;
    private CellStyle     headerStyle;
    private String        templatePath;

    public static <T> Exporter<T> create(Collection<T> data, CellStyle headerStyle) {
        Exporter<T> exporter = new Exporter<>();
        exporter.data = data;
        exporter.headerStyle = headerStyle;
        return exporter;
    }

    public Exporter<T> byTemplate(String templatePath) {
        this.templatePath = templatePath;
        return this;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public Collection<T> getData() {
        return data;
    }

    public CellStyle getHeaderStyle() {
        return headerStyle;
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

}
