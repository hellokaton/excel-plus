package io.github.biezhi.excel.plus.enums;

/**
 * Excel extension type
 *
 * @author biezhi
 * @date 2018/2/4
 */
public enum ExcelType {

    XLS, XLSX, CSV;

    public static ExcelType getExcelType(String fileName) {
        if (fileName.toUpperCase().endsWith(XLS.toString())) {
            return ExcelType.XLS;
        }
        if (fileName.toUpperCase().endsWith(XLSX.toString())) {
            return ExcelType.XLSX;
        }
        if (fileName.toUpperCase().endsWith(CSV.toString())) {
            return ExcelType.CSV;
        }
        return ExcelType.XLS;
    }

}
