package io.github.biezhi.excel.plus.reader;

import io.github.biezhi.excel.plus.utils.ExcelUtils;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import java.util.ArrayList;
import java.util.List;

/**
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

    public List<T> read() {
        Sheet   sheet = workbook.getSheet(0);
        int     rows  = sheet.getRows();
        List<T> list  = new ArrayList<>(rows - 1);
        long    cols  = ExcelUtils.getReadFieldOrders(type);

        for (int row = 1; row < rows; row++) {
            T item = ExcelUtils.newInstance(type);
            for (int col = 0; col < cols; col++) {
                Cell   cell  = sheet.getCell(row, col);
                String value = cell.getContents();
                if (null != value && !value.isEmpty()) {
                    ExcelUtils.writeToField(item.getClass(), col, cell.getContents());
                }
            }
            list.add(item);
        }
        return list;
    }

}
