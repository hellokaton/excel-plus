package io.github.biezhi.excel.plus.model;

import io.github.biezhi.excel.plus.annotation.ExcelColumn;
import lombok.Data;

import java.util.Date;

/**
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

    @ExcelColumn(index = 5)
    private String manufacturingPrice;

    @ExcelColumn(index = 12)
    private Date date;

    @ExcelColumn(index = 14)
    private String month;

}
