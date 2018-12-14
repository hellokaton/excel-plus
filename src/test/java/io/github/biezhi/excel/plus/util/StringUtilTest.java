package io.github.biezhi.excel.plus.util;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

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
