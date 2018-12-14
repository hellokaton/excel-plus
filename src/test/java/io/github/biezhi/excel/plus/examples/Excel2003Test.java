package io.github.biezhi.excel.plus.examples;

import io.github.biezhi.excel.plus.BaseTest;
import io.github.biezhi.excel.plus.Reader;
import io.github.biezhi.excel.plus.Writer;
import io.github.biezhi.excel.plus.enums.ExcelType;
import io.github.biezhi.excel.plus.exception.ReaderException;
import io.github.biezhi.excel.plus.exception.WriterException;
import io.github.biezhi.excel.plus.model.Sample;
import org.junit.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * XLS Test
 *
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

//        deleteTempFile(fileName);
    }

    @Test
    public void testRead2003() throws WriterException, ReaderException {
        testWrite2003();

        List<Sample> samples = Reader.create(Sample.class)
                .from(new File("sample_test.xls"))
                .asList();

        assertEquals(5, samples.size());
        assertEquals("hello01", samples.get(0).getLocation());
        assertEquals("hello05", samples.get(samples.size() - 1).getLocation());
    }

}
