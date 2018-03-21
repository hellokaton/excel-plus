package io.github.biezhi.excel.plus;

import io.github.biezhi.excel.plus.enums.ParseType;
import io.github.biezhi.excel.plus.exception.ExcelException;
import io.github.biezhi.excel.plus.exception.ParseException;
import io.github.biezhi.excel.plus.handler.DefaultExcelHandler;
import io.github.biezhi.excel.plus.handler.Excel2007Handler;
import io.github.biezhi.excel.plus.reader.ReaderParam;
import io.github.biezhi.excel.plus.reader.ReaderResult;
import io.github.biezhi.excel.plus.utils.Pair;
import io.github.biezhi.excel.plus.writer.Exporter;
import io.github.biezhi.excel.plus.writer.FileExcelWriter;
import io.github.biezhi.excel.plus.writer.ResponseExcelWriter;
import io.github.biezhi.excel.plus.writer.ResponseWrapper;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * Excel Plus
 * <p>
 * Provides methods to read and write Excel documents without any implementation.
 *
 * @author biezhi
 * @date 2018/2/4
 */
public class ExcelPlus {

    /**
     * Export Excel configuration parameters
     */
    private Exporter exporter;

    /**
     * Sets the data for exporting a collection container.
     *
     * @param data collection data
     * @param <T>  Java Type
     * @return self, aspect follow-up
     */
    public <T> ExcelPlus export(Collection<T> data) {
        return this.export(Exporter.create(data));
    }

    /**
     * Custom a Exporter object is used to export Excel.
     *
     * @param exporter exporter
     * @param <T>      Java Type
     * @return self, aspect follow-up
     */
    public <T> ExcelPlus export(Exporter<T> exporter) {
        this.exporter = exporter;
        return this;
    }

    /**
     * Writes the exported data to the file.
     *
     * @param file file object
     * @throws ExcelException
     */
    public void writeAsFile(File file) throws ExcelException {
        new FileExcelWriter(file).export(exporter);
    }

    /**
     * Writes the exported data to the ResponseWrapper.
     * <p>
     * The Wrapper pattern is used here, and not all people use ServletResponse.
     *
     * @param wrapper A Servlet Response package requires a filename to be set,
     *                and you can use the ResponseWrapper.create method or its default constructor.
     * @throws ExcelException
     */
    public void writeAsResponse(ResponseWrapper wrapper) throws ExcelException {
        new ResponseExcelWriter(wrapper).export(exporter);
    }

    public <T> ReaderResult<T> read(Class<T> type, ReaderParam readerParam) throws ParseException {
        List<Pair<Integer, T>> result;

        boolean is2007 = readerParam.getExcelFile().getName().toLowerCase().endsWith(".xlsx");
        if (readerParam.getParseType().equals(ParseType.SAX) && is2007) {
            result = new Excel2007Handler<>(type, readerParam).parse();
        } else {
            result = new DefaultExcelHandler<>(type, readerParam).parse();
        }
        return new ReaderResult<>(result);
    }

}
