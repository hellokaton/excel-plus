package io.github.biezhi.excel.plus;

import io.github.biezhi.excel.plus.exception.ExcelException;
import io.github.biezhi.excel.plus.reader.ExcelReader;
import io.github.biezhi.excel.plus.style.ExcelStyle;
import io.github.biezhi.excel.plus.writer.Exporter;
import io.github.biezhi.excel.plus.writer.FileExcelWriter;
import io.github.biezhi.excel.plus.writer.ResponseExcelWriter;
import io.github.biezhi.excel.plus.writer.ResponseWrapper;
import jxl.Workbook;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

/**
 * Excel Plus
 *
 * @author biezhi
 * @date 2018/2/4
 */
public class ExcelPlus {

    private Exporter exportor;

    public <T> ExcelPlus export(Collection<T> data) {
        return this.export(data, null);
    }

    public <T> ExcelPlus export(Collection<T> data, ExcelStyle style) {
        this.exportor = Exporter.create(data, style);
        return this;
    }

    public <T> ExcelPlus export(Exporter<T> exportor) {
        this.exportor = exportor;
        return this;
    }

    public void writeAsFile(File file) throws ExcelException {
        new FileExcelWriter(file).export(exportor);
    }

    public void writeAsResponse(ResponseWrapper wrapper) throws ExcelException {
        new ResponseExcelWriter(wrapper).export(exportor);
    }

    public <T> List<T> readAsFile(File file, Class<T> type) throws ExcelException {
        try {
            return new ExcelReader<>(Workbook.getWorkbook(file), type).read();
        } catch (Exception e) {
            throw new ExcelException(e);
        }
    }

    public <T> List<T> readAsStream(InputStream inputStream, Class<T> type) throws ExcelException {
        try {
            return new ExcelReader<>(Workbook.getWorkbook(inputStream), type).read();
        } catch (Exception e) {
            throw new ExcelException(e);
        }
    }
}
