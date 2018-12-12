package io.github.biezhi.excel.plus;

import io.github.biezhi.excel.plus.exception.ReaderException;
import io.github.biezhi.excel.plus.exception.WriterException;
import io.github.biezhi.excel.plus.model.Book;
import org.junit.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author biezhi
 * @date 2018-12-11
 */
public class ExcelPlusTest {

    private ExcelPlus excelPlus = new ExcelPlus();

    @Test
    public void testRead() throws ReaderException {
        // 基本
        List<Book> list = excelPlus.read()
                .from(new File("book.xlsx"))
                .asList(Book.class);

        System.out.println(list);
    }

    @Test
    public void testReadByStartRow() throws ReaderException {
        // 基本
        List<Book> list = excelPlus.read()
                .from(new File("book.xlsx"))
                .startRow(10)
                .asList(Book.class);

        System.out.println(list);
    }

    @Test
    public void testReadAsFilter() throws ReaderException {
        // 基本
        List<Book> list = excelPlus.read()
                .from(new File("book.xlsx"))
                .asStream(Book.class)
                .filter(b -> b.getAuthor().contains("q"))
                .collect(Collectors.toList());

        System.out.println(list);
    }

    @Test
    public void testWrite() throws WriterException {

        int count = 100_0000;
//        int count = 10;

        List<Book> books = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            books.add(new Book("进击的智人:" + i, "河森堡:" + i, LocalDate.now()));
        }

        long start = System.currentTimeMillis();
        excelPlus.write()
                .withRows(books)
                .headerTitle("书籍列表 V1")
                .to(new File("book.xlsx"));

        System.out.println((System.currentTimeMillis() - start) / 1000 + "s");
    }

}
