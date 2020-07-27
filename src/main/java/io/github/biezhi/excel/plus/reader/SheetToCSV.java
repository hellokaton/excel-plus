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

import io.github.biezhi.excel.plus.conveter.Converter;
import io.github.biezhi.excel.plus.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;

import java.lang.reflect.Field;
import java.util.stream.Stream;

/**
 * Uses the XSSF Event SAX helpers to do most of the work
 * of parsing the Sheet XML, and outputs the contents
 * as a (basic) CSV.
 */
@Slf4j
public class SheetToCSV<T> extends ReaderConverter implements XSSFSheetXMLHandler.SheetContentsHandler {

    private boolean firstCellOfRow;
    private int     currentRow = -1;
    private int     currentCol = -1;

    private final OPCPackage        opcPackage;
    private final Stream.Builder<T> rowsStream;
    private final Class<T>          type;
    private final int               startRow;

    private T row;

    public SheetToCSV(OPCPackage opcPackage, int startRow, Class<T> type) {
        this.opcPackage = opcPackage;
        this.rowsStream = Stream.builder();
        this.startRow = startRow;

        this.type = type;

        try {
            this.initFieldConverter(type.getDeclaredFields());
        } catch (Exception e) {
            log.error("init field converter fail", e);
        }
    }

    @Override
    public void startRow(int rowNum) {
        row = null;
        // Prepare for this row
        firstCellOfRow = true;
        currentRow = rowNum;
        currentCol = -1;
        if (currentRow < startRow) {
            return;
        }
    }

    @Override
    public void endRow(int rowNum) {
        if (currentRow < startRow) {
            return;
        }
        if(null == row){
            return;
        }
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

        currentCol = (new CellReference(cellReference)).getCol();

        Field field = fieldIndexes.get(currentCol);
        if (null != field) {

            if(row == null){
                row = ExcelUtil.newInstance(type);
            }

            try {
                Object    cellValue = formattedValue;
                Converter converter = fieldConverters.get(field);
                if (null != converter) {
                    cellValue = converter.stringToR(formattedValue);
                }
                field.set(row, cellValue);
            } catch (Exception e) {
                log.error("write field {} value fail", field.getName(), e);
            }
        }
    }

    public OPCPackage getOpcPackage() {
        return opcPackage;
    }

    public Stream.Builder<T> getRowsStream() {
        return rowsStream;
    }

}