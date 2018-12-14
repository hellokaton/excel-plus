package io.github.biezhi.excel.plus.writer;

import io.github.biezhi.excel.plus.exception.WriterException;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;

/**
 * @author biezhi
 * @date 2018-12-15
 */
public class ResponseWrapperTest {

    @Test(expected = WriterException.class)
    public void testCreate() throws WriterException {
        HttpServletResponse response = null;
        ResponseWrapper.create(response, "abc.xlsx");
    }

}
