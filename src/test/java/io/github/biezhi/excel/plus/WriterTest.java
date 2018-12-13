package io.github.biezhi.excel.plus;

import io.github.biezhi.excel.plus.exception.WriterException;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * {@link Writer} Test
 *
 * @author biezhi
 * @date 2018-12-13
 */
public class WriterTest extends BaseTest {

    @Test
    public void testCreate() {
        Writer writer = Writer.create();

        Assert.assertNotNull(writer);

        Assert.assertNull(writer.rows());
        Assert.assertNull(writer.titleStyle());
        Assert.assertNull(writer.headerStyle());
        Assert.assertNull(writer.cellStyle());
        Assert.assertNull(writer.sheetConsumer());
        Assert.assertNull(writer.headerTitle());


        Assert.assertEquals(0, writer.startRow());
        Assert.assertEquals(100, writer.bufferSize());
        Assert.assertEquals("Sheet0", writer.sheet());

        Assert.assertFalse(writer.isRaw());
    }


    @Test
    public void testCreateWithArgs() throws WriterException {
        Writer writer = Writer.create();

        writer.withRows(buildData())
                .cellStyle((workbook, cellStyle) -> {
                })
                .headerTitle("Test Title")
                .to(new File("test_create_args.xlsx"));

        Assert.assertNotNull(writer);

        Assert.assertNotNull(writer.rows());
        Assert.assertNotNull(writer.cellStyle());
        Assert.assertNotNull(writer.headerTitle());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartRowError() {
        Writer.create().start(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSheetNameError() {
        Writer.create().sheet(null);
    }

}
