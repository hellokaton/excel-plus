package io.github.biezhi.excel.plus.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author biezhi
 * @date 2018-12-14
 */
public class StringUtilTest {

    private String a = null;
    private String b = "";
    private String c = "hello";

    @Test
    public void testEmpty(){
        assertTrue(StringUtil.isEmpty(a));
        assertTrue(StringUtil.isEmpty(b));
        assertFalse(StringUtil.isEmpty(c));
    }

    @Test
    public void testNotEmpty(){
        assertFalse(StringUtil.isNotEmpty(a));
        assertFalse(StringUtil.isNotEmpty(b));
        assertTrue(StringUtil.isNotEmpty(c));
    }

}
