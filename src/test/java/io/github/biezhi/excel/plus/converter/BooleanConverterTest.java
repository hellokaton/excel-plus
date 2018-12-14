package io.github.biezhi.excel.plus.converter;

import io.github.biezhi.excel.plus.conveter.BooleanConverter;
import io.github.biezhi.excel.plus.conveter.Converter;
import io.github.biezhi.excel.plus.exception.ConverterException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author biezhi
 * @date 2018-12-13
 */
public class BooleanConverterTest {

    private Converter<String, Boolean> converter = new BooleanConverter();

    @Test
    public void testStringToR() throws ConverterException {
        assertTrue(converter.stringToR("true"));
        assertFalse(converter.stringToR("false"));
        assertFalse(converter.stringToR("qqac"));

        assertTrue(converter.stringToR("TRUE"));
        assertFalse(converter.stringToR("FALSE"));
    }

    @Test
    public void testToString() throws ConverterException {
        assertEquals("true", converter.toString(true));
        assertEquals("false", converter.toString(false));
    }

}
