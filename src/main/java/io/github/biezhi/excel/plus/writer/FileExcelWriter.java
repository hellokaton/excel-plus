package io.github.biezhi.excel.plus.writer;

import io.github.biezhi.excel.plus.enums.ExcelType;
import io.github.biezhi.excel.plus.exception.ExcelException;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Write data to Excel file.
 *
 * @author biezhi
 * @date 2018/2/4
 */
public class FileExcelWriter implements ExcelWriter {

    private File savePath;

    public FileExcelWriter(File file) {
        this.savePath = file;
    }

    @Override
    public <T> void export(Exporter<T> exporter) throws ExcelException {
        if (null == savePath) {
            throw new ExcelException("Save the Excel path can not be null.");
        }
        try {
            ExcelType excelType = ExcelType.getExcelType(savePath.getName());
            exporter.setExcelType(excelType);
            this.export(exporter, new FileOutputStream(savePath));
        } catch (Exception e) {
            throw new ExcelException(e);
        }
    }

}
