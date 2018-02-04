package io.github.biezhi.excel.plus.converter;

public class CardTypeConverter implements Converter<Integer> {

    @Override
    public String write(Integer value) {
        return value.equals(1) ? "联通" : "移动";
    }

    @Override
    public Integer read(String value) {
        return value.equals("联通") ? 1 : 2;
    }
}