package io.github.biezhi.excel.plus.model;

import io.github.biezhi.excel.plus.annotation.ExcelField;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author biezhi
 * @date 2018/2/4
 */
public class Order implements Serializable {

    @ExcelField(order = 0, columnName = "订单编号")
    private Long id;

    @ExcelField(order = 1, columnName = "商品名称")
    private String productName;

    @ExcelField(order = 2, columnName = "商品价格")
    private BigDecimal price;

    @ExcelField(order = 3, columnName = "购买用户")
    private String buyUser;

    @ExcelField(order = 4, columnName = "用户手机号")
    private String userMobile;

    @ExcelField(order = 5, columnName = "购买数量")
    private Integer count;

    @ExcelField(order = 6, columnName = "下单时间")
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getBuyUser() {
        return buyUser;
    }

    public void setBuyUser(String buyUser) {
        this.buyUser = buyUser;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", buyUser='" + buyUser + '\'' +
                ", userMobile='" + userMobile + '\'' +
                ", count=" + count +
                ", createTime=" + createTime +
                '}';
    }
}
