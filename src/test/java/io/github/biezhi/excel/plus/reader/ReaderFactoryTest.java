package io.github.biezhi.excel.plus.reader;

import io.github.biezhi.excel.plus.BaseTest;
import io.github.biezhi.excel.plus.Reader;
import io.github.biezhi.excel.plus.exception.ReaderException;
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
 * @author biezhi
 * @date 2018-12-14
 */
public class ReaderFactoryTest extends BaseTest {

    @Test
    public void testReadByFile() throws ReaderException {
        Reader<Sample> reader = Reader.create(Sample.class);
        reader.from(new File(classPath() + "/SampleData.xlsx")).sheet(1).start(1);

        Stream<Sample> stream  = ReaderFactory.readByFile(reader);
        List<Sample>   samples = stream.collect(Collectors.toList());

        assertNotNull(stream);
        assertNotNull(samples);
        assertEquals(43, samples.size());

        reader = Reader.create(Sample.class);
        reader.from(new File(classPath() + "/SampleData.xls")).sheet(1).start(1);

        stream  = ReaderFactory.readByFile(reader);
        samples = stream.collect(Collectors.toList());

        assertNotNull(stream);
        assertNotNull(samples);
        assertEquals(43, samples.size());
    }

    @Test
    public void testReadByStream() throws FileNotFoundException, ReaderException {
        Reader<Sample> reader = Reader.create(Sample.class);
        reader.from(new FileInputStream(new File(classPath() + "/SampleData.xlsx"))).sheet(1).start(1);

        Stream<Sample> stream  = ReaderFactory.readByStream(reader);
        List<Sample>   samples = stream.collect(Collectors.toList());

        assertNotNull(stream);
        assertNotNull(samples);
        assertEquals(43, samples.size());
    }

}
