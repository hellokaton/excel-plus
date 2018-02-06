package io.github.biezhi.excel.plus.writer;

import io.github.biezhi.excel.plus.enums.ExcelType;
import io.github.biezhi.excel.plus.exception.ExcelException;
import io.github.biezhi.excel.plus.utils.ExcelUtils;
import io.github.biezhi.excel.plus.utils.Pair;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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

            T data0 = data.iterator().next();
            // Set Excel header
            Iterator<T> iterator = data.iterator();

            List<Pair<Integer, String>> writeFieldNames = ExcelUtils.getWriteFieldNames(data0.getClass());

            int startRow = exporter.startRow();

            if (null != exporter.getTemplatePath()) {
                InputStream in = ExcelWriter.class.getClassLoader().getResourceAsStream(exporter.getTemplatePath());
                workbook = WorkbookFactory.create(in);
                sheet = workbook.getSheetAt(0);
            } else {
                workbook = exporter.getExcelType().equals(ExcelType.XLSX) ? new XSSFWorkbook() : new HSSFWorkbook();
                sheet = workbook.createSheet(ExcelUtils.getSheetName(data0));

                if (null != exporter.getHeaderStyle()) {
                    headerStyle = exporter.getHeaderStyle().apply(workbook);
                } else {
                    headerStyle = defaultHeaderStyle(workbook);
                }
                if (null != exporter.getColumnStyle()) {
                    columnStyle = exporter.getColumnStyle().apply(workbook);
                } else {
                    columnStyle = defaultColumnStyle(workbook);
                }

                this.writeRowHead(headerStyle, sheet, writeFieldNames);
            }

            List<Integer> columnIndexes = writeFieldNames.stream().map(Pair::getK).collect(Collectors.toList());

            this.writeRows(sheet, columnStyle, null, iterator, startRow, columnIndexes);

            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            throw new ExcelException(e);
        }
    }

    /**
     * Write the header row to Sheet.
     *
     * @param headerStyle header row cell style
     * @param sheet       work sheet
     * @param columnNames column names
     */
    default void writeRowHead(CellStyle headerStyle, Sheet sheet, List<Pair<Integer, String>> columnNames) {
        Row rowHead = sheet.createRow(0);
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
     * The default Excel header style.
     *
     * @param workbook Excel workbook
     * @return header row cell style
     */
    default CellStyle defaultHeaderStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();

        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);

        headerStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Font headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        return headerStyle;
    }

    /**
     * The default Excel column style.
     *
     * @param workbook Excel workbook
     * @return row column cell style
     */
    default CellStyle defaultColumnStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setWrapText(true);

        Font cellFont = workbook.createFont();
        cellStyle.setFont(cellFont);
        return cellStyle;
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
