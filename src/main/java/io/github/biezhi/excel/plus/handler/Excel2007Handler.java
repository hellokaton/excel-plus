package io.github.biezhi.excel.plus.handler;

import io.github.biezhi.excel.plus.exception.ParseException;
import io.github.biezhi.excel.plus.reader.ReaderParam;
import io.github.biezhi.excel.plus.utils.ExcelUtils;
import io.github.biezhi.excel.plus.utils.Pair;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author biezhi
 * @date 2018/3/21
 */
@Slf4j
public class Excel2007Handler<T> implements ExcelHandler {

    /**
     * 表格默认处理器
     */
    private ISheetContentHandler contentHandler = new DefaultSheetHandler();

    /**
     * 读取数据
     */
    private List<Pair<Integer, String[]>> data = new ArrayList<>();

    private Class<T>    type;
    private ReaderParam readerParam;

    public Excel2007Handler(Class<T> type, ReaderParam readerParam) {
        this.type = type;
        this.readerParam = readerParam;
    }

    /**
     * 转换表格
     *
     * @return 表格转换器
     */
    @Override
    public List<Pair<Integer, T>> parse() throws ParseException {
        // 每次转换前都清空数据
        data.clear();
        OPCPackage pkg = null;
        try {
            InputStream inputStream   = new FileInputStream(readerParam.getExcelFile());
            int         sheetIndex    = readerParam.getSheetIndex() + 1;
            int         columnsLength = ExcelUtils.getMaxOrder(type);

            // 打开表格文件输入流
            pkg = OPCPackage.open(inputStream);

            // 创建表阅读器
            XSSFReader reader;
            try {
                reader = new XSSFReader(pkg);
            } catch (OpenXML4JException e) {
                log.error("读取表格出错");
                throw new ParseException(e.fillInStackTrace());
            }

            // 转换指定单元表
            InputStream shellStream = reader.getSheet("rId" + sheetIndex);
            try {
                InputSource                sheetSource = new InputSource(shellStream);
                StylesTable                styles      = reader.getStylesTable();
                ReadOnlySharedStringsTable strings     = new ReadOnlySharedStringsTable(pkg);
                getContentHandler().init(data, columnsLength);// 设置读取出的数据
                // 获取转换器
                XMLReader parser = getSheetParser(styles, strings);
                parser.parse(sheetSource);
            } catch (SAXException e) {
                log.error("读取表格出错");
                throw new ParseException(e.fillInStackTrace());
            } finally {
                shellStream.close();
            }
        } catch (Exception e) {
            if (null != pkg) {
                try {
                    pkg.close();
                } catch (Exception e2) {
                }
            }
            throw new ParseException(e);
        } finally {
            if (null != pkg) {
                try {
                    pkg.close();
                } catch (Exception e) {
                }
            }
        }
        return getData().stream().map(this::parse).collect(Collectors.toList());
    }

    private Pair<Integer, T> parse(Pair<Integer, String[]> pair) {
        Pair<Integer, T> np = new Pair<>();
        np.setK(pair.getK());
        np.setV(convert(type, pair.getV()));
        return np;
    }

    private T convert(Class<T> type, String[] columns) {
        T item = ExcelUtils.newInstance(type);
        if (null == item) {
            return null;
        }
        for (int i = 0; i < columns.length; i++) {
            String value = columns[i];
            ExcelUtils.writeToField(item, i, value);
        }
        return item;
    }

    /**
     * 获取表格读取数据,获取数据前，需要先转换数据<br>
     * 此方法不会获取第一行数据
     *
     * @return 表格读取数据
     */
    public List<Pair<Integer, String[]>> getData() {
        return data;

    }

    /**
     * 获取读取表格的转换器
     *
     * @return 读取表格的转换器
     * @throws SAXException SAX错误
     */
    protected XMLReader getSheetParser(StylesTable styles, ReadOnlySharedStringsTable strings) throws SAXException {
        XMLReader parser = XMLReaderFactory.createXMLReader();
        parser.setContentHandler(new XSSFSheetXMLHandler(styles, strings, getContentHandler(), false));
        return parser;
    }

    public ISheetContentHandler getContentHandler() {
        return contentHandler;
    }

    public void setContentHandler(ISheetContentHandler contentHandler) {
        this.contentHandler = contentHandler;
    }

    public interface ISheetContentHandler extends XSSFSheetXMLHandler.SheetContentsHandler {
        /**
         * 设置转换后的数据集，用于存放转换结果
         *
         * @param datas        转换结果
         * @param columsLength 读取多少列
         */
        void init(List<Pair<Integer, String[]>> datas, int columsLength);
    }

    /**
     * 默认表格解析handder
     *
     * @version 1.0
     * @date 2017年6月26日 下午2:30:50
     */
    class DefaultSheetHandler implements ISheetContentHandler {
        /**
         * 读取数据
         */
        private List<Pair<Integer, String[]>> data;
        private int                           columnsLength;
        // 读取行信息
        private String[]                      readRow;

        @Override
        public void init(List<Pair<Integer, String[]>> datas, int columsLength) {
            this.data = datas;
            this.columnsLength = columsLength;
        }

        @Override
        public void startRow(int rowNum) {
            readRow = new String[columnsLength];
        }

        @Override
        public void endRow(int rowNum) {
            if (rowNum >= readerParam.getStartRowIndex()) {
                long notEmptyCols = Arrays.stream(readRow).filter(s -> null != s && !s.trim().isEmpty()).count();
                if (notEmptyCols > 0) {
                    data.add(new Pair<>(rowNum, readRow.clone()));
                }
            }
            readRow = null;
        }

        @Override
        public void cell(String cellReference, String formattedValue, XSSFComment comment) {
            int index = getCellIndex(cellReference);//转换A1,B1,C1等表格位置为真实索引位置
            if (index < columnsLength) {
                readRow[index] = formattedValue;
            }
        }

        @Override
        public void headerFooter(String text, boolean isHeader, String tagName) {
        }

        /**
         * 转换表格引用为列编号
         *
         * @param cellReference 列引用
         * @return 表格列位置，从0开始算
         */
        public int getCellIndex(String cellReference) {
            String ref    = cellReference.replaceAll("\\d+", "");
            int    num    = 0;
            int    result = 0;
            for (int i = 0; i < ref.length(); i++) {
                char ch = cellReference.charAt(ref.length() - i - 1);
                num = ch - 'A' + 1;
                num *= Math.pow(26, i);
                result += num;
            }
            return result - 1;
        }
    }

}
