# excel-plus

Easier to read and generate an excel file, supports 2003 and 2007.

[![](https://img.shields.io/travis/biezhi/excel-plus.svg)](https://travis-ci.org/biezhi/excel-plus)
[![](https://img.shields.io/maven-central/v/io.github.biezhi/excel-plus.svg)](https://mvnrepository.com/artifact/io.github.biezhi/excel-plus)
[![](https://img.shields.io/badge/license-Apache2-FF0080.svg)](https://github.com/biezhi/excel-plus/blob/master/LICENSE)

<a href="https://biezhi.github.io/excel-plus/" target="_blank">中文文档</a>

# Feature

- Easy to use
- Annotation driven
- Based java 8
- Support `xls`、`xlsx`、`csv`
- Support export by template
- Support custom column style
- High performance, it takes only 15 seconds to write `1,000,000` lines

# Usage

snapshot version

```xml
<dependency>
    <groupId>io.github.biezhi</groupId>
    <artifactId>excel-plus</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

**Create ExcelPlus**

```java
ExcelPlus excelPlus = new ExcelPlus();
```

**Read excel as List**

```java
List<Member> members = 
        excelPlus.read()
                 .from(new File("members.xlsx"))
                 .asList(Member.class);
```

**Write excel as file**

```java
excelPlus.write()
         .withRows(books)
         .headerTitle("书籍列表 V1")
         .to(new File("book.xlsx"));
```

**Browser download**

```java
excelPlus.write()
         .withRows(orders)
         .to(ResponseWrapper.create(HttpServletResponse, "order-list.xls"));
```

# Examples

See [here](https://github.com/biezhi/excel-plus/tree/master/src/test/java/io/github/biezhi/excel/plus/Examples.java)

# Advanced

**Custom export style**

# Thanks

- [ydq](https://github.com/ydq)

# License

[Apache2](https://github.com/biezhi/excel-plus/blob/master/LICENSE)