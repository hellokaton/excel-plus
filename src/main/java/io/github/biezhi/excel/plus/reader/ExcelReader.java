package io.github.biezhi.excel.plus.reader;

import io.github.biezhi.excel.plus.utils.ExcelUtils;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Excel read to list
 * <p>
 * this class used to read Excel documents and convert all rows in the document to List.
 * <p>
 * to parse Excel by passing a Workbook object in the constructor,
 * the Class is passed to reflect the value for a particular type of reflection.
 *
 * @author biezhi
 * @date 2018/2/4
 */
public class ExcelReader<T> {

    private Workbook     workbook;
    private Class<T>     type;
    private Predicate<T> filter;
    private int          startRowIndex = 1;

    public ExcelReader(Workbook workbook, Class<T> type) {
        this.workbook = workbook;
        this.type = type;
    }

    /**
     * Reading the Excel document content into a List container.
     * <p>
     * Get the sheet Name according to the Java Class type, and the sheet with an index of 0 if the sheet failure is read.
     * <p>
     * The cell data that is read from each row is injected into the field of the Java object by reflection,
     * and you need to make sure that the Java Type has a non-parameterized constructor,
     * otherwise the process will fail.
     *
     * @return excel rows
     */
    public List<T> asList() {
        String sheetName = ExcelUtils.getSheetName(type);
        Sheet  sheet     = workbook.getSheet(sheetName);
        if (null == sheet) {
            sheet = workbook.getSheet(0);
        }
        int     rows = sheet.getRows();
        List<T> list = new ArrayList<>(rows);
        long    cols = ExcelUtils.getReadFieldOrders(type);

        // traverse excel row
        for (int row = this.startRowIndex; row < rows; row++) {
            T item = this.buildItem(sheet, cols, row);
            if (null != item) {
                list.add(item);
            }
        }
        if (null != this.filter) {
            list = list.stream().filter(this.filter).collect(Collectors.toList());
        }
        return list;
    }

    /**
     * Set the Excel row data to the item object.
     *
     * @param sheet excel sheet
     * @param cols  the number of columns
     * @param row   row index
     */
    private T buildItem(Sheet sheet, long cols, int row) {
        T item = ExcelUtils.newInstance(type);
        if (null == item) {
            return null;
        }
        for (int col = 0; col < cols; col++) {
            Cell   cell  = sheet.getCell(col, row);
            String value = cell.getContents();
            if (null != value && !value.isEmpty()) {
                ExcelUtils.writeToField(item, col, cell.getContents());
            }
        }
        return item;
    }

    /**
     * Filter the converted List data
     *
     * @param predicate filter condition
     * @return self
     */
    public ExcelReader<T> filter(Predicate<T> predicate) {
        this.filter = predicate;
        return this;
    }

    /**
     * Set the first line from the Excel document to start reading the data by default from the first line,
     * assuming a header.
     *
     * @param startRowIndex row index
     * @return self
     */
    public ExcelReader<T> startRow(int startRowIndex) {
        this.startRowIndex = startRowIndex;
        return this;
    }

}
