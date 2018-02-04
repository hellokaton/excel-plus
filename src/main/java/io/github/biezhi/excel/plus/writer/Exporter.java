package io.github.biezhi.excel.plus.writer;

import io.github.biezhi.excel.plus.style.ExcelStyle;

import java.util.Collection;

/**
 * Excel exporter
 *
 * @author biezhi
 * @date 2018/2/4
 */
public class Exporter<T> {

    private Collection<T> data;
    private ExcelStyle    excelStyle;

    public static <T> Exporter<T> create(Collection<T> data, ExcelStyle style) {
        Exporter<T> exporter = new Exporter<>();
        exporter.data = data;
        exporter.excelStyle = style;
        return exporter;
    }

    public Collection<T> getData() {
        return data;
    }

    public ExcelStyle getStyle() {
        return excelStyle;
    }

}
