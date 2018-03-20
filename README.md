# excel-plus

Easier to read and generate an excel file, supports 2003 and 2007.

[![](https://img.shields.io/travis/biezhi/excel-plus.svg)](https://travis-ci.org/biezhi/excel-plus)
[![](https://img.shields.io/maven-central/v/io.github.biezhi/excel-plus.svg)](https://mvnrepository.com/artifact/io.github.biezhi/excel-plus)
[![@biezhi on zhihu](https://img.shields.io/badge/zhihu-%40biezhi-red.svg)](https://www.zhihu.com/people/biezhi)
[![](https://img.shields.io/badge/license-Apache2-FF0080.svg)](https://github.com/biezhi/excel-plus/blob/master/LICENSE)
[![](https://img.shields.io/github/followers/biezhi.svg?style=social&label=Follow%20Me)](https://github.com/biezhi)

<a href="https://biezhi.github.io/excel-plus/" target="_blank">中文文档</a>

# Feature

- Easy to use
- Annotation driven
- Based java 8
- Support `xls`、`xlsx`、`csv`
- Support export by template
- Support custom column style

# Usage

Add maven dependency

```xml
<dependency>
    <groupId>io.github.biezhi</groupId>
    <artifactId>excel-plus</artifactId>
    <version>0.0.3</version>
</dependency>
```

**Export as file**

```java
ExcelPlus excelPlus = new ExcelPlus();
List<Order> orders = queryData();

excelPlus.exportor(orders).writeAsFile(new File("order-list.xls"));
```

**Browser download**

```java
ExcelPlus excelPlus = new ExcelPlus();
List<Order> orders = queryData();

excelPlus.exportor(orders)
         .writeAsResponse(ResponseWrapper.create(HttpServletResponse, "order-list.xls"));
```

**Read as file**

```java
ExcelPlus excelPlus = new ExcelPlus();
List<Order>  orders = excelPlus.read(new File("/Users/biezhi/Desktop/order.xls"), Order.class).asList();
```

**Read as stream**

```java
ExcelPlus excelPlus = new ExcelPlus();
List<Order>  orders = excelPlus.read(inputStream, Order.class).asList();
```

# Examples

See [here](https://github.com/biezhi/excel-plus/tree/master/src/test/java/io/github/biezhi/excel/plus/Examples.java)

# Advanced

**Custom export style**

# License

[Apache2](https://github.com/biezhi/excel-plus/blob/master/LICENSE)