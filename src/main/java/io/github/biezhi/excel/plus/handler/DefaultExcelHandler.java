package io.github.biezhi.excel.plus.handler;

import io.github.biezhi.excel.plus.exception.ParseException;
import io.github.biezhi.excel.plus.reader.ReaderParam;
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

    private Class<T>    type;
    private ReaderParam readerParam;

    public DefaultExcelHandler(Class<T> type, ReaderParam readerParam) {
        this.type = type;
        this.readerParam = readerParam;
    }

    @Override
    public List<Pair<Integer, T>> parse() throws ParseException {

        Workbook    workbook;
        Sheet       sheet       = null;
        try {
            workbook = WorkbookFactory.create(readerParam.getExcelFile());
        } catch (IOException | InvalidFormatException e) {
            throw new ParseException(e);
        }
        if (null != readerParam.getSheetName()) {
            sheet = workbook.getSheet(readerParam.getSheetName());
        } else {
            if (readerParam.getSheetIndex() >= 0) {
                sheet = workbook.getSheetAt(readerParam.getSheetIndex());
            } else {
                sheet = workbook.getSheetAt(0);
            }
        }

        int firstRowNum = sheet.getFirstRowNum();
        int lastRowNum  = sheet.getLastRowNum();

        List<Pair<Integer, T>> list = new ArrayList<>(lastRowNum);

        // traverse excel row
        for (int rowNum = firstRowNum + readerParam.getStartRowIndex(); rowNum <= lastRowNum; rowNum++) {
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
        int lastCellNum  = row.getPhysicalNumberOfCells();
        for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
            Cell   cell  = row.getCell(cellNum);
            String value = ExcelUtils.getCellValue(cell);
            ExcelUtils.writeToField(item, cellNum, value);
        }
        return item;
    }

}
