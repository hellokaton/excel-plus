package io.github.biezhi.excel.plus.converter;

import io.github.biezhi.excel.plus.conveter.Converter;
import io.github.biezhi.excel.plus.conveter.ConverterCache;
import io.github.biezhi.excel.plus.model.Sample;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author biezhi
 * @date 2018-12-15
 */
public class ConverterCacheTest {

    static class TestStatusConv implements Converter<String, Integer> {
        @Override
        public Integer stringToR(String value) {
            if ("enable".equals(value)) {
                return 1;
            }
            return 0;
        }

        @Override
        public String toString(Integer value) {
            if (null == value) {
                return null;
            } else if (value == 1) {
                return "enable";
            }
            return "disable";
        }
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
        Field     field     = Sample.class.getDeclaredField("date");
        Converter converter = ConverterCache.computeConvert(field);
        assertNotNull(converter);

        assertNull(ConverterCache.computeConvert(null));
    }

}
