package io.github.biezhi.excel.plus.model;

import io.github.biezhi.excel.plus.annotation.ExcelField;
import lombok.Data;

@Data
public class Station {

    @ExcelField(order = 0, columnName = "序号")
    private String no;

    @ExcelField(order = 1, columnName = "省份")
    private String province;

    @ExcelField(order = 2, columnName = "城市")
    private String city;

    @ExcelField(order = 3, columnName = "县区")
    private String xian;

    @ExcelField(order = 4, columnName = "站名")
    private String zhan;

    @ExcelField(order = 5, columnName = "区站号")
    private String station;

    @ExcelField(order = 6, columnName = "站址")
    private String zhanzhi;

    @ExcelField(order = 8, columnName = "经度(度分秒)")
    private String longitudex;

    @ExcelField(order = 9, columnName = "纬度（度分秒）")
    private String latitudey;

}



