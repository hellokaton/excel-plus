package io.github.biezhi.excel.plus.converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class LocalDateDefaultConverter implements Converter<LocalDate> {

    @Override
    public String write(LocalDate value) {
        return value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Override
    public LocalDate read(String value) {
        return LocalDate.parse(value);
    }

}