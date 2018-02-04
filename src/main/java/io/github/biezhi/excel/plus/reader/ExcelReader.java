package io.github.biezhi.excel.plus.reader;

import io.github.biezhi.excel.plus.utils.ExcelUtils;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import java.util.ArrayList;
import java.util.List;

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

    private Workbook workbook;
    private Class<T> type;

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
    public List<T> read() {
        String sheetName = ExcelUtils.getSheetName(type);
        Sheet  sheet     = workbook.getSheet(sheetName);
        if (null == sheet) {
            sheet = workbook.getSheet(0);
        }
        int     rows = sheet.getRows();
        List<T> list = new ArrayList<>(rows - 1);
        long    cols = ExcelUtils.getReadFieldOrders(type);

        // traverse excel row
        for (int row = 1; row < rows; row++) {
            T item = ExcelUtils.newInstance(type);
            this.buildItem(sheet, cols, row, item);
            list.add(item);
        }
        return list;
    }

    /**
     * Set the Excel row data to the item object.
     *
     * @param sheet excel sheet
     * @param cols  the number of columns
     * @param row   row index
     * @param item  item object, java objects used to store row data
     */
    private void buildItem(Sheet sheet, long cols, int row, T item) {
        for (int col = 0; col < cols; col++) {
            Cell   cell  = sheet.getCell(col, row);
            String value = cell.getContents();
            if (null != value && !value.isEmpty()) {
                ExcelUtils.writeToField(item, col, cell.getContents());
            }
        }
    }

}
