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

    @ExcelField(writeOrder = 0, title = "订单编号")
    private Long id;

    @ExcelField(writeOrder = 1, title = "商品名称")
    private String productName;

    @ExcelField(writeOrder = 2, title = "商品价格")
    private BigDecimal price;

    @ExcelField(writeOrder = 3, title = "购买用户")
    private String buyUser;

    @ExcelField(writeOrder = 4, title = "用户手机号")
    private String userMobile;

    @ExcelField(writeOrder = 5, title = "购买数量")
    private Integer count;

    @ExcelField(writeOrder = 6, title = "下单时间")
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
