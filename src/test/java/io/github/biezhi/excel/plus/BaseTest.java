package io.github.biezhi.excel.plus;

import io.github.biezhi.excel.plus.exception.WriterException;
import io.github.biezhi.excel.plus.model.PerformanceTestModel;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @author biezhi
 * @date 2018-12-13
 */
@Slf4j
public class BaseTest {

    protected final int    testCount    = 1_0000;
//    protected final int    testCount    = 100_0000;
    protected final String testFileName = "test_write.xlsx";

    protected ExcelPlus excelPlus = new ExcelPlus();

    protected List<PerformanceTestModel> readyData() {

        List<PerformanceTestModel> data = new ArrayList<>(testCount);

        LocalDate now    = LocalDate.now();
        Random    random = new Random();

        for (int i = 1; i <= testCount; i++) {
            data.add(new PerformanceTestModel(i, UUID.randomUUID().toString(), now,
                    new BigDecimal(String.valueOf(random.nextDouble() * 1000)).setScale(2, 0).doubleValue(),
                    new BigDecimal(String.valueOf(random.nextDouble() * 10000)).setScale(3, 0),
                    "15828395124", (byte) (i % 3))
            );
        }
        return data;
    }

    protected void writeTestExcel(List<PerformanceTestModel> rows) throws WriterException {
        log.info("data  ready !!!");
        log.info("start write !!!");
        excelPlus.write()
                .withRows(rows)
                .headerTitle("Test Write Model Excel")
                .to(new File(testFileName));
    }

    protected String classPath(){
        return BaseTest.class.getResource("/").getPath();
    }

}
