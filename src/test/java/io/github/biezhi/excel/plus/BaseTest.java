package io.github.biezhi.excel.plus;

import io.github.biezhi.excel.plus.exception.WriterException;
import io.github.biezhi.excel.plus.model.PerformanceTestModel;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Base Test
 *
 * @author biezhi
 * @date 2018-12-13
 */
@Slf4j
public class BaseTest {

    final int testCount = 1_0000;
    //    protected final int    testCount    = 100_0000;

    final String testFileName = "test_write_100w_rows.xlsx";

    ExcelPlus excelPlus = new ExcelPlus();

    List<PerformanceTestModel> readyData() {

        List<PerformanceTestModel> data = new ArrayList<>(testCount);

        LocalDate now    = LocalDate.now();
        Random    random = new Random();

        for (int i = 1; i <= testCount; i++) {
            data.add(new PerformanceTestModel(i, UUID.randomUUID().toString(), now,
                    new BigDecimal(String.valueOf(random.nextDouble() * 1000)).setScale(2, 0).doubleValue(),
                    new BigDecimal(String.valueOf(random.nextDouble() * 10000)).setScale(3, 0),
                    "15800001112", (byte) (i % 3))
            );
        }
        return data;
    }

    void writeTestExcel(List<PerformanceTestModel> rows) throws WriterException {
        log.info("data  ready !!!");
        log.info("start write !!!");
        excelPlus.write()
                .withRows(rows)
                .headerTitle("Test Write Model Excel")
                .to(new File(testFileName));
    }

    String classPath() {
        return BaseTest.class.getResource("/").getPath();
    }

    void deleteTempFile(String fileName) {
        try {
            Files.delete(Paths.get(fileName));
        } catch (IOException e) {
            log.warn("delete file {} fail", fileName, e);
        }
    }
}
