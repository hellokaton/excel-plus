package io.github.biezhi.excel.plus.model;

import io.github.biezhi.excel.plus.annotation.ExcelColumn;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author biezhi
 * @date 2018-12-12
 */
@Data
public class Member {

    @ExcelColumn(title = "卡号", index = 0)
    private Long cardNo;

    @ExcelColumn(title = "卡类型", index = 1)
    private String cardType;

    @ExcelColumn(title = "领用状态", index = 2)
    private String requisitionStatus;

    @ExcelColumn(title = "状态", index = 3)
    private String status;

    @ExcelColumn(title = "余额(元)", index = 6)
    private BigDecimal amount;

    @ExcelColumn(title = "会员", index = 7)
    private String nickname;

    @ExcelColumn(title = "性别", index = 9)
    private String gender;

    @ExcelColumn(title = "手机", index = 10)
    private String mobile;

    @ExcelColumn(title = "发卡日期", index = 14, datePattern = "M/d/yyyy HH:mm")
    private Date sendCardTime;

}
