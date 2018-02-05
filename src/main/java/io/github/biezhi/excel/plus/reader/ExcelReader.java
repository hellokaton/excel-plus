package io.github.biezhi.excel.plus.reader;

import io.github.biezhi.excel.plus.utils.ExcelUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

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

    private HSSFWorkbook workbook;
    private Class<T>     type;
    private Predicate<T> filter;
    private int          startRowIndex = 1;

    public ExcelReader(HSSFWorkbook workbook, Class<T> type) {
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
            sheet = workbook.getSheetAt(0);
        }

        int firstRowNum = sheet.getFirstRowNum();
        int lastRowNum  = sheet.getLastRowNum();

        List<T> list = new ArrayList<>(lastRowNum);

        // traverse excel row
        for (int rowNum = firstRowNum + this.startRowIndex; rowNum <= lastRowNum; rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                continue;
            }
            T item = this.buildItem(row);
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
     * @param row row index
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
            String value = getCellValue(cell);
            ExcelUtils.writeToField(item, cellNum, value);
        }
        return item;
    }

    private String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        if (cell.getCellTypeEnum().equals(CellType.NUMERIC)) {
            cell.setCellType(CellType.STRING);
        }
        switch (cell.getCellTypeEnum()) {
            case NUMERIC:
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case STRING:
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA:
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case BLANK:
                cellValue = "";
                break;
            case ERROR:
                cellValue = "illegal character";
                break;
            default:
                cellValue = "Unknown type";
                break;
        }
        return cellValue;
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
