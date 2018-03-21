package io.github.biezhi.excel.plus;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 名称: ExcelXlsxReader.java<br>
 * 描述: <br>
 * 类型: JAVA<br>
 * 最近修改时间:2016年7月5日 上午10:00:52<br>
 *
 * @author “”
 * @since 2016年7月5日
 */
public class ExcelXlsxReader extends DefaultHandler {

    private IExcelRowReader rowReader;

    public void setRowReader(IExcelRowReader rowReader) {
        this.rowReader = rowReader;
    }

    /**
     * 共享字符串表
     */
    private SharedStringsTable sst;

    /**
     * 上一次的内容
     */
    private String lastContents;

    /**
     * 字符串标识
     */
    private boolean nextIsString;

    /**
     * 工作表索引
     */
    private int sheetIndex = -1;

    /**
     * 行集合
     */
    private List<String> rowlist = new ArrayList<String>();

    /**
     * 当前行
     */
    private int curRow = 0;

    /**
     * 当前列
     */
    private int curCol = 0;

    /**
     * T元素标识
     */
    private boolean isTElement;

    /**
     * 异常信息，如果为空则表示没有异常
     */
    private String exceptionMessage;

    /**
     * 单元格数据类型，默认为字符串类型
     */
    private CellDataType nextDataType = CellDataType.SSTINDEX;

    private final DataFormatter formatter = new DataFormatter();

    private short formatIndex;

    private String formatString;

    // 定义前一个元素和当前元素的位置，用来计算其中空的单元格数量，如A6和A8等
    private String preRef = null, ref = null;

    // 定义该文档一行最大的单元格数，用来补全一行最后可能缺失的单元格
    private String maxRef = null;

    /**
     * 单元格
     */
    private StylesTable stylesTable;

    /**
     * 遍历工作簿中所有的电子表格
     *
     * @param filename
     * @throws IOException
     * @throws OpenXML4JException
     * @throws SAXException
     * @throws Exception
     */
    public void process(String filename) throws IOException, OpenXML4JException, SAXException {
        OPCPackage pkg        = OPCPackage.open(filename);
        XSSFReader xssfReader = new XSSFReader(pkg);
        stylesTable = xssfReader.getStylesTable();
        SharedStringsTable    sst    = xssfReader.getSharedStringsTable();
        XMLReader             parser = this.fetchSheetParser(sst);
        Iterator<InputStream> sheets = xssfReader.getSheetsData();
        while (sheets.hasNext()) {
            curRow = 0;
            sheetIndex++;
            InputStream sheet       = sheets.next();
            InputSource sheetSource = new InputSource(sheet);
            parser.parse(sheetSource);
            sheet.close();
        }
    }

