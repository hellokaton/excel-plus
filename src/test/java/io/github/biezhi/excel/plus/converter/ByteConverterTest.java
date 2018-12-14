package io.github.biezhi.excel.plus.converter;

import io.github.biezhi.excel.plus.conveter.ByteConverter;
import io.github.biezhi.excel.plus.conveter.Converter;
import io.github.biezhi.excel.plus.exception.ConverterException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author biezhi
 * @date 2018-12-13
 */
public class ByteConverterTest {

    private Converter<String, Byte> converter = new ByteConverter();

    @Test
    public void testStringToR() throws ConverterException {
        assertEquals(java.util.Optional.of((byte) 123).get(), converter.stringToR("123"));
    }

    @Test(expected = ConverterException.class)
    public void testStringToRError() throws ConverterException {
        converter.stringToR(Integer.MAX_VALUE + "");
        converter.stringToR("abc");
    }

    @Test
    public void testToString() throws ConverterException {
        assertEquals("123", converter.toString((byte) 123));
    }

}
