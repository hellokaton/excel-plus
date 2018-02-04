package io.github.biezhi.excel.plus;

import io.github.biezhi.excel.plus.enums.ExcelType;
import io.github.biezhi.excel.plus.exception.ExcelException;
import io.github.biezhi.excel.plus.export.Exporter;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Excel Plus
 *
 * @author biezhi
 * @date 2018/2/4
 */
public class ExcelPlus {

    private Exporter  exporter;
    private ExcelType excelType;

    public <T> void export(Exporter<T> exporter) throws ExcelException {
        Collection<T> data     = exporter.getData();
        File          savePath = exporter.getSavePath();
        if (null == data || data.size() == 0) {
            throw new ExcelException("Export excel data is empty.");
        }
        if (null == savePath) {
            throw new ExcelException("Save the Excel path can not be null.");
        }

        try {
            WritableWorkbook workbook = jxl.Workbook.createWorkbook(savePath);
            WritableSheet    sheet    = workbook.createSheet(exporter.getSheetName(), 0);

            // Set Excel header
            Iterator<T>  iterator = data.iterator();
            T            item     = iterator.next();
            List<String> fields   = exporter.getFieldNames(item);
            for (int i = 0; i < fields.size(); i++) {
                sheet.addCell(new Label(i, 0, fields.get(i)));
            }

            while (iterator.hasNext()) {
                item = iterator.next();
                for (int i = 0; i < fields.size(); i++) {
                    sheet.addCell(new Label(i, i + 1, exporter.getColumnValue(item, i)));
                }
            }
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            throw new ExcelException(e);
        }
    }

}
