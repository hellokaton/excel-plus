package io.github.biezhi.excel.plus.converter;

import io.github.biezhi.excel.plus.conveter.Converter;
import io.github.biezhi.excel.plus.conveter.DecimalConverter;
import io.github.biezhi.excel.plus.conveter.DoubleConverter;
import io.github.biezhi.excel.plus.exception.ConverterException;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author biezhi
 * @date 2018-12-14
 */
public class DoubleConverterTest {

    private Converter<String, Double> converter = new DoubleConverter();

    @Test
    public void testStringToR() throws ConverterException {
        assertEquals(new Double("123"), converter.stringToR("123"));
        assertNull(converter.stringToR(null));
    }

    @Test(expected = ConverterException.class)
    public void testStringToRError() throws ConverterException {
        converter.stringToR("abc");
    }

    @Test
    public void testToString() throws ConverterException {
        assertEquals("123.0", converter.toString(new Double("123")));
        assertEquals("123.0", converter.toString(123D));
        assertNull(converter.toString(null));
    }

}
