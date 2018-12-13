package io.github.biezhi.excel.plus.converter;

import io.github.biezhi.excel.plus.conveter.Converter;
import io.github.biezhi.excel.plus.conveter.IntConverter;
import io.github.biezhi.excel.plus.exception.ConverterException;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author biezhi
 * @date 2018-12-13
 */
public class IntConverterTest {

    private Converter<String, Integer> converter = new IntConverter();

    @Test
    public void testStringToR() throws ConverterException {
        int num1 = converter.stringToR("123");
        Assert.assertEquals(123, num1);

        int num2 = converter.stringToR(Integer.MAX_VALUE + "");
        Assert.assertEquals(Integer.MAX_VALUE, num2);
    }

    @Test(expected = ConverterException.class)
    public void testStringToRError() throws ConverterException {
        converter.stringToR("abc");
    }

    @Test
    public void testToString() throws ConverterException {
        String value = converter.toString(123);
        Assert.assertEquals("123", value);
    }

}
