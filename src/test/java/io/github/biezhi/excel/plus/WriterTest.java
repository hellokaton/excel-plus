package io.github.biezhi.excel.plus;

import io.github.biezhi.excel.plus.exception.WriterException;
import io.github.biezhi.excel.plus.model.PerformanceTestModel;
import org.junit.Test;

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
 * @author biezhi
 * @date 2018-12-11
 */
public class WriterTest {

    @Test
    public void testWrite100wRows() throws WriterException, IOException {
        int count = 100_0000;
//        int    count    = 10;
        String fileName = "test_write.xlsx";

        ExcelPlus excelPlus = new ExcelPlus();

        List<PerformanceTestModel> data   = new ArrayList<>(count);
        LocalDate                  now    = LocalDate.now();
        Random                     random = new Random();
        for (int i = 0; i < count; i++) {
            data.add(new PerformanceTestModel(i, UUID.randomUUID().toString(), now,
                    new BigDecimal(String.valueOf(random.nextDouble() * 1000)).setScale(2, 0).doubleValue(),
                    new BigDecimal(String.valueOf(random.nextDouble() * 10000)).setScale(3, 0),
                    "15828395124", (byte) (i % 3))
            );
        }

        System.out.println("Data ready!!!");

        long start = System.currentTimeMillis();
        excelPlus.write()
                .withRows(data)
                .headerTitle("Test Write Model Excel")
                .to(new File(fileName));

        long end = System.currentTimeMillis();
        System.out.println("Write " + count + " rows, time consuming: " + (end - start) + "ms");
        // If you want to open the file view, please comment this line
        Files.delete(Paths.get(fileName));
    }

}
