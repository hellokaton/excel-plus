package io.github.biezhi.excel.plus.util;

import io.github.biezhi.excel.plus.BaseTest;
import io.github.biezhi.excel.plus.Reader;
import io.github.biezhi.excel.plus.exception.ReaderException;
import io.github.biezhi.excel.plus.reader.XLSXDataFormatter;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void testCreateByFile() {
        Workbook workbook = ExcelUtil.create(new File(classPath() + "/SampleData.xlsx"));

        assertNotNull(workbook);

        Executable e = () -> ExcelUtil.create(new File(classPath() + "/abc.xlsx"));
        assertThrows(ReaderException.class, e);
    }

    @Test
    public void testCreateByInputStream() throws FileNotFoundException {
        Workbook workbook = ExcelUtil.create(new FileInputStream(new File(classPath() + "/SampleData.xlsx")));

        assertNotNull(workbook);

        Executable e = () -> ExcelUtil.create(new FileInputStream(new File(classPath() + "/abc.xlsx")));
        assertThrows(FileNotFoundException.class, e);
    }

    @Test
    public void testGetFileExtension() {
        assertEquals("txt", ExcelUtil.getFileExtension("abc.txt"));
        assertEquals("CSV", ExcelUtil.getFileExtension("abc.CSV"));
        assertEquals("xls", ExcelUtil.getFileExtension("abc.xls"));
        assertEquals("xlsx", ExcelUtil.getFileExtension("abc.xlsx"));
        assertEquals("", ExcelUtil.getFileExtension("abcd"));
    }

    @Test
    public void testIsXLSX() throws FileNotFoundException {
        assertTrue(ExcelUtil.isXLSX(new File(classPath() + "/SampleData.xlsx")));
        assertFalse(ExcelUtil.isXLSX(new File(classPath() + "/hello.xls")));
        assertFalse(ExcelUtil.isXLSX(new File(classPath() + "/SampleData.xls")));
        assertFalse(ExcelUtil.isXLSX((File) null));

        assertTrue(ExcelUtil.isXLSX(new FileInputStream(new File(classPath() + "/SampleData.xlsx"))));
        assertFalse(ExcelUtil.isXLSX(new FileInputStream(new File(classPath() + "/SampleData.xls"))));
        assertFalse(ExcelUtil.isXLSX((InputStream) null));

        Executable e = () -> ExcelUtil.isXLSX(new FileInputStream(new File(classPath() + "/hello.xls")));
        assertThrows(FileNotFoundException.class, e);
    }

    @Test
    public void testIsXLS() throws FileNotFoundException {
        assertFalse(ExcelUtil.isXLS(new File(classPath() + "/SampleData.xlsx")));
        assertFalse(ExcelUtil.isXLS(new File(classPath() + "/hello.xls")));
        assertFalse(ExcelUtil.isXLS((File) null));
        assertTrue(ExcelUtil.isXLS(new File(classPath() + "/SampleData.xls")));

        assertTrue(ExcelUtil.isXLS(new FileInputStream(new File(classPath() + "/SampleData.xls"))));
        assertFalse(ExcelUtil.isXLS(new FileInputStream(new File(classPath() + "/SampleData.xlsx"))));
        assertFalse(ExcelUtil.isXLS((InputStream) null));

        Executable e = () -> ExcelUtil.isXLS(new FileInputStream(new File(classPath() + "/hello.xlsx")));
        assertThrows(FileNotFoundException.class, e);
    }

    @Test
    public void testIsCSV() {
        assertTrue(ExcelUtil.isCSV(new File(classPath() + "/book.csv")));

        assertFalse(ExcelUtil.isCSV(new File(classPath() + "/book.abc")));
        assertFalse(ExcelUtil.isCSV(new File(classPath() + "/hello.csv")));
        assertFalse(ExcelUtil.isCSV(null));
    }

}
