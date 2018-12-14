package io.github.biezhi.excel.plus.converter;

import io.github.biezhi.excel.plus.conveter.Converter;
import io.github.biezhi.excel.plus.conveter.LocalDateTimeConverter;
import io.github.biezhi.excel.plus.exception.ConverterException;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author biezhi
 * @date 2018-12-14
 */
public class LocalDateTimeConverterTest {

    private final String pattern = "yyyy/MM/dd HH:mm:ss";

    @Test
    public void testStringToR() throws ConverterException {
        Converter<String, LocalDateTime> converter = new LocalDateTimeConverter(pattern);

        LocalDateTime date = converter.stringToR("2018/12/12 12:32:11");

        assertEquals(2018, date.getYear());
        assertEquals(12, date.getMonthValue());
        assertEquals(12, date.getDayOfMonth());

        assertNull(converter.stringToR(null));
    }

    @Test(expected = ConverterException.class)
    public void testStringToRError() throws ConverterException {
        Converter<String, LocalDateTime> converter = new LocalDateTimeConverter(pattern);
        converter.stringToR("2018-12-12");
    }

    @Test
    public void testToString() throws ConverterException {
        Converter<String, LocalDateTime> converter = new LocalDateTimeConverter(pattern);

        String value = converter.toString(LocalDateTime.now());
        assertEquals(LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern)), value);

        assertNull(converter.toString(null));
    }

}
