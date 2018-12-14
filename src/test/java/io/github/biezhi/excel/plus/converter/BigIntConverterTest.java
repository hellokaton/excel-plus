package io.github.biezhi.excel.plus.converter;

import io.github.biezhi.excel.plus.conveter.BigIntConverter;
import io.github.biezhi.excel.plus.conveter.Converter;
import io.github.biezhi.excel.plus.exception.ConverterException;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

/**
 * @author biezhi
 * @date 2018-12-14
 */
public class BigIntConverterTest {

    private Converter<String, BigInteger> converter = new BigIntConverter();

    @Test
    public void testStringToR() throws ConverterException {
        BigInteger num1 = converter.stringToR("123");
        assertEquals(new BigInteger("123"), num1);

        BigInteger num2 = converter.stringToR(Integer.MAX_VALUE + "");
        assertEquals(new BigInteger(Integer.MAX_VALUE + ""), num2);
    }

    @Test(expected = ConverterException.class)
    public void testStringToRError() throws ConverterException {
        converter.stringToR("abc");
    }

    @Test
    public void testToString() throws ConverterException {
        String value = converter.toString(new BigInteger("123"));
        assertEquals("123", value);
    }

}
