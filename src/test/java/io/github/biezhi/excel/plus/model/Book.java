package io.github.biezhi.excel.plus.model;

import io.github.biezhi.excel.plus.annotation.ExcelColumn;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author biezhi
 * @date 2018-12-11
 */
@Data
public class Book {

    @ExcelColumn(title = "书名", index = 0)
    private String title;

    @ExcelColumn(title = "作者", index = 1)
    private String author;

    @ExcelColumn(title = "发布日期", index = 2, datePattern = "yyyy年MM月dd日")
    private LocalDate publishDate;

    public Book() {
    }

    public Book(String title, String author, LocalDate publishDate) {
        this.title = title;
        this.author = author;
        this.publishDate = publishDate;
    }

}