    public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
        XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        this.sst = sst;
        parser.setContentHandler(this);
        return parser;
    }

    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        // c => 单元格
        if ("c".equals(name)) {
            // 前一个单元格的位置
            if (preRef == null) {
                preRef = attributes.getValue("r");
            } else {
                preRef = ref;
            }
            // 当前单元格的位置
            ref = attributes.getValue("r");
            // 设定单元格类型
            this.setNextDataType(attributes);
            // Figure out if the value is an index in the SST
            String cellType = attributes.getValue("t");
            if (cellType != null && cellType.equals("s")) {
                nextIsString = true;
            } else {
                nextIsString = false;
            }
        }

        // 当元素为t时
        if ("t".equals(name)) {
            isTElement = true;
        } else {
            isTElement = false;
        }

        // 置空
        lastContents = "";
    }

    /**
     * 单元格中的数据可能的数据类型
     */
    enum CellDataType {
        BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, NUMBER, DATE, NULL
    }

    /**
     * 处理数据类型
     *
     * @param attributes
     */
    public void setNextDataType(Attributes attributes) {
        nextDataType = CellDataType.NUMBER;
        formatIndex = -1;
        formatString = null;
        String cellType     = attributes.getValue("t");
        String cellStyleStr = attributes.getValue("s");
        String columData    = attributes.getValue("r");

        if ("b".equals(cellType)) {
            nextDataType = CellDataType.BOOL;
        } else if ("e".equals(cellType)) {
            nextDataType = CellDataType.ERROR;
        } else if ("inlineStr".equals(cellType)) {
            nextDataType = CellDataType.INLINESTR;
        } else if ("s".equals(cellType)) {
            nextDataType = CellDataType.SSTINDEX;
        } else if ("str".equals(cellType)) {
            nextDataType = CellDataType.FORMULA;
        }

        if (cellStyleStr != null) {
            int           styleIndex = Integer.parseInt(cellStyleStr);
            XSSFCellStyle style      = stylesTable.getStyleAt(styleIndex);
            formatIndex = style.getDataFormat();
            formatString = style.getDataFormatString();

            if ("m/d/yy" == formatString) {
                nextDataType = CellDataType.DATE;
                formatString = "yyyy-MM-dd hh:mm:ss.SSS";
            }

            if (formatString == null) {
                nextDataType = CellDataType.NULL;
                formatString = BuiltinFormats.getBuiltinFormat(formatIndex);
            }
        }
    }

    /**
     * 对解析出来的数据进行类型处理
     *
     * @param value   单元格的值（这时候是一串数字）
     * @param thisStr 一个空字符串
     * @return
     */
    @SuppressWarnings("deprecation")
    public String getDataValue(String value, String thisStr) {
        switch (nextDataType) {
            // 这几个的顺序不能随便交换，交换了很可能会导致数据错误
            case BOOL:
                char first = value.charAt(0);
                thisStr = first == '0' ? "FALSE" : "TRUE";
                break;
            case ERROR:
                thisStr = "\"ERROR:" + value.toString() + '"';
                break;
            case FORMULA:
                thisStr = '"' + value.toString() + '"';
                break;
            case INLINESTR:
                XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());

                thisStr = rtsi.toString();
                rtsi = null;
                break;
            case SSTINDEX:
                String sstIndex = value.toString();
                try {
                    int                idx  = Integer.parseInt(sstIndex);
                    XSSFRichTextString rtss = new XSSFRichTextString(sst.getEntryAt(idx));
                    thisStr = rtss.toString();
                    rtss = null;
                } catch (NumberFormatException ex) {
                    thisStr = value.toString();
                }
                break;
            case NUMBER:
                if (formatString != null) {
                    thisStr = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex, formatString).trim();
                } else {
                    thisStr = value;
                }

                thisStr = thisStr.replace("_", "").trim();
                break;
            case DATE:
                thisStr = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex, formatString);

                // 对日期字符串作特殊处理
                thisStr = thisStr.replace(" ", "T");
                break;
            default:
                thisStr = " ";

                break;
        }

        return thisStr;
    }

    private boolean isNumber(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        // 根据SST的索引值的到单元格的真正要存储的字符串
        // 这时characters()方法可能会被调用多次
        if (nextIsString && (null != lastContents && !lastContents.isEmpty()) && isNumber(lastContents)) {
            int idx = Integer.parseInt(lastContents);
            lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
        }

        // t元素也包含字符串
        if (isTElement) {
            // 将单元格内容加入rowlist中，在这之前先去掉字符串前后的空白符
            String value = lastContents.trim();
            rowlist.add(curCol, value);
            curCol++;
            isTElement = false;
        } else if ("v".equals(name)) {
            // v => 单元格的值，如果单元格是字符串则v标签的值为该字符串在SST中的索引
            String value = this.getDataValue(lastContents.trim(), "");
            // 补全单元格之间的空单元格
            if (!ref.equals(preRef)) {
                int len = countNullCell(ref, preRef);
                for (int i = 0; i < len; i++) {
                    rowlist.add(curCol, "");
                    curCol++;
                }
            }
            rowlist.add(curCol, value);
            curCol++;
        } else {
            // 如果标签名称为 row ，这说明已到行尾，调用 optRows() 方法
            if (name.equals("row")) {
                // 默认第一行为表头，以该行单元格数目为最大数目
                if (curRow == 0) {
                    maxRef = ref;
                }
                // 补全一行尾部可能缺失的单元格
                if (maxRef != null) {
                    int len = countNullCell(maxRef, ref);
                    for (int i = 0; i <= len; i++) {
                        rowlist.add(curCol, "");
                        curCol++;
                    }
                }
                rowReader.getRows(sheetIndex, curRow, rowlist);

                rowlist.clear();
                curRow++;
                curCol = 0;
                preRef = null;
                ref = null;
            }
        }
    }

    /**
     * 计算两个单元格之间的单元格数目(同一行)
     *
     * @param ref
     * @param preRef
     * @return
     */
    public int countNullCell(String ref, String preRef) {
        // excel2007最大行数是1048576，最大列数是16384，最后一列列名是XFD
        String xfd   = ref.replaceAll("\\d+", "");
        String xfd_1 = preRef.replaceAll("\\d+", "");

        xfd = fillChar(xfd, 3, '@', true);
        xfd_1 = fillChar(xfd_1, 3, '@', true);

        char[] letter   = xfd.toCharArray();
        char[] letter_1 = xfd_1.toCharArray();
        int    res      = (letter[0] - letter_1[0]) * 26 * 26 + (letter[1] - letter_1[1]) * 26 + (letter[2] - letter_1[2]);
        return res - 1;
    }

    /**
     * 字符串的填充
     *
     * @param str
     * @param len
     * @param let
     * @param isPre
     * @return
     */
    String fillChar(String str, int len, char let, boolean isPre) {
        int len_1 = str.length();
        if (len_1 < len) {
            if (isPre) {
                for (int i = 0; i < (len - len_1); i++) {
                    str = let + str;
                }
            } else {
                for (int i = 0; i < (len - len_1); i++) {
                    str = str + let;
                }
            }
        }
        return str;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // 得到单元格内容的值
        lastContents += new String(ch, start, length);
    }

    /**
     * @return the exceptionMessage
     */
    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public static void main(String[] args) {
        IExcelRowReader reader = new ExcelRowReader();
        try {
            ExcelXlsxReader exceXlsx = new ExcelXlsxReader();
            exceXlsx.setRowReader(reader);
            exceXlsx.process("/Users/biezhi/workspace/projects/java/excel-plus/src/test/resources/卡密列表.xlsx");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}