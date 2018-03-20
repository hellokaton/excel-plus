package io.github.biezhi.excel.plus.writer;

import io.github.biezhi.excel.plus.Constant;
import io.github.biezhi.excel.plus.exception.ExcelException;
import io.github.biezhi.excel.plus.utils.ExcelUtils;
import io.github.biezhi.excel.plus.utils.Pair;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Exporter interface
 *
 * @author biezhi
 * @date 2018/2/4
 */
public interface ExcelWriter extends Constant {

    /**
     * Default Export method
     *
     * @param exporter     Exporter Object
     * @param outputStream OutputStream
     * @param <T>          Java Type
     * @throws ExcelException thrown when exporting Excel to an exception
     */
    default <T> void export(Exporter<T> exporter, OutputStream outputStream) throws ExcelException {
        Collection<T> data = exporter.getData();
        if (null == data || data.size() == 0) {
            throw new ExcelException("Export excel data is empty.");
        }
        try {

            Sheet     sheet;
            Workbook  workbook;
            CellStyle headerStyle;
            CellStyle columnStyle = null;
            CellStyle titleStyle;

            T data0 = data.iterator().next();
            // Set Excel header
            Iterator<T> iterator = data.iterator();

            List<Pair<Integer, String>> writeFieldNames = ExcelUtils.getWriteFieldNames(data0.getClass());

            List<Integer> columnIndexes = writeFieldNames.stream().map(Pair::getK).collect(Collectors.toList());

            int startRow = exporter.startRow();

            if (null != exporter.getTemplatePath()) {
                InputStream in = ExcelWriter.class.getClassLoader().getResourceAsStream(exporter.getTemplatePath());
                workbook = WorkbookFactory.create(in);
                sheet = workbook.getSheetAt(0);

            } else {
                workbook = exporter.getExcelType().equals(ExcelType.XLSX) ? new XSSFWorkbook() : new HSSFWorkbook();
                sheet = workbook.createSheet(ExcelUtils.getSheetName(data0.getClass()));

                if (null != exporter.getTitleStyle()) {
                    titleStyle = exporter.getTitleStyle().apply(workbook);
                } else {
                    titleStyle = this.defaultTitleStyle(workbook);
                }

                if (null != exporter.getHeaderStyle()) {
                    headerStyle = exporter.getHeaderStyle().apply(workbook);
                } else {
                    headerStyle = this.defaultHeaderStyle(workbook);
                }

                if (null != exporter.getColumnStyle()) {
                    columnStyle = exporter.getColumnStyle().apply(workbook);
                } else {
                    columnStyle = this.defaultColumnStyle(workbook);
                }

                String headerTitle = exporter.getHeaderTitle();
                int    colIndex    = 0;
                if (null != headerTitle) {
                    colIndex = 1;
                    int maxColIndex = columnIndexes.stream().max(Comparator.comparingInt(Integer::intValue)).get();
                    this.writeTitleRow(titleStyle, sheet, headerTitle, maxColIndex);
                }
                this.writeColumnNames(colIndex, headerStyle, sheet, writeFieldNames);
                startRow += colIndex;

            }

            this.writeRows(sheet, columnStyle, null, iterator, startRow, columnIndexes);

            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            throw new ExcelException(e);
        }
    }

    default void writeTitleRow(CellStyle cellStyle, Sheet sheet, String title, int maxColIndex) {
        Row titleRow = sheet.createRow(0);
        for (int i = 0; i <= maxColIndex; i++) {
            Cell cell = titleRow.createCell(i);
            if (i == 0) {
                cell.setCellValue(title);
            }
            cell.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, maxColIndex));
    }

    /**
     * Write the header row to Sheet.
     *
     * @param rowIndex    start row index
     * @param headerStyle header row cell style
     * @param sheet       work sheet
     * @param columnNames column names
     */
    default void writeColumnNames(int rowIndex, CellStyle headerStyle, Sheet sheet, List<Pair<Integer, String>> columnNames) {
        Row rowHead = sheet.createRow(rowIndex);
        columnNames.forEach(pair -> {
            Integer colIndex   = pair.getK();
            String  columnName = pair.getV();
            Cell    cell       = rowHead.createCell(colIndex);
            if (null != headerStyle) {
                cell.setCellStyle(headerStyle);
            }
            cell.setCellValue(columnName);
        });
    }

    /**
     * Write line data
     *
     * @param sheet       work sheet
     * @param columnStyle each column style in the row.
     * @param rowStyle    row style
     * @param iterator    row data iterator
     * @param startRow    from the beginning of the line, the default is 1
     * @param <T>         Java Type
     */
    default <T> void writeRows(Sheet sheet, CellStyle columnStyle, CellStyle rowStyle, Iterator<T> iterator, int startRow, List<Integer> columnIndexes) {
        for (int rowNum = startRow; iterator.hasNext(); rowNum++) {
            T   item = iterator.next();
            Row row  = sheet.createRow(rowNum);
            if (null != rowStyle) {
                row.setRowStyle(rowStyle);
            }
            Iterator<Integer> colIt = columnIndexes.iterator();
            while (colIt.hasNext()) {
                int    col   = colIt.next();
                Cell   cell  = row.createCell(col);
                String value = ExcelUtils.getColumnValue(item, col);
                if (null != columnStyle) {
                    cell.setCellStyle(columnStyle);
                }
                cell.setCellValue(value);
                sheet.autoSizeColumn(col);
            }
        }
    }

    /**
     * Export excel
     *
     * @param exporter Exporter Object
     * @param <T>      Java Type
     * @throws ExcelException thrown when exporting Excel to an exception
     */
    <T> void export(Exporter<T> exporter) throws ExcelException;

}
