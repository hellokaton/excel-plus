package io.github.biezhi.excel.plus;

import io.github.biezhi.excel.plus.exception.ReaderException;
import io.github.biezhi.excel.plus.model.Sample;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * {@link Reader} Test
 *
 * @author biezhi
 * @date 2018-12-13
 */
public class ReaderTest extends BaseTest {

    @Test
    public void testCreate() {
        Reader reader = Reader.create(null);

        assertNotNull(reader);

        assertNull(reader.modelType());
        assertNull(reader.fromFile());
        assertNull(reader.fromStream());
        assertNull(reader.sheetName());

        assertEquals(2, reader.startRow());
        assertEquals(0, reader.sheetIndex());

    }

    @Test
    public void testCreateByFile() {
        Reader reader = Reader.create(Sample.class, new File(classPath() + "/SampleData.xlsx"));

        assertNotNull(reader);
        assertNotNull(reader.fromFile());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateByFileNotExist() {
        Reader.create(null, new File("abc.xlsx"));
    }

    @Test
    public void testReaderArgs() {
        Reader reader = Reader.create(Sample.class);

        reader.from(new File(classPath() + "/SampleData.xlsx"))
                .sheet(1)
                .start(1)
                .sheet("SalesOrders");

        assertNotNull(reader.fromFile());

        assertEquals(1, reader.startRow());
        assertEquals(1, reader.sheetIndex());
        assertEquals("SalesOrders", reader.sheetName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartRowError() {
        Reader.create(null).start(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSheetIndexError() {
        Reader.create(null).sheet(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSheetNameError() {
        Reader.create(null).sheet(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testModelTypeError() throws ReaderException {
        Reader.create(null).asList();
    }

}
