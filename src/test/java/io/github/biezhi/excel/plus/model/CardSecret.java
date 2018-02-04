package io.github.biezhi.excel.plus.model;

import io.github.biezhi.excel.plus.annotation.ExcelField;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 一卡通卡密
 *
 * @author biezhi
 * @date 2018/2/4
 */
public class CardSecret implements Serializable {

    @ExcelField(readOrder = 0, title = "卡密类型")
    private String cardType;

    @ExcelField(readOrder = 1, title = "卡密")
    private String secret;

    @ExcelField(readOrder = 2, title = "面额")
    private BigDecimal amount;

    @ExcelField(readOrder = 3, title = "过期时间")
    private Date expiredDate;

    public CardSecret() {
    }

    public CardSecret(String secret, BigDecimal amount) {
        this.cardType = "联通";
        this.secret = secret;
        this.amount = amount;
        this.expiredDate = new Date();
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

}
