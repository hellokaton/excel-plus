/**
 * Copyright (c) 2018, biezhi (biezhi.me@gmail.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.biezhi.excel.plus.writer;

import io.github.biezhi.excel.plus.Constant;
import io.github.biezhi.excel.plus.Writer;
import io.github.biezhi.excel.plus.annotation.ExcelColumn;
import io.github.biezhi.excel.plus.conveter.*;
import io.github.biezhi.excel.plus.exception.WriterException;
import io.github.biezhi.excel.plus.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.Comparator.comparingInt;

/**
 * ExcelWriter
 *
 * @author biezhi
 * @date 2018-12-11
 */
@Slf4j
public abstract class ExcelWriter {

    private int                 rowNum;
    private Sheet               sheet;
    private Map<Integer, Field> fieldIndexes;
    private List<ExcelColumn>   columns;

    OutputStream outputStream;

    ExcelWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    ExcelWriter() {
    }

    public abstract void writeSheet(Writer writer) throws WriterException;

    /**
     * Write data to Excel Sheet
     * <p>
     * 1. create sheet
     * 2. write title(optional)
     * 3. write column header
     * 4. write row
     * 5. write to OutputStream
     *
     * @param writer   excel-plus writer
     * @param workbook workbook
     * @throws WriterException
     */
    void writeSheet(Writer writer, Workbook workbook) throws WriterException {
        // create sheet
        this.sheet = workbook.createSheet(writer.sheetName());

        // setting styles
        CellStyle headerStyle = Constant.defaultHeaderStyle(workbook);
        CellStyle columnStyle = Constant.defaultColumnStyle(workbook);
        CellStyle titleStyle  = Constant.defaultTitleStyle(workbook);
        if (null != writer.titleStyle()) {
            writer.titleStyle().accept(workbook, titleStyle);
        }
        if (null != writer.headerStyle()) {
            writer.headerStyle().accept(workbook, headerStyle);
        }
        if (null != writer.cellStyle()) {
            writer.cellStyle().accept(workbook, columnStyle);
        }

        if (writer.isRaw()) {
            writer.sheetConsumer().accept(sheet);
        } else {
            // compute the Filed to be written
            Collection<?> rows   = writer.rows();
            Field[]       fields = rows.iterator().next().getClass().getDeclaredFields();

            this.fieldIndexes = new HashMap<>(fields.length);
            this.columns = new ArrayList<>();

            for (Field field : fields) {
                ExcelColumn column = field.getAnnotation(ExcelColumn.class);
                if (null != column) {
                    field.setAccessible(true);
                    fieldIndexes.put(column.index(), field);
                    columns.add(column);
                }
            }

            int colRowIndex = 0;
            // write title
            String title = writer.headerTitle();
            if (StringUtils.isNotEmpty(title)) {
                Integer maxColIndex = columns.stream()
                        .map(ExcelColumn::index)
                        .max(comparingInt(Integer::intValue))
                        .get();

                this.writeHeader(titleStyle, sheet, title, maxColIndex);
                colRowIndex = 1;
            }

            this.rowNum = writer.startRow();
            if (this.rowNum == 0) {
                this.rowNum = colRowIndex + 1;
            }

            try {
                // write column header
                this.writeColumnNames(colRowIndex, headerStyle);

                // write rows
                for (Object row : rows) {
                    this.writeRow(row, columnStyle);
                }
            } catch (Exception e) {
                log.error("write row fail", e);
            }
        }

        // write to OutputStream
        try (OutputStream os = outputStream) {
            workbook.write(os);
        } catch (Exception e) {
            throw new WriterException("workbook write to OutputStream error", e);
        }
    }

    private void writeHeader(CellStyle cellStyle, Sheet sheet, String title, int maxColIndex) {
        Row titleRow = sheet.createRow(0);
        titleRow.setHeightInPoints(50);

        for (int i = 0; i <= maxColIndex; i++) {
            Cell cell = titleRow.createCell(i);
            if (i == 0) {
                cell.setCellValue(title);
            }
            cell.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, maxColIndex));
    }

    private void writeColumnNames(int rowIndex, CellStyle headerStyle) {
        Row rowHead = sheet.createRow(rowIndex);
        rowHead.setHeightInPoints(30);
        for (ExcelColumn column : columns) {
            Cell cell = rowHead.createCell(column.index());
            if (null != headerStyle) {
                cell.setCellStyle(headerStyle);
            }
            cell.setCellValue(column.title());
            if (column.width() > 0) {
                sheet.setColumnWidth(column.index(), column.width());
            } else {
                sheet.setColumnWidth(column.index(), Constant.DEFAULT_COLUMN_WIDTH);
            }
        }
    }

    private void writeRow(Object instance, CellStyle columnStyle) throws Exception {
        Row row = sheet.createRow(rowNum++);
        for (Integer index : fieldIndexes.keySet()) {
            Field field = fieldIndexes.get(index);
            if (null == field) {
                continue;
            }

            Object value = field.get(instance);
            if (value == null) {
                continue;
            }

            Cell cell = row.createCell(index);
            cell.setCellStyle(columnStyle);

            if (field.getType().equals(String.class)) {
                cell.setCellValue(value.toString());
                continue;
            }

            ExcelColumn column = field.getAnnotation(ExcelColumn.class);

            if (!NullConverter.class.equals(column.converter())) {
                Converter convert = column.converter().newInstance();
                ConverterCache.addConvert(convert);
                cell.setCellValue(convert.toString(value));
            } else {
                if (StringUtils.isNotEmpty(column.datePattern())) {
                    String content = "";
                    if (Date.class.equals(field.getType())) {
                        content = new DateConverter(column.datePattern()).toString((Date) value);
                    } else if (LocalDate.class.equals(field.getType())) {
                        content = new LocalDateConverter(column.datePattern()).toString((LocalDate) value);
                    }
                    if (LocalDateTime.class.equals(field.getType())) {
                        content = new LocalDateTimeConverter(column.datePattern()).toString((LocalDateTime) value);
                    }
                    cell.setCellValue(content);
                } else {
                    Converter converter = ConverterCache.computeConvert(field);
                    if (null != converter) {
                        cell.setCellValue(converter.toString(value));
                    } else {
                        cell.setCellValue(value.toString());
                    }
                }
            }
        }
    }

}
