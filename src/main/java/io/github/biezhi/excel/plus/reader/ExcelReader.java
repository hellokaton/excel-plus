package io.github.biezhi.excel.plus.reader;

import io.github.biezhi.excel.plus.utils.ExcelUtils;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import java.util.ArrayList;
import java.util.List;

/**
 * Excel read to list
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
     * Read excel content as List<T>
     *
     * @return rows data
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

        for (int row = 1; row < rows; row++) {
            T item = ExcelUtils.newInstance(type);
            for (int col = 0; col < cols; col++) {
                Cell   cell  = sheet.getCell(col, row);
                String value = cell.getContents();
                if (null != value && !value.isEmpty()) {
                    ExcelUtils.writeToField(item, col, cell.getContents());
                }
            }
            list.add(item);
        }
        return list;
    }

}
