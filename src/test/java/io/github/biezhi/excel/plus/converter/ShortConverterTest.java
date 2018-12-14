package io.github.biezhi.excel.plus.converter;

import io.github.biezhi.excel.plus.conveter.Converter;
import io.github.biezhi.excel.plus.conveter.DoubleConverter;
import io.github.biezhi.excel.plus.conveter.ShortConverter;
import io.github.biezhi.excel.plus.exception.ConverterException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author biezhi
 * @date 2018-12-14
 */
public class ShortConverterTest {

    private Converter<String, Short> converter = new ShortConverter();

    @Test
    public void testStringToR() throws ConverterException {
        assertEquals(new Short("123"), converter.stringToR("123"));
        assertNull(converter.stringToR(null));
    }

    @Test(expected = ConverterException.class)
    public void testStringToRError() throws ConverterException {
        converter.stringToR("abc");
    }

    @Test
    public void testToString() throws ConverterException {
        assertEquals("123", converter.toString(new Short("123")));
        assertEquals("123", converter.toString((short) 123));
        assertNull(converter.toString(null));
    }

}
