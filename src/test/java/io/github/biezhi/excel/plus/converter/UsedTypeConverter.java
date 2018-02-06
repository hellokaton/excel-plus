package io.github.biezhi.excel.plus.converter;

public class UsedTypeConverter implements Converter<Boolean> {

    @Override
    public String write(Boolean value) {
        return value ? "已使用" : "未使用";
    }

    @Override
    public Boolean read(String value) {
        return "已使用".equals(value);
    }
}