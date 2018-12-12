package io.github.biezhi.excel.plus;

import io.github.biezhi.excel.plus.exception.WriterException;
import io.github.biezhi.excel.plus.model.PerformanceTestModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author biezhi
 * @date 2018-12-11
 */
@Slf4j
public class WriterTest extends BaseTest {

    @Test
    public void testWrite100wRows() throws WriterException, IOException {
        List<PerformanceTestModel> rows = readyData();

        long start = System.currentTimeMillis();
        writeTestExcel(rows);
        long end = System.currentTimeMillis();

        log.info("Write " + testCount + " rows, time consuming: " + (end - start) + "ms");
        // If you want to open the file view, please comment this line
        Files.delete(Paths.get(testFileName));
    }

}
