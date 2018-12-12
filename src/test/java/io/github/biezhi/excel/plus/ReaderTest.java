package io.github.biezhi.excel.plus;

import io.github.biezhi.excel.plus.model.PerformanceTestModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;
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

}
