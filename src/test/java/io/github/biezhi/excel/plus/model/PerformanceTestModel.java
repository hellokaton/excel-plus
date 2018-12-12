package io.github.biezhi.excel.plus.model;

import io.github.biezhi.excel.plus.annotation.ExcelColumn;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 性能测试实体
 *
 * @author biezhi
 * @date 2018-12-13
 */
@Data
public class PerformanceTestModel {

    @ExcelColumn(title = "序号", index = 0)
    private Integer id;

    @ExcelColumn(title = "UUID", index = 1, width = 50 * 256)
    private String uuid;

    @ExcelColumn(title = "日期", index = 2, datePattern = "yyyy年MM月dd日")
    private LocalDate date;

    @ExcelColumn(title = "余额", index = 3)
    private double amount;

    @ExcelColumn(title = "本金", index = 4)
    private BigDecimal principal;

    @ExcelColumn(title = "手机号", index = 5)
    private String mobile;

    @ExcelColumn(title = "状态", index = 6, converter = StatusConverter.class)
    private Byte status;

    public PerformanceTestModel() {
    }

    public PerformanceTestModel(Integer id, String uuid, LocalDate date, double amount, BigDecimal principal, String mobile, Byte status) {
        this.id = id;
        this.uuid = uuid;
        this.date = date;
        this.amount = amount;
        this.principal = principal;
        this.mobile = mobile;
        this.status = status;
    }
}
