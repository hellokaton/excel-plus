package io.github.biezhi.excel.plus;

import io.github.biezhi.excel.plus.exception.WriterException;
import io.github.biezhi.excel.plus.model.PerformanceTestModel;
import io.github.biezhi.excel.plus.model.Sample;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.junit.Test;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
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

    @Test
    public void testWriteSample() throws WriterException {
        List<Sample> samples = new ArrayList<>();
        samples.add(new Sample(LocalDate.now(), "hello01", 101));
        samples.add(new Sample(LocalDate.now(), "hello02", 102));
        samples.add(new Sample(LocalDate.now(), "hello03", 103));
        samples.add(new Sample(LocalDate.now(), "hello04", 104));
        samples.add(new Sample(LocalDate.now(), "hello05", 105));
        excelPlus.write()
                .headerTitle("一份简单的Excel表格")
                .withRows(samples)
                .to(new File("sample_test.xlsx"));
    }

    @Test
    public void testWriteCustomStyle() throws WriterException {
        List<Sample> samples = new ArrayList<>();
        samples.add(new Sample(LocalDate.now(), "hello01", 101));
        samples.add(new Sample(LocalDate.now(), "hello02", 102));
        samples.add(new Sample(LocalDate.now(), "hello03", 103));
        samples.add(new Sample(LocalDate.now(), "hello04", 104));
        samples.add(new Sample(LocalDate.now(), "hello05", 105));
        excelPlus.write()
                .headerTitle("一份自定义样式的Excel表格")
                .withRows(samples)
                .titleStyle((wb, style) -> {
                    Font font = wb.createFont();
                    font.setFontHeightInPoints((short) 40);
                    font.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
                    style.setFont(font);
                })
                .headerStyle((wb, style) -> {
                    Font font = wb.createFont();
                    font.setFontHeightInPoints((short) 20);
                    font.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
                    style.setFont(font);
                })
                .cellStyle((wb, style) -> {
                    Font font = wb.createFont();
                    font.setFontHeightInPoints((short) 20);
                    font.setColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
                    style.setFont(font);
                })
                .to(new File("sample_custom_style_test.xlsx"));
    }

}
