package io.github.biezhi.excel.plus.writer;

import io.github.biezhi.excel.plus.exception.ExcelException;
import jxl.Workbook;

import java.io.File;

/**
 * @author biezhi
 * @date 2018/2/4
 */
public class FileExcelWriter implements ExcelWriter {

    private File savePath;

    public FileExcelWriter(File file) {
        this.savePath = file;
    }

    @Override
    public <T> void export(Exporter<T> exportor) throws ExcelException {
        if (null == savePath) {
            throw new ExcelException("Save the Excel path can not be null.");
        }
        try {
            this.export(exportor, Workbook.createWorkbook(savePath));
        } catch (Exception e) {
            throw new ExcelException(e);
        }
    }

}
