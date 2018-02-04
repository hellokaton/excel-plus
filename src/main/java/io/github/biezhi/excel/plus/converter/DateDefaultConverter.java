package io.github.biezhi.excel.plus.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateDefaultConverter implements Converter<Date> {

    private String pattern;

    public DateDefaultConverter() {
        this.pattern = "yyyy-MM-dd";
    }

    public DateDefaultConverter(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String write(Date value) {
        SimpleDateFormat sdf = new SimpleDateFormat(this.pattern);
        return sdf.format(value);
    }

    @Override
    public Date read(String value) {
        SimpleDateFormat sdf = new SimpleDateFormat(this.pattern);
        try {
            return sdf.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}