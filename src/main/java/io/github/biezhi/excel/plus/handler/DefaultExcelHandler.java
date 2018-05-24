/**
 * Copyright (c) 2018, biezhi 王爵 (biezhi.me@gmail.com)
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
package io.github.biezhi.excel.plus.handler;

import io.github.biezhi.excel.plus.exception.ExcelException;
import io.github.biezhi.excel.plus.exception.ParseException;
import io.github.biezhi.excel.plus.reader.Reader;
import io.github.biezhi.excel.plus.utils.ExcelUtils;
import io.github.biezhi.excel.plus.utils.Pair;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author biezhi
 * @date 2018/3/21
 */
@Slf4j
public class DefaultExcelHandler<T> implements ExcelHandler {

    private Class<T> type;
    private Reader   reader;

    public DefaultExcelHandler(Class<T> type, Reader reader) {
        this.type = type;
        this.reader = reader;
    }

    @Override
    public List<Pair<Integer, T>> parse() throws ExcelException {
        Workbook workbook;
        Sheet    sheet;
        try {
            workbook = WorkbookFactory.create(reader.getExcelFile());
        } catch (IOException | InvalidFormatException e) {
            throw new ParseException(e);
        }
        if (null != reader.getSheetName()) {
            sheet = workbook.getSheet(reader.getSheetName());
        } else {
            if (reader.getSheetIndex() >= 0) {
                sheet = workbook.getSheetAt(reader.getSheetIndex());
            } else {
                sheet = workbook.getSheetAt(0);
            }
        }
        if (null == sheet) {
            throw new ParseException("Not Found Sheet.");
        }

        int firstRowNum = sheet.getFirstRowNum();
        int lastRowNum  = sheet.getLastRowNum();

        List<Pair<Integer, T>> list = new ArrayList<>(lastRowNum);

        // traverse excel row
        for (int rowNum = firstRowNum + reader.getStartRowIndex(); rowNum <= lastRowNum; rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                continue;
            }
            T item = this.buildItem(row);
            if (null != item) {
                list.add(new Pair<>(rowNum, item));
            }
        }
        return list;
    }

    /**
     * Set the Excel row data to the item object.
     *
     * @param row excel row
     * @return return java instance
     */
    private T buildItem(Row row) {
        T item = ExcelUtils.newInstance(type);
        if (null == item) {
            return null;
        }
        int firstCellNum = row.getFirstCellNum();
        int lastCellNum  = row.getLastCellNum();

        for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
            Cell   cell  = row.getCell(cellNum);
            String value = ExcelUtils.getCellValue(cell);
            ExcelUtils.writeToField(item, cellNum, value);
        }
        return item;
    }

}
