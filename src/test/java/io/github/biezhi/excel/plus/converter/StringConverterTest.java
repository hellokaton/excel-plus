package io.github.biezhi.excel.plus.converter;

import io.github.biezhi.excel.plus.conveter.Converter;
import io.github.biezhi.excel.plus.conveter.StringConverter;
import io.github.biezhi.excel.plus.exception.ConverterException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author biezhi
 * @date 2018-12-14
 */
public class StringConverterTest {

    private Converter<String, String> converter = new StringConverter();

    @Test
    public void testStringToR() throws ConverterException {
        assertEquals("abc", converter.stringToR("abc"));
        assertNull(converter.stringToR(null));
    }

    @Test
    public void testToString() throws ConverterException {
        assertEquals("123", converter.toString("123"));
        assertNull(converter.toString(null));
    }

}
