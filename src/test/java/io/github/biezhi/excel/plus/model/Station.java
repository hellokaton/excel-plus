package io.github.biezhi.excel.plus.model;

import io.github.biezhi.excel.plus.annotation.ExcelField;
import io.github.biezhi.excel.plus.annotation.ExcelSheet;

@ExcelSheet("北京")
public class Station {


    @ExcelField(order = 5, columnName = "区站号")
    private String station;

    @ExcelField(order = 8, columnName = "经度(度分秒)")
    private String longitudex;

    @ExcelField(order = 9, columnName = "纬度（度分秒）")
    private String latitudey;

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getLongitudex() {
        return longitudex;
    }

    public void setLongitudex(String longitudex) {
        this.longitudex = longitudex;
    }

    public String getLatitudey() {
        return latitudey;
    }

    public void setLatitudey(String latitudey) {
        this.latitudey = latitudey;
    }

    @Override
    public String toString() {
        return "Station{" +
                "station='" + station + '\'' +
                ", longitudex='" + longitudex + '\'' +
                ", latitudey='" + latitudey + '\'' +
                '}';
    }
}



