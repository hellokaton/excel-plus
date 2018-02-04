package io.github.biezhi.excel.plus.export;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Excel exporter
 *
 * @author biezhi
 * @date 2018/2/4
 */
public class Exporter<T> {

    private Collection<T> data;
    private File          savePath;

    public static <T> Exporter<T> create(Collection<T> data) {
        Exporter<T> exporter = new Exporter<>();
        exporter.data = data;
        return exporter;
    }

    public Exporter<T> asFile(File file) {
        this.savePath = file;
        return this;
    }

    public Collection<T> getData() {
        return data;
    }

    public File getSavePath() {
        return savePath;
    }

    public String getSheetName() {
        return "";
    }

    public List<String> getFieldNames(T item) {
        return new ArrayList<>();
    }

    public String getColumnValue(T item, int order) {
        return "";
    }

}
