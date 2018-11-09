/**
 * Copyright (c) 2018, biezhi 王爵 (biezhi.me@gmail.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.biezhi.excel.plus.handler;

import io.github.biezhi.excel.plus.annotation.ExcelField;
import io.github.biezhi.excel.plus.annotation.ReadField;
import io.github.biezhi.excel.plus.enums.CellDataType;
import io.github.biezhi.excel.plus.reader.Reader;
import io.github.biezhi.excel.plus.utils.ExcelUtils;
import io.github.biezhi.excel.plus.utils.Pair;
import lombok.extern.slf4j.Slf4j;
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

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Excel 2007 Parser handle
 *
 * @author biezhi
 * @date 2018/3/21
 */
@Slf4j
public class Excel2007Handler<T> extends DefaultHandler implements ExcelHandler {

    private Class<T> type;
    private Reader   reader;

    private SharedStringsTable sst;
    private String             lastContents;
    private int                sheetIndex = 0;

    private       List<String>  rows         = new ArrayList<>();
    private       CellDataType  nextDataType = CellDataType.SSTINDEX;
    private final DataFormatter formatter    = new DataFormatter();

    private List<Pair<Integer, List<String>>> data = new ArrayList<>();

    private int     curRow = 0;
    private int     curCol = 0;
    private boolean isTElement;
    private short   formatIndex;
    private String  formatString;

    private StylesTable stylesTable;

    private Map<Integer, Field> result = new HashMap<>();

    /**
     * Defines the position of the previous element and the current element,
     * used to calculate the number of empty cells, such as A6 to A8
     */
    private String preRef = null, ref = null;

    /**
     * Defines the maximum number of cells in a row of the document,
     * used to fill in the last row of cells that may be missing
     */
    private int maxColIndex = -1;

    public Excel2007Handler(Class<T> type, Reader reader) {
        this.type = type;
        this.reader = reader;
    }

