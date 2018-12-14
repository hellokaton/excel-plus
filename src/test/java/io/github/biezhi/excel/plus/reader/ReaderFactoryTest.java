package io.github.biezhi.excel.plus.reader;

import io.github.biezhi.excel.plus.BaseTest;
import io.github.biezhi.excel.plus.Reader;
import io.github.biezhi.excel.plus.exception.ReaderException;
import io.github.biezhi.excel.plus.model.Book;
import io.github.biezhi.excel.plus.model.Sample;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * ReaderFactoryTest
 *
 * @author biezhi
 * @date 2018-12-14
 */
public class ReaderFactoryTest extends BaseTest {

    @Test
    public void testReadByFileXLSX() throws ReaderException {
        Reader<Sample> reader = Reader.create(Sample.class);
        reader.from(new File(classPath() + "/SampleData.xlsx")).sheet(1).start(1);

        Stream<Sample> stream  = ReaderFactory.readByFile(reader);
        List<Sample>   samples = stream.collect(Collectors.toList());

        assertNotNull(stream);
        assertNotNull(samples);
        assertEquals(43, samples.size());
    }

    @Test
    public void testReadByFileXLS() throws ReaderException {
        Reader<Sample> reader = Reader.create(Sample.class);
        reader.from(new File(classPath() + "/SampleData.xls")).sheet(1).start(1);

        Stream<Sample> stream  = ReaderFactory.readByFile(reader);
        List<Sample>   samples = stream.collect(Collectors.toList());

        assertNotNull(stream);
        assertNotNull(samples);
        assertEquals(43, samples.size());
    }

    @Test
    public void testReadByFileCSV() throws ReaderException {
        Reader<Book> reader = Reader.create(Book.class);
        reader.from(new File(classPath() + "/book.csv")).start(0);

        Stream<Book> stream  = ReaderFactory.readByFile(reader);
        List<Book>   samples = stream.collect(Collectors.toList());

        assertNotNull(stream);
        assertNotNull(samples);
        assertEquals(5, samples.size());
    }

    @Test
    public void testReadByStreamXLSX() throws FileNotFoundException, ReaderException {
        Reader<Sample> reader = Reader.create(Sample.class);
        reader.from(new FileInputStream(new File(classPath() + "/SampleData.xlsx"))).sheet(1).start(1);

        Stream<Sample> stream  = ReaderFactory.readByStream(reader);
        List<Sample>   samples = stream.collect(Collectors.toList());

        assertNotNull(stream);
        assertNotNull(samples);
        assertEquals(43, samples.size());
    }

    @Test
    public void testReadByStreamXLS() throws FileNotFoundException, ReaderException {
        Reader<Sample> reader = Reader.create(Sample.class);
        reader.from(new FileInputStream(new File(classPath() + "/SampleData.xls"))).sheet(1).start(1);

        Stream<Sample> stream  = ReaderFactory.readByStream(reader);
        List<Sample>   samples = stream.collect(Collectors.toList());

        assertNotNull(stream);
        assertNotNull(samples);
        assertEquals(43, samples.size());
    }

    @Test
    public void testReadByStreamCSV() throws FileNotFoundException, ReaderException {
        Reader<Book> reader = Reader.create(Book.class);
        reader.from(new FileInputStream(new File(classPath() + "/book.csv"))).start(0);

        Stream<Book> stream  = ReaderFactory.readByStream(reader);
        List<Book>   samples = stream.collect(Collectors.toList());

        assertNotNull(stream);
        assertNotNull(samples);
        assertEquals(5, samples.size());
    }

}
