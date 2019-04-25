package io.github.biezhi.excel.plus;

import io.github.biezhi.excel.plus.exception.WriterException;
import org.junit.Test;

import java.io.File;
import java.util.stream.Stream;

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
        assertEquals("Sheet0", writer.sheetName());

        assertFalse(writer.isRaw());
    }


    @Test(expected = WriterException.class)
    public void testRowsIsNull() throws WriterException {
        String fileName = "test_temp_012.xlsx";
        Writer.create().to(new File(fileName));
        deleteTempFile(fileName);
    }

    @Test
    public void testChangeStartRow() {
        Writer writer = Writer.create().start(10);
        assertNotNull(writer);
        assertEquals(10, writer.startRow());
    }

    @Test
    public void testWithTemplate() {
        Writer writer = Writer.create().withTemplate(classPath() + "/template.xlsx");

        assertNotNull(writer);
        assertNotNull(writer.template());
        assertTrue(writer.template().exists());

        writer = Writer.create().withTemplate(new File(classPath() + "/template.xlsx"));

        assertNotNull(writer);
        assertNotNull(writer.template());
        assertTrue(writer.template().exists());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullTemplate(){
        Writer.create().withTemplate((File) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotExistTemplate(){
        Writer.create().withTemplate(new File("abcd.12346"));
    }

    @Test
    public void testChangeBufferSize(){
        Writer writer = Writer.create().bufferSize(200);

        assertNotNull(writer);
        assertEquals(200, writer.bufferSize());
    }

    @Test
    public void testCreateWithArgs() throws WriterException {
        String fileName = "test_create_args.xlsx";

        Writer writer = Writer.create();

        writer.withRows(buildData())
                .headerTitle("Test Title")
                .to(new File(fileName));

        assertNotNull(writer);

        assertNotNull(writer.rows());
        assertNotNull(writer.cellStyle());
        assertNotNull(writer.headerTitle());

        deleteTempFile(fileName);
    }

    @Test
    public void testCustomTitleStyle(){
        Writer writer = Writer.create();

        assertNotNull(writer);
        assertNotNull(writer.titleStyle());
    }

    @Test
    public void testCustomHeaderStyle(){
        Writer writer = Writer.create();

        assertNotNull(writer);
        assertNotNull(writer.headerStyle());
    }

    @Test
    public void testWithRaw(){
        Writer writer = Writer.create().withRaw();

        assertNotNull(writer);
        assertTrue(writer.isRaw());
    }

    @Test
    public void testChangeSheetName() {
        String sheetName = "MySheet";

        Writer writer = Writer.create();
        writer.sheet(sheetName);

        assertEquals(sheetName, writer.sheetName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartRowError() {
        Writer.create().start(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSheetNameError() {
        Writer.create().sheet(null);
    }

    @Test(expected = WriterException.class)
    public void testToNotExistFile() throws WriterException {
        Writer.create().withRows(buildData()).to(new File("/a/b/c"));
    }

    @Test
    public void testWriteCSV() throws WriterException {
        String fileName = "write_csv_test.csv";
        Writer.create().withRows(buildData()).to(new File(fileName));
        deleteTempFile(fileName);
    }

}
