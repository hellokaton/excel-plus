package io.github.biezhi.excel.plus;

import io.github.biezhi.excel.plus.enums.ParseType;
import io.github.biezhi.excel.plus.model.TestBean;
import io.github.biezhi.excel.plus.reader.Reader;
import io.github.biezhi.excel.plus.reader.ReaderResult;
import io.github.biezhi.excel.plus.reader.ValidRow;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author darren <yangdaiquan@yiguo.com>
 * @description  读取Excel demo
 * @date 2018/11/8 13:43
 */
@Slf4j
public class ReadTest {

    private ExcelPlus excelPlus = new ExcelPlus();

    private File getTestBeanFile() {
        try {
            return new File(URLDecoder.decode(ReadTest.class.getResource("/TestBean.xlsx").getPath(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    @Test
    public void read() throws Exception{
        Reader reader = Reader.create()
                .excelFile(getTestBeanFile())
                .parseType(ParseType.SAX);

        ReaderResult<TestBean> readerResult = new ExcelPlus()
                .read(TestBean.class, reader)
                .valid((row,bean)->{
                    bean.setRowNum(row);//没提供针对内部result的直接操作，也没有peek方法，所以只能在valid中切入了
                    return ValidRow.ok();
                });

        readerResult.asList().forEach(System.out::println);


    }


}
