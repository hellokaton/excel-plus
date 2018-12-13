# Excel Plus

## 它是什么?

`excel-plus` 是基于 [Apache POI](https://poi.apache.org/) 框架的一款扩展封装小库，让我们在开发中更快速的完成导入导出的需求。
尽管很多人会提出 `poi` 能干这事儿为什么还要封装一层呢？

`excel-plus`很大程度上简化了代码、让使用者更轻松的
**读**、**写** Excel 文档，也不用去关心格式兼容等问题，很多时候我们在代码中会写很多的 `for` 循环，各种 `getXXXIndex`
来获取行或列让代码变的更臃肿。多个项目之间打一枪换一个地方，代码 Copy 来 Copy 去十分凌乱，
如果你也在开发中遇到类似的问题，那么 `excel-plus` 是你值得一试的工具。

## 不是什么

`excel-plus` 不是万能的，比如你想合并某几列，或者让第三行的某一列设置样式或特殊格式，
很抱歉它是做不到的，因为这让事情复杂化了，即便支持也会像原始的 POI API 一样让人痛恶。
如果真的需要，你可能需要在网络上寻找一些 `Utils` 结尾的工具类自行编写了，祝你好运 :P

> 如果你在使用过程中遇到什么问题或者建议可以发一个 [issue](https://github.com/biezhi/excel-plus/issues/new) 告诉我

## 特性

- 基于 Java 8 开发
- 简洁的 API 操作
- 注解驱动
- 高性能低损耗
- 可配置列顺序
- 支持按模板导出
- 支持过滤行数据
- 支持数据类型转换
- 支持自定义列样式
- 支持一行代码下载 Excel 文件
- 支持 Excel 2003、2007、CSV 格式

# 快速开始

## 引入依赖

加入以下 `maven` 依赖到你的 `pom.xml` 文件中，该项目使用的 `poi` 版本是 [4.0.1](https://mvnrepository.com/artifact/org.apache.poi/poi/4.0.1)，
如果你的项目已经存在，请注意删除或者排除依赖。

```xml
<dependency>
    <groupId>io.github.biezhi</groupId>
    <artifactId>excel-plus</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

> **注意**：这里的版本号请使用 `maven` 仓库较新版本，可在 Github 的 README 中看到。

## 读取和写入

下面是我们的 Java 模型类，用于存储 Excel 的行数据。

```java
public class Sample {

    @ExcelColumn(index = 0, datePattern = "M/d/yy")
    private LocalDate date;

    @ExcelColumn(index = 1)
    private String location;

    @ExcelColumn(index = 4)
    private int proportion;

    @ExcelColumn(index = 5)
    private double ss;

    @ExcelColumn(index = 6)
    private BigDecimal amount;
    
    // getter setter 省略
}
```

这是一个简单的模型类，使用 `@ExcelColumn` 来匹配 Excel 中的列关系，这个表格的数据在 [这里](https://github.com/biezhi/excel-plus/blob/v1.0/src/test/resources/SampleData.xlsx)。

测试的 Excel 文档中有很多个 `Sheet`，我们只需读取名为 `SalesOrders` 的就可以了，其他的不关心。

```java
List<Sample> samples = Reader.create(Sample.class)
                .from(new File("SampleData.xlsx"))
                .sheet("SalesOrders")
                .startRow(1)
                .asList();
```

这样就可以读取到了，非常简单！

> 这里设置 `startRow` 为 1 的原因：
> 1. 索引总是从 0 开始
> 2. 这个表格中索引为 0 的行是列信息，故从索引为 1 的开始读取

接下来试试写入一个表格到磁盘上吧 :)

```java
List<Sample> samples = new ArrayList<>();
// 这里的数据需自行准备
Writer.create()
        .headerTitle("一份简单的Excel表格")
        .withRows(samples)
        .to(new File("sample_test.xlsx"));
```

此时看看本地是否产生了一个名为 `sample_test.xlsx` 的 Excel 表格。

# 进阶使用

## 读取过滤

有时候我们需要对读取的行数据做一下过滤，这时候就可以使用 `filter` 函数来筛选出合适的数据项。

```java
List<Sample> samples = Reader.create(Sample.class)
                .from(new File(classPath() + "/SampleData.xlsx"))
                .sheet("SalesOrders")
                .startRow(1)
                .asStream()
                .filter(sample -> sample.getAmount().intValue() > 1000)
                .collect(toList());

```

## 自定义写入样式

大多数情况下我们是无需设置样式的，在 `excel-plus` 中提供了设置表头和列的样式 API。
在某些需求下可能需要设置字体大小、颜色、居中等，你可以像下面的代码这样干。
如果你对样式的操作不熟悉可以参考 POI 的列设置 [文档](https://poi.apache.org/spreadsheet/quick-guide.html#Creating+Date+Cells)。

```java
Writer.create()
        .headerTitle("一份自定义样式的Excel表格")
        .withRows(buildData())
        .titleStyle((wb, style) -> {
            Font font = wb.createFont();
            font.setFontHeightInPoints((short) 40);
            font.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
            style.setFont(font);
        })
        .headerStyle((wb, style) -> {
            Font font = wb.createFont();
            font.setFontHeightInPoints((short) 20);
            font.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
            style.setFont(font);
        })
        .cellStyle((wb, style) -> {
            Font font = wb.createFont();
            font.setFontHeightInPoints((short) 20);
            font.setColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
            style.setFont(font);
        })
        .to(new File(fileName));
```

## 浏览器下载

为了方便我们将数据库查询的数据直接输出到浏览器弹出下载，`excel-plus` 也做了一点 _手脚_ 让你一行代码就可以搞定。

```java
Writer.create()
      ...
      .to(ResponseWrapper.create(servletResponse, "xxx表格.xls"))
```

只需要将 `HttpServletResponse` 对象传入，并输入导出的文件名称，其他的都见鬼去吧。

## 模板导出

有时候我们需要导出的 Excel 表格样式比较复杂，可以事先设置好一个模板表格，数据为空，
由程序向模板中填入数据，然后导出即可，这样就满足了美观的需求。

```java
Writer.create()
        .withTemplate(classPath() + "/template.xls")
        .withRows(buildData())
        .to(new File(fileName));
```

> 需要注意的是这里的 `template.xls` 位于 `classpath` 路径下。

# API 概览

## 核心对象

- `Reader`: 用于读取一份 Excel 文档
- `Writer`: 用于写入一份 Excel 文档
- `Converter`: 数据类型转换的顶层接口，处理自定义的读取、写入规则

## Reader

- `create(Class)`：创建指定类型的 Reader
- `from(File)`：从文件中读取
- `from(InputStream)`：从 InputStream 中读取
- `startRow(int)`：设置从第几行开始读，索引从 0 开始
- `sheet(int)`：要读取的 sheet 索引，默认为 0
- `sheet(String)`：要读取的 sheet 名称，如果设置则不会根据 sheetIndex 读取
- `asStream()`：将读取结果存储在 Stream 中返回
- `asList()`：将读取结果存储在 List 中返回

## Writer

- `Writer(ExcelType)`：构造函数，写入什么类型的文件，支持 XLSX、XLS、CSV 格式
- `withRows(Collection)`：写入的数据，该方法接收一个集合
- `sheet(String)`：写入的 Sheet 名称，默认为 `Sheet0`
- `startRow(int)`：从第几行开始写入，索引从 0 开始，默认是计算出的，建议不设置
- `headerTitle(String)`：Sheet 的大标题，可选项
- `titleStyle(BiConsumer)`：自定义标题样式
- `headerStyle(BiConsumer)`：自定义列头样式
- `cellStyle(BiConsumer)`：自定义行中的单元格样式
- `withTemplate(File)`：根据模板文件创建 Excel
- `bufferSize(int)`：写入一个 XLSX 格式的文件时缓冲大小，默认为 100，建议不修改
- `withRaw()`：自定义写入行，启用该配置后不会根据集合数据写 Excel
- `createRow(int)`：`withRaw` 启用后可使用该 API，用于自定义创建 `Row` 和 `Cell`
- `to(File)`：写入 Excel 文档到文件
- `to(OutputStream)`：写入 Excel 文档到 OutputStream

## 注解使用

通过使用注解来配置如何读取、写入 Excel 文档。

<b>@ExcelColumn 注解</b>

| 选项        | 默认值               | 描述                                                               |
|-------------|----------------------|--------------------------------------------------------------------|
| `index`       | `-1` | 用于标识 Excel 中的列索引，从 0 开始，该选项适用于读取或写入 Excel    |
| `title`  | `""` | 导出 Excel 时的列名称，如：状态、姓名、手机号                        |
| `datePattern` | `""`   | 日期格式化的 `pattern`，对 `Date`、`LocalDate`、`LocalDateTime` 生效        |
| `converter` | `NullConverter.class` | 数据类型转换的类 Class，实现自 Converter 接口，实现类需提供无参构造函数 |
| `width` | `-1` | 导出为 Excel 时的列宽度，建议以 `字符数 * 256` 为基准进行设置 |

# 常见问题

在使用过程中遇到什么问题或者建议可以发一个 [issue](https://github.com/biezhi/excel-plus/issues/new)

# 版本更新

<b>v1.0-SNAPSHOT</b>

1. 修复性能问题
2. 重构部分 API
3. 简化代码
