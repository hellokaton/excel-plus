package io.github.biezhi.excel.plus;

import java.util.List;

/**
 * 名称: IRowReader.java<br>
 * 描述: <br>
 * 类型: JAVA<br>
 * 最近修改时间:2016年7月5日 上午10:28:06<br>
 * 
 * @since 2016年7月5日
 * @author “”
 */
public interface IExcelRowReader {
    /**
     * 业务逻辑实现方法
     * 
     * @param sheetIndex
     * @param curRow
     * @param rowlist
     */
    void getRows(int sheetIndex, int curRow, List<String> rowlist);
}