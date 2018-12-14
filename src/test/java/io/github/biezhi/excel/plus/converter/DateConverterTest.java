package io.github.biezhi.excel.plus.converter;

import io.github.biezhi.excel.plus.conveter.Converter;
import io.github.biezhi.excel.plus.conveter.DateConverter;
import io.github.biezhi.excel.plus.exception.ConverterException;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author biezhi
 * @date 2018-12-14
 */
public class DateConverterTest {

    @Test
    public void testStringToR() throws ConverterException {
        Converter<String, Date> converter = new DateConverter("yyyy/MM/dd");

        Date date = converter.stringToR("2018/12/12");

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        assertEquals(2018, cal.get(Calendar.YEAR));
        assertEquals(12, cal.get(Calendar.MONTH) + 1);
        assertEquals(12, cal.get(Calendar.DAY_OF_MONTH));

        assertNull(converter.stringToR(null));
    }

    @Test(expected = ConverterException.class)
    public void testStringToRError() throws ConverterException {
        Converter<String, Date> converter = new DateConverter("yyyy/MM/dd");
        converter.stringToR("2018-12-12");
    }

    @Test
    public void testToString() throws ConverterException {
        Converter<String, Date> converter = new DateConverter("yyyy/MM/dd");

        String value = converter.toString(new Date());
        assertEquals(new SimpleDateFormat("yyyy/MM/dd").format(new Date()), value);

        assertNull(converter.toString(null));
    }

    @Test(expected = ConverterException.class)
    public void testToStringError() throws ConverterException {
        Converter<String, Date> converter = new DateConverter("abcd");
        converter.toString(new Date());
    }

}
