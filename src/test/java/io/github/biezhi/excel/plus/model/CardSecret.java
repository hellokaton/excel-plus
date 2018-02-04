package io.github.biezhi.excel.plus.model;

import io.github.biezhi.excel.plus.annotation.ReadField;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 一卡通卡密
 *
 * @author biezhi
 * @date 2018/2/4
 */
public class CardSecret {

    @ReadField(order = 0)
    private String cardType;

    @ReadField(order = 1)
    private String secret;

    @ReadField(order = 2)
    private BigDecimal amount;

    @ReadField(order = 3)
    private Date expiredDate;

}
