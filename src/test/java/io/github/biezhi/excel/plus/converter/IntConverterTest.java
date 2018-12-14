package io.github.biezhi.excel.plus.converter;

import io.github.biezhi.excel.plus.conveter.Converter;
import io.github.biezhi.excel.plus.conveter.IntConverter;
import io.github.biezhi.excel.plus.exception.ConverterException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author biezhi
 * @date 2018-12-13
 */
public class IntConverterTest {

    private Converter<String, Integer> converter = new IntConverter();

    @Test
    public void testStringToR() throws ConverterException {
        int num1 = converter.stringToR("123");
        assertEquals(123, num1);

        int num2 = converter.stringToR(Integer.MAX_VALUE + "");
        assertEquals(Integer.MAX_VALUE, num2);
    }

    @Test
    public void testStringToRError() throws ConverterException {
        Executable e = () -> converter.stringToR("abc");
        assertThrows(ConverterException.class, e);
    }

    @Test
    public void testToString() throws ConverterException {
        String value = converter.toString(123);
        assertEquals("123", value);
    }

}
