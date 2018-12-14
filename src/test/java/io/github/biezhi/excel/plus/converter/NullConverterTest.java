package io.github.biezhi.excel.plus.converter;

import io.github.biezhi.excel.plus.conveter.NullConverter;
import org.junit.Test;

/**
 * @author biezhi
 * @date 2018-12-15
 */
public class NullConverterTest {

    @Test(expected = RuntimeException.class)
    public void testStringToR(){
        new NullConverter().stringToR("hello");
    }

}
