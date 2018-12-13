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
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Comparator.comparingInt;

/**
 * @author biezhi
 * @date 2018-12-11
 */
public abstract class ExcelWriter {

    protected OutputStream outputStream;

    public ExcelWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    protected ExcelWriter() {
    }

    public abstract void writeWorkbook(Writer writer) throws WriterException;

    protected void writeWorkbook(Writer writer, Workbook workbook) throws WriterException {
        List<?> rows = writer.getRows();
        if (null == rows || rows.isEmpty()) {
            throw new WriterException("Write rows cannot be empty");
        }

        Sheet sheet = workbook.createSheet(writer.sheetName());

        try (OutputStream os = outputStream) {
            AtomicInteger counter = new AtomicInteger(writer.startRow());

            CellStyle headerStyle = Constant.defaultHeaderStyle(workbook);
            CellStyle columnStyle = Constant.defaultColumnStyle(workbook);
            CellStyle titleStyle  = Constant.defaultTitleStyle(workbook);

            if (null != writer.getHeaderStyle()) {
                writer.getTitleStyle().accept(headerStyle);
            }
            if (null != writer.getCellStyle()) {
                writer.getCellStyle().accept(columnStyle);
            }
            if (null != writer.getTitleStyle()) {
                writer.getTitleStyle().accept(titleStyle);
            }

            String headerTitle = writer.headerTitle();

            Class<?>            type           = rows.get(0).getClass();
            Field[]             declaredFields = type.getDeclaredFields();
            Map<Integer, Field> fieldMap       = new HashMap<>();
            List<ExcelColumn>   columnList     = new ArrayList<>();

            for (Field field : declaredFields) {
                ExcelColumn column = field.getAnnotation(ExcelColumn.class);
                if (null != column) {
                    field.setAccessible(true);
                    fieldMap.put(column.index(), field);
                    columnList.add(column);
                }
            }

            // write header
            int colRowIndex = 0;
            if (StringUtils.isNotEmpty(headerTitle)) {
                Integer maxColIndex = columnList.stream().map(ExcelColumn::index)
                        .max(comparingInt(Integer::intValue))
                        .get();

                this.writeHeader(titleStyle, sheet, headerTitle, maxColIndex);
                colRowIndex = 1;
            }

            if (counter.get() == 0) {
                counter.set(colRowIndex + 1);
            }

            this.writeColumnNames(colRowIndex, headerStyle, sheet, columnList);

            // write rows
            rows.forEach(row -> this.writeRow(sheet, row, columnStyle, fieldMap, counter.getAndIncrement()));

            // write to OutputStream
            workbook.write(os);
        } catch (Exception e) {
            throw new WriterException(e);
        }
    }


    protected void writeHeader(CellStyle cellStyle, Sheet sheet, String title, int maxColIndex) {
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

    protected void writeColumnNames(int rowIndex, CellStyle headerStyle, Sheet sheet, List<ExcelColumn> columnList) {
        Row rowHead = sheet.createRow(rowIndex);
        rowHead.setHeightInPoints(30);
        for (ExcelColumn column : columnList) {
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

    protected void writeRow(Sheet sheet, Object instance, CellStyle columnStyle,
                            Map<Integer, Field> fieldMap, int rowNum) {

        Row row = sheet.createRow(rowNum);

        Iterator<Integer> iterator = fieldMap.keySet().iterator();

        try {
            while (iterator.hasNext()) {
                Integer columnIndex = iterator.next();
                Cell    cell        = row.createCell(columnIndex);

                Field  field = fieldMap.get(columnIndex);
                Object value = field.get(instance);

                if (value == null) {
                    continue;
                }

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
                        continue;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
