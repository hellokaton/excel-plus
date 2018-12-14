package io.github.biezhi.excel.plus.converter;

import io.github.biezhi.excel.plus.annotation.ExcelColumn;
import io.github.biezhi.excel.plus.conveter.*;
import io.github.biezhi.excel.plus.model.Sample;
import org.junit.Test;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author biezhi
 * @date 2018-12-15
 */
public class ConverterCacheTest {

    static class NomalModel {
        long  l1;
        float f1;
        short s1;
        byte  b1;

        @ExcelColumn(datePattern = "yyyy-MM-dd")
        Date d1;

        BigInteger bi1;

        @ExcelColumn(datePattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime ldt1;

        @ExcelColumn(converter = TestStatusConv.class)
        Integer status;

        @ExcelColumn
        String normalConvert;
    }

    @Test
    public void testAddConvert() {
        Converter converter = ConverterCache.getConvert(TestStatusConv.class);
        assertNull(converter);

        ConverterCache.addConvert(new TestStatusConv());
        converter = ConverterCache.getConvert(TestStatusConv.class);
        assertNotNull(converter);
    }

    @Test
    public void testComputeConverter() throws Exception {
        Converter converter = ConverterCache.computeConvert(Sample.class.getDeclaredField("date"));
        assertNotNull(converter);
        assertEquals(LocalDateConverter.class, converter.getClass());

        converter = ConverterCache.computeConvert(Sample.class.getDeclaredField("location"));
        assertNotNull(converter);
        assertEquals(StringConverter.class, converter.getClass());

        converter = ConverterCache.computeConvert(Sample.class.getDeclaredField("proportion"));
        assertNotNull(converter);
        assertEquals(IntConverter.class, converter.getClass());

        converter = ConverterCache.computeConvert(Sample.class.getDeclaredField("ss"));
        assertNotNull(converter);
        assertEquals(DoubleConverter.class, converter.getClass());

        converter = ConverterCache.computeConvert(Sample.class.getDeclaredField("amount"));
        assertNotNull(converter);
        assertEquals(DecimalConverter.class, converter.getClass());

        converter = ConverterCache.computeConvert(NomalModel.class.getDeclaredField("l1"));
        assertNotNull(converter);
        assertEquals(LongConverter.class, converter.getClass());

        converter = ConverterCache.computeConvert(NomalModel.class.getDeclaredField("f1"));
        assertNotNull(converter);
        assertEquals(FloatConverter.class, converter.getClass());

        converter = ConverterCache.computeConvert(NomalModel.class.getDeclaredField("s1"));
        assertNotNull(converter);
        assertEquals(ShortConverter.class, converter.getClass());

        converter = ConverterCache.computeConvert(NomalModel.class.getDeclaredField("b1"));
        assertNotNull(converter);
        assertEquals(ByteConverter.class, converter.getClass());

        converter = ConverterCache.computeConvert(NomalModel.class.getDeclaredField("d1"));
        assertNotNull(converter);
        assertEquals(DateConverter.class, converter.getClass());

        converter = ConverterCache.computeConvert(NomalModel.class.getDeclaredField("bi1"));
        assertNotNull(converter);
        assertEquals(BigIntConverter.class, converter.getClass());

        converter = ConverterCache.computeConvert(NomalModel.class.getDeclaredField("ldt1"));
        assertNotNull(converter);
        assertEquals(LocalDateTimeConverter.class, converter.getClass());

        converter = ConverterCache.computeConvert(NomalModel.class.getDeclaredField("status"));
        assertNotNull(converter);
        assertEquals(TestStatusConv.class, converter.getClass());

        assertNull(ConverterCache.computeConvert(null));
    }

}
