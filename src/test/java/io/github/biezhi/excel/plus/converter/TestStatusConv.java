package io.github.biezhi.excel.plus.converter;

import io.github.biezhi.excel.plus.conveter.Converter;

public class TestStatusConv implements Converter<String, Integer> {
    @Override
    public Integer stringToR(String value) {
        if ("enable".equals(value)) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString(Integer value) {
        if (null == value) {
            return null;
        } else if (value == 1) {
            return "enable";
        }
        return "disable";
    }
}