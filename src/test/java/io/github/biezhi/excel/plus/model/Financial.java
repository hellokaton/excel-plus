package io.github.biezhi.excel.plus.model;

import io.github.biezhi.excel.plus.annotation.ExcelColumn;
import lombok.Data;

import java.util.Date;

/**
 * FinancialSample.xlsx Test Model
 *
 * @author biezhi
 * @date 2018-12-12
 */
@Data
public class Financial {

    @ExcelColumn(index = 0)
    private String segment;

    @ExcelColumn(index = 1)
    private String country;

    @ExcelColumn(index = 4)
    private Double unitsSold;

    @ExcelColumn(index = 6)
    private String salePrice;

//    @ExcelColumn(index = 12, datePattern = "yyyy/M/d")
    @ExcelColumn(index = 12, datePattern = "M/d/yyyy")
    private Date date;

    @ExcelColumn(index = 14)
    private String month;

}
