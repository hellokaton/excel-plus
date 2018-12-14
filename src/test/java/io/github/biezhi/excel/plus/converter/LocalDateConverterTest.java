package io.github.biezhi.excel.plus.converter;

import io.github.biezhi.excel.plus.conveter.Converter;
import io.github.biezhi.excel.plus.conveter.LocalDateConverter;
import io.github.biezhi.excel.plus.exception.ConverterException;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author biezhi
 * @date 2018-12-14
 */
public class LocalDateConverterTest {

    @Test
    public void testStringToR() throws ConverterException {
        Converter<String, LocalDate> converter = new LocalDateConverter("yyyy/MM/dd");

        LocalDate date = converter.stringToR("2018/12/12");

        assertEquals(2018, date.getYear());
        assertEquals(12, date.getMonthValue());
        assertEquals(12, date.getDayOfMonth());

        assertNull(converter.stringToR(null));
    }

    @Test(expected = ConverterException.class)
    public void testStringToRError() throws ConverterException {
        Converter<String, LocalDate> converter = new LocalDateConverter("yyyy/MM/dd");
        converter.stringToR("2018-12-12");
    }

    @Test
    public void testToString() throws ConverterException {
        Converter<String, LocalDate> converter = new LocalDateConverter("yyyy/MM/dd");

        String value = converter.toString(LocalDate.now());
        assertEquals(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")), value);

        assertNull(converter.toString(null));
    }

}
