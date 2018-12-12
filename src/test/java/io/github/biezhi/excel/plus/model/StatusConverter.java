package io.github.biezhi.excel.plus.model;

import io.github.biezhi.excel.plus.conveter.Converter;

public class StatusConverter implements Converter<String, Byte> {

    @Override
    public Byte stringToR(String value) {
        if ("正常".equals(value)) {
            return 1;
        } else if ("拉黑".equals(value)) {
            return 2;
        }
        return -1;
    }

    @Override
    public String toString(Byte value) {
        if (null == value) {
            return "";
        }
        if (value == 1) {
            return "正常";
        } else if (value == 2) {
            return "拉黑";
        } else {
            return "等待";
        }
    }
}
