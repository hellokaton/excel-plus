package io.github.biezhi.excel.plus;

import io.github.biezhi.excel.plus.enums.ExcelType;
import io.github.biezhi.excel.plus.exception.ReaderException;
import io.github.biezhi.excel.plus.exception.WriterException;
import io.github.biezhi.excel.plus.model.Sample;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author biezhi
 * @date 2018-12-13
 */
public class Excel2003Test extends BaseTest {

    @Test
    public void testWrite2003() throws WriterException {
        String fileName = "sample_test.xls";

        List<Sample> samples = new ArrayList<>();
        samples.add(new Sample(LocalDate.now(), "hello01", 101));
        samples.add(new Sample(LocalDate.now(), "hello02", 102));
        samples.add(new Sample(LocalDate.now(), "hello03", 103));
        samples.add(new Sample(LocalDate.now(), "hello04", 104));
        samples.add(new Sample(LocalDate.now(), "hello05", 105));

        Writer.create(ExcelType.XLS)
                .headerTitle("一份简单的Excel表格")
                .withRows(samples)
                .to(new File(fileName));

        deleteTempFile(fileName);
    }

    @Test
    public void testRead2003() throws WriterException, ReaderException {
        testWrite2003();

        List<Sample> samples = Reader.create()
                .from(new File("sample_test.xls"))
                .asList(Sample.class);

        Assert.assertEquals(5, samples.size());
        Assert.assertEquals("hello01", samples.get(0).getLocation());
        Assert.assertEquals("hello05", samples.get(samples.size() - 1).getLocation());
    }

}
