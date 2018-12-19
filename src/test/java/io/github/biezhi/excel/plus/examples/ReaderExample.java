package io.github.biezhi.excel.plus.examples;

import io.github.biezhi.excel.plus.BaseTest;
import io.github.biezhi.excel.plus.Reader;
import io.github.biezhi.excel.plus.exception.ReaderException;
import io.github.biezhi.excel.plus.model.AAA;
import io.github.biezhi.excel.plus.model.Financial;
import io.github.biezhi.excel.plus.model.PerformanceTestModel;
import io.github.biezhi.excel.plus.model.Sample;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import sun.nio.cs.ext.GBK;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

/**
 * @author biezhi
 * @date 2018-12-11
 */
@Slf4j
public class ReaderExample extends BaseTest {

    @Test
    public void testReadBasic() throws ReaderException {
        List<Financial> financials = Reader.create(Financial.class)
                .from(new File(classPath() + "/FinancialSample.xlsx"))
                .asList();

        log.info("financials size: {}", financials.size());
    }

    @Test
    public void testRead100wRows() throws Exception {
        List<PerformanceTestModel> rows = readyData();
        writeTestExcel(rows);
        log.info("write done !!!");

        long start = System.currentTimeMillis();

        List<PerformanceTestModel> list = Reader.create(PerformanceTestModel.class)
                .from(new File(testFileName))
                .asList();

        long end = System.currentTimeMillis();
        log.info("Read " + list.size() + " rows, time consuming: " + (end - start) + "ms");
        // If you want to open the file view, please comment this line
        Files.delete(Paths.get(testFileName));
    }

    @Test
    public void testReadBySheetIndex() throws ReaderException {
        System.out.println(classPath());
        List<Sample> samples = Reader.create(Sample.class)
                .from(new File(classPath() + "/SampleData.xlsx"))
                .sheet(1)
                .start(1)
                .asList();

        assertEquals(43, samples.size());
        assertEquals(new BigDecimal("189.05"), samples.get(0).getAmount());
        assertEquals(new BigDecimal("139.72"), samples.get(samples.size() - 1).getAmount());
    }

    @Test
    public void testReadBySheetName() throws ReaderException {
        List<Sample> samples = Reader.create(Sample.class)
                .from(new File(classPath() + "/SampleData.xlsx"))
                .sheet("SalesOrders")
                .start(1)
                .asList();

        assertEquals(43, samples.size());
        assertEquals(new BigDecimal("189.05"), samples.get(0).getAmount());
        assertEquals(new BigDecimal("139.72"), samples.get(samples.size() - 1).getAmount());
    }

    @Test
    public void testReadAndFilter() throws ReaderException {
        List<Sample> samples = Reader.create(Sample.class)
                .from(new File(classPath() + "/SampleData.xlsx"))
                .sheet("SalesOrders")
                .start(1)
                .asStream()
                .filter(sample -> sample.getAmount().intValue() > 1000)
                .collect(toList());

        assertEquals(6, samples.size());
        assertEquals(new BigDecimal("1619.19"), samples.get(0).getAmount());
        assertEquals(new BigDecimal("1879.06"), samples.get(samples.size() - 1).getAmount());
    }

    @Test
    public void testReadAndValid() throws ReaderException {
        List<Sample> samples = Reader.create(Sample.class)
                .from(new File(classPath() + "/SampleData.xlsx"))
                .sheet("SalesOrders")
                .start(1)
                .asStream()
                .filter(sample -> sample.getAmount().intValue() > 1000)
                .collect(toList());

        assertEquals(6, samples.size());
        assertEquals(new BigDecimal("1619.19"), samples.get(0).getAmount());
        assertEquals(new BigDecimal("1879.06"), samples.get(samples.size() - 1).getAmount());
    }

    @Test
    public void testRead111(){
//        List<AAA> aaas = Reader.create(AAA.class)
//                .from(new File(classPath() + "/111.csv"))
//                .start(1)
//                .charset(StandardCharsets.ISO_8859_1)
//                .asList();
//        System.out.println(aaas);

        List<Sample> samples = Reader.create(Sample.class)
                .from(new File("write_as_csv.csv"))
                .asList();
        System.out.println(samples);
    }

}
