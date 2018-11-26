package io.github.biezhi.excel.plus.model;

import io.github.biezhi.excel.plus.annotation.ExcelField;
import io.github.biezhi.excel.plus.converter.CardTypeConverter;
import io.github.biezhi.excel.plus.converter.UsedTypeConverter;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 一卡通卡密
 *
 * @author biezhi
 * @date 2018/2/4
 */
@Data
@NoArgsConstructor
public class CardSecret implements Serializable {

    @ExcelField(order = 0, columnName = "运营商", convertType = CardTypeConverter.class)
    private Integer cardType;

    @ExcelField(order = 1, columnName = "卡密")
    private String secret;

    @ExcelField(order = 2, columnName = "面额")
    private BigDecimal amount;

    @ExcelField(order = 3, columnName = "过期时间", datePattern = "yyyy年MM月dd日")
    private Date expiredDate;

    @ExcelField(order = 5, columnName = "使用情况", convertType = UsedTypeConverter.class)
    private Boolean used;

    public CardSecret(Integer cardType, String secret, BigDecimal amount, boolean used) {
        this.cardType = cardType;
        this.secret = secret;
        this.amount = amount;
        this.expiredDate = new Date();
        this.used = used;
    }

}
