# excel-plus

Easier to read and generate an excel file, supports `XLSX`、`XLS`、`CSV`.

[![EO principles respected here](http://www.elegantobjects.org/badge.svg)](http://www.elegantobjects.org)
[![DevOps By Rultor.com](http://www.rultor.com/b/hellokaton/excel-plus)](http://www.rultor.com/p/hellokaton/excel-plus)


[![](https://img.shields.io/travis/hellokaton/excel-plus.svg)](https://travis-ci.org/hellokaton/excel-plus)
[![Javadocs](http://javadoc.io/badge/io.github.biezhi/excel-plus.svg)](http://javadoc.io/doc/io.github.biezhi/excel-plus)
[![](https://img.shields.io/maven-central/v/io.github.biezhi/excel-plus.svg)](https://search.maven.org/search?q=excel-plus)
[![](https://img.shields.io/badge/license-Apache2-FF0080.svg)](https://github.com/hellokaton/excel-plus/blob/master/LICENSE)

[![codecov](https://codecov.io/gh/hellokaton/excel-plus/branch/master/graph/badge.svg)](https://codecov.io/gh/hellokaton/excel-plus)
[![SonarQube](https://img.shields.io/badge/sonar-ok-green.svg)](https://sonarcloud.io/dashboard/index/io.github.biezhi:excel-plus)

<a href="https://hellokaton.github.io/excel-plus/#/" target="_blank">中文文档</a>

# Feature

- Easy to use
- Annotation driven
- Based java 8
- Support `xls`、`xlsx`、`csv`
- Support export by template
- Support custom column style
- High performance, only 30 seconds to read or write `1,000,000` lines

# Usage

**How to use**. Latest version here

```xml
<dependency>
    <groupId>io.github.biezhi</groupId>
    <artifactId>excel-plus</artifactId>
    <version>1.0.8</version>
</dependency>
```

snapshot version

```xml
<repositories>
    <repository>
        <id>snapshots-repo</id>
        <url>https://oss.sonatype.org/content/repositories/snapshots</url>
        <releases>
            <enabled>false</enabled>
        </releases>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>io.github.biezhi</groupId>
        <artifactId>excel-plus</artifactId>
        <version>1.0.8-SNAPSHOT</version>
    </dependency>
</dependencies>
```

**Read excel as List**

![](https://i.loli.net/2018/12/14/5c1290880509b.png)

```java
public class Member {

    @ExcelColumn(title = "卡号", index = 0)
    private Long cardNo;

    @ExcelColumn(title = "卡类型", index = 1)
    private String cardType;

    @ExcelColumn(title = "领用状态", index = 2)
    private String requisitionStatus;

    @ExcelColumn(title = "状态", index = 3)
    private String status;

    @ExcelColumn(title = "余额(元)", index = 6)
    private BigDecimal amount;

    @ExcelColumn(title = "会员", index = 7)
    private String nickname;

    @ExcelColumn(title = "性别", index = 9)
    private String gender;

    @ExcelColumn(title = "手机", index = 10)
    private String mobile;

    @ExcelColumn(title = "发卡日期", index = 14, datePattern = "M/d/yyyy HH:mm")
    private Date sendCardTime;
    
    // getter setter 省略
}
```

```java
List<Member> members = Reader.create(Member.class)
                 .from(new File("members.xlsx"))
                 .start(1)
                 .asList();
```

**Write excel as file**

```java
public class Book {

    @ExcelColumn(title = "书名", index = 0)
    private String title;
    
    @ExcelColumn(title = "作者", index = 1)
    private String author;

    @ExcelColumn(title = "售价", index = 2)
    private Double price;

    @ExcelColumn(title = "出版日期", index = 3, datePattern = "yyyy年M月")
    private LocalDate publishDate;
    
    // getter setter 省略
}
```

```java
Writer.create()
         .withRows(books)
         .headerTitle("书籍列表 V1")
         .to(new File("book.xlsx"));
```

![](https://i.loli.net/2018/12/14/5c1292b23b66f.png)

Code See [here](https://github.com/hellokaton/excel-plus/blob/master/src/test/java/io/github/biezhi/excel/plus/examples/WriterExample.java#L145)

**Browser download**

```java
Writer.create()
         .withRows(orders)
         .to(ResponseWrapper.create(HttpServletResponse, "order-list.xls"));
```

# Examples

See [here](https://github.com/hellokaton/excel-plus/blob/master/src/test/java/io/github/biezhi/excel/plus/examples)

# Thanks

- [ydq](https://github.com/ydq)

# License

[Apache2](https://github.com/hellokaton/excel-plus/blob/master/LICENSE)
