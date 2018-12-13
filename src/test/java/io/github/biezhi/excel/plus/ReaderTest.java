package io.github.biezhi.excel.plus;

import io.github.biezhi.excel.plus.exception.ReaderException;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * {@link Reader} Test
 *
 * @author biezhi
 * @date 2018-12-13
 */
public class ReaderTest extends BaseTest {

    @Test
    public void testCreate() {
        Reader reader = Reader.create();

        Assert.assertNotNull(reader);

        Assert.assertNull(reader.modelType());
        Assert.assertNull(reader.fromFile());
        Assert.assertNull(reader.fromStream());
        Assert.assertNull(reader.sheetName());

        Assert.assertEquals(2, reader.startRow());
        Assert.assertEquals(0, reader.sheetIndex());

    }

    @Test
    public void testCreateByFile() {
        Reader reader = Reader.create(new File(classPath() + "/SampleData.xlsx"));

        Assert.assertNotNull(reader);
        Assert.assertNotNull(reader.fromFile());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateByFileNotExist() {
        Reader.create(new File("abc.xlsx"));
    }

    @Test
    public void testReaderArgs() {
        Reader reader = Reader.create();

        reader.from(new File(classPath() + "/SampleData.xlsx"))
                .sheetIndex(1)
                .startRow(1)
                .sheetName("SalesOrders");

        Assert.assertNotNull(reader.fromFile());

        Assert.assertEquals(1, reader.startRow());
        Assert.assertEquals(1, reader.sheetIndex());
        Assert.assertEquals("SalesOrders", reader.sheetName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartRowError() {
        Reader.create().startRow(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSheetIndexError() {
        Reader.create().sheetIndex(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSheetNameError() {
        Reader.create().sheetName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testModelTypeError() throws ReaderException {
        Reader.create().asList(null);
    }

}