    @Override
    public List<Pair<Integer, T>> parse() {
        OPCPackage pkg;
        try {
            if(reader.getExcelFile() != null){
                pkg = OPCPackage.open(reader.getExcelFile());
            } else {
                pkg = OPCPackage.open(reader.getInputStream());
            }
            XSSFReader xssfReader = new XSSFReader(pkg);
            stylesTable = xssfReader.getStylesTable();
            SharedStringsTable    sst    = xssfReader.getSharedStringsTable();
            XMLReader             parser = this.fetchSheetParser(sst);
            Iterator<InputStream> sheets = xssfReader.getSheetsData();
            while (sheets.hasNext()) {
                curRow = 0;
                InputStream sheet = sheets.next();
                if (sheetIndex == reader.getSheetIndex()) {
                    InputSource sheetSource = new InputSource(sheet);
                    parser.parse(sheetSource);
                }
                sheetIndex++;
                sheet.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return getData().stream().map(this::parse).collect(Collectors.toList());
    }

    private Pair<Integer, T> parse(Pair<Integer, List<String>> pair) {
        Pair<Integer, T> np = new Pair<>();
        np.setK(pair.getK());
        np.setV(this.convert(type, pair.getV()));
        return np;
    }

    private Map<Integer, Field> mapField() {
        if (!result.isEmpty()) {
            return result;
        }
        List<Field>         fields = ExcelUtils.getAndSaveFields(type);
        Map<Integer, Field> map    = new HashMap<>();
        for (Field field : fields) {
            ExcelField excelField = field.getAnnotation(ExcelField.class);
            if (null == excelField) {
                continue;
            }
            ReadField readField = field.getAnnotation(ReadField.class);
            if (null != readField) {
                map.put(readField.order(), field);
            } else {
                map.put(excelField.order(), field);
            }
        }
//        AtomicInteger index = new AtomicInteger();
//        map.keySet().stream().sorted(Integer::compareTo).forEach(order -> result.put(index.getAndIncrement(), map.get(order)));
        return map;
    }

    private T convert(Class<T> type, List<String> columns) {
        T item = ExcelUtils.newInstance(type);
        if (null == item) {
            return null;
        }
        int emptyCount = 0;
        for (int i = 0; i < columns.size(); i++) {
            String value = columns.get(i);
            if (null == value || value.trim().length() == 0) {
                emptyCount++;
            }
            Field field = mapField().get(i);
            ExcelUtils.writeToField(item, field, value);
        }
        if (columns.size() == emptyCount) {
            return null;
        }
        return item;
    }

    private XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
        XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        this.sst = sst;
        parser.setContentHandler(this);
        return parser;
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) {
        if ("dimension".equals(name)){
            String dimension = attributes.getValue("ref");//有效范围的坐标，如：A1:G15
            maxColIndex = convertRowIdToInt(dimension.substring(dimension.indexOf(":")+1));
        }
        if ("c".equals(name)) {
            // previous cell position
            if (preRef == null) {
                preRef = attributes.getValue("r");
            } else {
                preRef = ref;
            }
            // current cell position
            ref = attributes.getValue("r");
            // set cell type
            this.setNextDataType(attributes);
        }
        if("row".equals(name)) {
            curRow = Integer.parseInt(attributes.getValue("r"));
        }
        // when the element is t
        isTElement = "t".equals(name);
        lastContents = "";
    }

    public void setNextDataType(Attributes attributes) {
        nextDataType = CellDataType.NUMBER;
        formatIndex = -1;
        formatString = null;
        String cellType     = attributes.getValue("t");
        String cellStyleStr = attributes.getValue("s");

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

            if ("m/d/yy".equals(formatString)) {
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
     * Type processing of parsed data
     */
    public String getDataValue(String value) {
        String thisStr;
        switch (nextDataType) {
            case BOOL:
                char first = value.charAt(0);
                thisStr = first == '0' ? "FALSE" : "TRUE";
                break;
            case ERROR:
                thisStr = "\"ERROR:" + value + '"';
                break;
            case FORMULA:
                thisStr = '"' + value + '"';
                break;
            case INLINESTR:
                XSSFRichTextString rtsi = new XSSFRichTextString(value);
                thisStr = rtsi.toString();
                break;
            case SSTINDEX:
                try {
                    int                idx  = Integer.parseInt(value);
                    XSSFRichTextString rtss = new XSSFRichTextString(sst.getEntryAt(idx));
                    thisStr = rtss.toString();
                } catch (NumberFormatException ex) {
                    thisStr = value;
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
                break;
            default:
                thisStr = " ";
                break;
        }
        return thisStr;
    }

    @Override
    public void endElement(String uri, String localName, String name) {
        // t element also contains a string
        if (isTElement) {
            // Add the contents of the cell to rows, before removing the whitespace before and after the string
            String value = lastContents.trim();
            try {
                int idx = Integer.parseInt(value);
                value = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
            } catch (NumberFormatException ex) {

            }
            rows.add(curCol, value);
            curCol++;
            isTElement = false;
        } else if ("v".equals(name)) {
            // first column
            if (ref.equals(preRef)) {
                // Complementing cells that may be missing at the start of a row
                int currColumn = convertRowIdToInt(ref);
                for(int i=0;i< currColumn-1;i++){//1 means column A
                    rows.add(curCol, "");
                    curCol++;
                }
            }else{
                // Complement empty cells between cells
                int gap = convertRowIdToInt(ref) - convertRowIdToInt(preRef);
                for(int i=0;i< gap -1;i++){
                    rows.add(curCol, "");
                    curCol++;
                }
            }
            String value = this.getDataValue(lastContents.trim());
            rows.add(curCol, value);
            curCol++;
        } else {
            // If the tag name is row , this indicates that the end of the line has been reached and the method optRows() is called
            if (name.equals("row")) {
                // Complementing cells that may be missing at the end of a row
                int lastColIndex = convertRowIdToInt(preRef);
                for(int i = 0; i< maxColIndex - lastColIndex; i++){
                    rows.add(curCol, "");
                    curCol++;
                }
                if (curRow >= reader.getStartRowIndex() && rows.stream().anyMatch(ExcelUtils::isNotEmpty)) {
                    Pair<Integer, List<String>> pair = new Pair<>();
                    pair.setK(curRow);
                    pair.setV(new ArrayList<>(rows));
                    data.add(pair);
                }
                rows.clear();
                curCol = 0;
                preRef = null;
                ref = null;
            }
        }
    }

    public List<Pair<Integer, List<String>>> getData() {
        return data;
    }


    @Override
    public void characters(char[] ch, int start, int length) {
        // Get the value of the cell content
        lastContents += new String(ch, start, length);
    }

    private int convertRowIdToInt(String rowId) {
        int firstDigit = -1;
        for (int c = 0; c < rowId.length(); ++c) {
            if (Character.isDigit(rowId.charAt(c))) {
                firstDigit = c;
                break;
            }
        }
        //AB7-->AB
        //AB是列号, 7是行号
        String newRowId = rowId.substring(0, firstDigit);
        int num = 0;
        int result = 0;
        int length = newRowId.length();
        for (int i = 0; i < length; i++) {
            //先取最低位，B
            char ch = newRowId.charAt(length - i - 1);
            //B表示的十进制2，ascii码相减，以A的ascii码为基准，A表示1，B表示2
            num = (int) (ch - 'A' + 1);
            //列号转换相当于26进制数转10进制
            num *= Math.pow(26, i);
            result += num;
        }
        return result;

    }

}
