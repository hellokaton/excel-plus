package io.github.biezhi.excel.plus.converters;

import io.github.biezhi.excel.plus.converter.Converter;

public class CardTypeConverter implements Converter<Integer> {

    public CardTypeConverter() {
    }

    @Override
    public String write(Integer value) {
        return value.equals(1) ? "联通" : "移动";
    }

    @Override
    public Integer read(String value) {
        return value.equals("联通") ? 1 : 2;
    }
}