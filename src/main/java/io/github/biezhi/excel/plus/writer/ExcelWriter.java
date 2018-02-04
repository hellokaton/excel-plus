package io.github.biezhi.excel.plus.writer;

import io.github.biezhi.excel.plus.exception.ExcelException;
import io.github.biezhi.excel.plus.utils.ExcelUtils;
import jxl.CellView;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Exporter interface
 *
 * @author biezhi
 * @date 2018/2/4
 */
public interface ExcelWriter {

    /**
     * Default Export method
     *
     * @param exporter
     * @param workbook
     * @param <T>
     * @throws ExcelException
     */
    default <T> void export(Exporter<T> exporter, WritableWorkbook workbook) throws ExcelException {
        Collection<T> data = exporter.getData();
        if (null == data || data.size() == 0) {
            throw new ExcelException("Export excel data is empty.");
        }
        try {
            T             data0    = data.iterator().next();
            WritableSheet sheet    = workbook.createSheet(ExcelUtils.getSheetName(data0), 0);
            CellView      cellView = new CellView();
            cellView.setAutosize(true);
            sheet.setColumnView(1, cellView);

            // Set Excel header
            Iterator<T>  iterator   = data.iterator();
            List<String> fieldNames = ExcelUtils.getWriteFieldNames(data0.getClass());
            int          cols       = fieldNames.size();

            for (int i = 0; i < cols; i++) {
                sheet.addCell(new Label(i, 0, fieldNames.get(i), getHeader()));
            }

            for (int row = 1; iterator.hasNext(); row++) {
                T item = iterator.next();
                for (int col = 0; col < cols; col++) {
                    sheet.addCell(new Label(col, row, ExcelUtils.getColumnValue(item, col)));
                }
            }
            workbook.write();
        } catch (Exception e) {
            throw new ExcelException(e);
        } finally {
            try {
                workbook.close();
            } catch (Exception ignored) {
            }
        }
    }

    default WritableCellFormat getHeader() {
        WritableFont font = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
        try {
            font.setColour(Colour.BLACK);
        } catch (WriteException e1) {
            e1.printStackTrace();
        }
        WritableCellFormat format = new WritableCellFormat(font);
        try {
            format.setAlignment(jxl.format.Alignment.CENTRE);
            format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
            format.setBackground(Colour.GRAY_25);
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return format;
    }

    /**
     * Export excel
     *
     * @param exporter
     * @param <T>
     * @throws ExcelException
     */
    <T> void export(Exporter<T> exporter) throws ExcelException;

}
