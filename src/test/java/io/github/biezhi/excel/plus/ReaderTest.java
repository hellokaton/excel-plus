package io.github.biezhi.excel.plus;

import io.github.biezhi.excel.plus.exception.ReaderException;
import io.github.biezhi.excel.plus.model.Sample;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void testCreateByFileNotExist() {
        Executable e = () -> Reader.create(null, new File("abc.xlsx"));
        assertThrows(IllegalArgumentException.class, e);
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

    @Test
    public void testStartRowError() {
        Executable e = () -> Reader.create(null).start(-1);
        assertThrows(IllegalArgumentException.class, e);
    }

    @Test
    public void testSheetIndexError() {
        Executable e = () -> Reader.create(null).sheet(-1);
        assertThrows(IllegalArgumentException.class, e);
    }

    @Test
    public void testSheetNameError() {
        Executable e = () -> Reader.create(null).sheet(null);
        assertThrows(IllegalArgumentException.class, e);
    }

    @Test
    public void testModelTypeError() throws ReaderException {
        Executable e = () -> Reader.create(null).asList();
        assertThrows(IllegalArgumentException.class, e);
    }

}
