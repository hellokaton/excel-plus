package io.github.biezhi.excel.plus.converter;

import io.github.biezhi.excel.plus.conveter.Converter;
import io.github.biezhi.excel.plus.conveter.LongConverter;
import io.github.biezhi.excel.plus.exception.ConverterException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author biezhi
 * @date 2018-12-13
 */
public class LongConverterTest {

    private Converter<String, Long> converter = new LongConverter();

    @Test
    public void testStringToR() throws ConverterException {
        assertEquals(new Long("123"), converter.stringToR("123"));
    }

    @Test(expected = ConverterException.class)
    public void testStringToRError() throws ConverterException {
        converter.stringToR("abc");
        converter.stringToR((Long.MAX_VALUE + 1) + "");
    }

    @Test
    public void testToString() throws ConverterException {
        String value = converter.toString(123L);
        assertEquals("123", value);
    }

}
