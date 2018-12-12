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
package io.github.biezhi.excel.plus.reader;

import io.github.biezhi.excel.plus.annotation.ExcelColumn;
import io.github.biezhi.excel.plus.conveter.Converter;
import io.github.biezhi.excel.plus.conveter.ConverterCache;
import io.github.biezhi.excel.plus.conveter.NullConverter;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Uses the XSSF Event SAX helpers to do most of the work
 * of parsing the Sheet XML, and outputs the contents
 * as a (basic) CSV.
 */
public class SheetToCSV<T> implements XSSFSheetXMLHandler.SheetContentsHandler {

    private boolean firstCellOfRow;
    private int     currentRow = -1;
    private int     currentCol = -1;

    private final OPCPackage        opcPackage;
    private final Stream.Builder<T> rowsStream;
    private final Class<T>          type;
    private final int               startRow;

    private Map<Field, Converter<String, ?>> fieldConverterMap;

    private Map<Integer, Field> fieldIndexMap;

    private T row;

    public SheetToCSV(OPCPackage opcPackage, int startRow, Class<T> type) {
        this.opcPackage = opcPackage;
        this.rowsStream = Stream.builder();
        this.startRow = startRow;

        this.fieldConverterMap = new HashMap<>();
        this.type = type;

        Field[] declaredFields = type.getDeclaredFields();
        this.fieldIndexMap = new HashMap<>(declaredFields.length);

        for (Field field : declaredFields) {
            ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
            if (null != excelColumn) {
                fieldIndexMap.put(excelColumn.index(), field);
            }
        }
    }

    @Override
    public void startRow(int rowNum) {
        // Prepare for this row
        firstCellOfRow = true;
        currentRow = rowNum;
        currentCol = -1;
        try {
            row = type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void endRow(int rowNum) {
        rowsStream.add(row);
    }

    @Override
    public void cell(String cellReference, String formattedValue,
                     XSSFComment comment) {

        if (currentRow < startRow) {
            return;
        }

        if (firstCellOfRow) {
            firstCellOfRow = false;
        }

        // gracefully handle missing CellRef here in a similar way as XSSFCell does
        if (cellReference == null) {
            cellReference = new CellAddress(currentRow, currentCol).formatAsString();
        }

        currentCol = (int) (new CellReference(cellReference)).getCol();
        Field field = fieldIndexMap.get(currentCol);
        if (null != field) {
            this.writeToModel(formattedValue, field);
        }
    }

    private void writeToModel(String value, Field field) {
        try {
            field.setAccessible(true);

            Converter converter = fieldConverterMap.get(field);
            if (null != converter) {
                field.set(row, converter.stringToR(value));
            } else {
                ExcelColumn column = field.getAnnotation(ExcelColumn.class);
                if (NullConverter.class.equals(column.converter())) {
                    converter = ConverterCache.computeConvert(field);
                } else {
                    converter = column.converter().newInstance();
                }
                if (null != converter) {
                    fieldConverterMap.put(field, converter);
                    field.set(row, converter.stringToR(value));
                }
            }
        } catch (Exception e) {
            System.out.println(field.getName());
            e.printStackTrace();
        }
    }

    public OPCPackage getOpcPackage() {
        return opcPackage;
    }

    public Stream.Builder<T> getRowsStream() {
        return rowsStream;
    }

}