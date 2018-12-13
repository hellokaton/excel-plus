package io.github.biezhi.excel.plus.model;

import io.github.biezhi.excel.plus.annotation.ExcelColumn;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Book {

    @ExcelColumn(title = "书名", index = 0)
    private String title;

    @ExcelColumn(title = "作者", index = 1)
    private String author;

    @ExcelColumn(title = "售价", index = 2)
    private Double price;

    @ExcelColumn(title = "出版日期", index = 3, datePattern = "yyyy年M月")
    private LocalDate publishDate;

    public Book(String title, String author, Double price, LocalDate publishDate) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.publishDate = publishDate;
    }

}
