package io.github.biezhi.excel.plus.util;

import io.github.biezhi.excel.plus.BaseTest;
import io.github.biezhi.excel.plus.Reader;
import io.github.biezhi.excel.plus.exception.ReaderException;
import io.github.biezhi.excel.plus.reader.XLSXDataFormatter;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.Assert.*;


/**
 * @author biezhi
 * @date 2018-12-14
 */
public class ExcelUtilTest extends BaseTest {

    @Test
    public void testNewInstance() {
        String s = ExcelUtil.newInstance(String.class);
        assertNotNull(s);

        XLSXDataFormatter formatter = ExcelUtil.newInstance(XLSXDataFormatter.class);
        assertNotNull(formatter);

        Reader reader = ExcelUtil.newInstance(Reader.class);
        assertNull(reader);
    }

    @Test(expected = ReaderException.class)
    public void testCreateByFile() throws ReaderException {
        Workbook workbook = ExcelUtil.create(new File(classPath() + "/SampleData.xlsx"));

        assertNotNull(workbook);

        ExcelUtil.create(new File(classPath() + "/abc.xlsx"));
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateByInputStream() throws FileNotFoundException, ReaderException {
        Workbook workbook = ExcelUtil.create(new FileInputStream(new File(classPath() + "/SampleData.xlsx")));

        assertNotNull(workbook);

        ExcelUtil.create(new FileInputStream(new File(classPath() + "/abc.xlsx")));
    }

    @Test
    public void testGetFileExtension() {
        assertEquals("txt", ExcelUtil.getFileExtension("abc.txt"));
        assertEquals("CSV", ExcelUtil.getFileExtension("abc.CSV"));
        assertEquals("xls", ExcelUtil.getFileExtension("abc.xls"));
        assertEquals("xlsx", ExcelUtil.getFileExtension("abc.xlsx"));
        assertEquals("", ExcelUtil.getFileExtension("abcd"));
    }

    @Test(expected = FileNotFoundException.class)
    public void testIsXLSX() throws FileNotFoundException {
        assertTrue(ExcelUtil.isXLSX(new File(classPath() + "/SampleData.xlsx")));
        assertFalse(ExcelUtil.isXLSX(new File(classPath() + "/hello.xls")));
        assertFalse(ExcelUtil.isXLSX(new File(classPath() + "/SampleData.xls")));
        assertFalse(ExcelUtil.isXLSX((File) null));

        assertTrue(ExcelUtil.isXLSX(new FileInputStream(new File(classPath() + "/SampleData.xlsx"))));
        assertFalse(ExcelUtil.isXLSX(new FileInputStream(new File(classPath() + "/SampleData.xls"))));
        assertFalse(ExcelUtil.isXLSX((InputStream) null));

        ExcelUtil.isXLSX(new FileInputStream(new File(classPath() + "/hello.xls")));
    }

    @Test(expected = FileNotFoundException.class)
    public void testIsXLS() throws FileNotFoundException {
        assertFalse(ExcelUtil.isXLS(new File(classPath() + "/SampleData.xlsx")));
        assertFalse(ExcelUtil.isXLS(new File(classPath() + "/hello.xls")));
        assertFalse(ExcelUtil.isXLS((File) null));
        assertTrue(ExcelUtil.isXLS(new File(classPath() + "/SampleData.xls")));

        assertTrue(ExcelUtil.isXLS(new FileInputStream(new File(classPath() + "/SampleData.xls"))));
        assertFalse(ExcelUtil.isXLS(new FileInputStream(new File(classPath() + "/SampleData.xlsx"))));
        assertFalse(ExcelUtil.isXLS((InputStream) null));

        ExcelUtil.isXLS(new FileInputStream(new File(classPath() + "/hello.xlsx")));
    }

    @Test
    public void testIsCSV() {
        assertTrue(ExcelUtil.isCSV(new File(classPath() + "/book.csv")));

        assertFalse(ExcelUtil.isCSV(new File(classPath() + "/book.abc")));
        assertFalse(ExcelUtil.isCSV(new File(classPath() + "/hello.csv")));
        assertFalse(ExcelUtil.isCSV(null));
    }

}
