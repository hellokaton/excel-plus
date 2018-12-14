package io.github.biezhi.excel.plus;

import io.github.biezhi.excel.plus.exception.WriterException;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;


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

        assertNotNull(writer);

        assertNull(writer.rows());
        assertNull(writer.titleStyle());
        assertNull(writer.headerStyle());
        assertNull(writer.cellStyle());
        assertNull(writer.sheetConsumer());
        assertNull(writer.headerTitle());


        assertEquals(0, writer.startRow());
        assertEquals(100, writer.bufferSize());
        assertEquals("Sheet0", writer.sheet());

        assertFalse(writer.isRaw());
    }


    @Test
    public void testCreateWithArgs() throws WriterException {
        Writer writer = Writer.create();

        writer.withRows(buildData())
                .cellStyle((workbook, cellStyle) -> {
                })
                .headerTitle("Test Title")
                .to(new File("test_create_args.xlsx"));

        assertNotNull(writer);

        assertNotNull(writer.rows());
        assertNotNull(writer.cellStyle());
        assertNotNull(writer.headerTitle());
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
