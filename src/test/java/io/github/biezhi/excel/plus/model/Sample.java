package io.github.biezhi.excel.plus.model;

import io.github.biezhi.excel.plus.annotation.ExcelColumn;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * SampleData.xlsx Test Model
 *
 * @author biezhi
 * @date 2018-12-12
 */
@Data
public class Sample {

    @ExcelColumn(index = 0, datePattern = "M/d/yy")
    private LocalDate date;

    @ExcelColumn(index = 1)
    private String location;

    @ExcelColumn(index = 4)
    private int proportion;

    @ExcelColumn(index = 5)
    private double ss;

    @ExcelColumn(index = 6)
    private BigDecimal amount;

}
