package io.github.biezhi.excel.plus;

import io.github.biezhi.excel.plus.exception.ReaderException;
import io.github.biezhi.excel.plus.model.Financial;
import io.github.biezhi.excel.plus.model.PerformanceTestModel;
import io.github.biezhi.excel.plus.model.Sample;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author biezhi
 * @date 2018-12-11
 */
@Slf4j
public class ReaderTest extends BaseTest {

    @Test
    public void testReadBasic() throws ReaderException {
        List<Financial> financials = excelPlus.read()
                .from(new File(classPath() + "/FinancialSample.xlsx"))
                .asList(Financial.class);
        System.out.println(financials);
    }

    @Test
    public void testRead100wRows() throws Exception {
        List<PerformanceTestModel> rows = readyData();
        writeTestExcel(rows);
        log.info("write done !!!");

        long start = System.currentTimeMillis();

        List<PerformanceTestModel> list = excelPlus.read()
                .from(new File(testFileName))
                .asList(PerformanceTestModel.class);

        long end = System.currentTimeMillis();
        log.info("Read " + list.size() + " rows, time consuming: " + (end - start) + "ms");
        // If you want to open the file view, please comment this line
        Files.delete(Paths.get(testFileName));
    }

    @Test
    public void testReadBySheetIndex() throws ReaderException {
        System.out.println(classPath());
        List<Sample> samples = excelPlus.read()
                .from(new File(classPath() + "/SampleData.xlsx"))
                .sheetIndex(1)
                .startRow(1)
                .asList(Sample.class);

        Assert.assertEquals(43, samples.size());
        Assert.assertEquals(new BigDecimal("189.05"), samples.get(0).getAmount());
        Assert.assertEquals(new BigDecimal("139.72"), samples.get(samples.size() - 1).getAmount());
    }

    @Test
    public void testReadBySheetName() throws ReaderException {
        System.out.println(classPath());
        List<Sample> samples = excelPlus.read()
                .from(new File(classPath() + "/SampleData.xlsx"))
                .sheetName("SalesOrders")
                .startRow(1)
                .asList(Sample.class);

        Assert.assertEquals(43, samples.size());
        Assert.assertEquals(new BigDecimal("189.05"), samples.get(0).getAmount());
        Assert.assertEquals(new BigDecimal("139.72"), samples.get(samples.size() - 1).getAmount());
    }

}
