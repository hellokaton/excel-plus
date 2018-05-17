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
package io.github.biezhi.excel.plus;

import io.github.biezhi.excel.plus.enums.ParseType;
import io.github.biezhi.excel.plus.exception.ExcelException;
import io.github.biezhi.excel.plus.handler.CSVHandler;
import io.github.biezhi.excel.plus.handler.DefaultExcelHandler;
import io.github.biezhi.excel.plus.handler.Excel2007Handler;
import io.github.biezhi.excel.plus.reader.Reader;
import io.github.biezhi.excel.plus.reader.ReaderResult;
import io.github.biezhi.excel.plus.utils.ExcelUtils;
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

    public <T> ReaderResult<T> read(Class<T> type, Reader reader) throws ExcelException {
        List<Pair<Integer, T>> result;
        this.beforeCheck(reader);

        boolean is2007 = reader.getExcelFile().getName().toLowerCase().endsWith(".xlsx");
        if (reader.getParseType().equals(ParseType.SAX) && is2007) {
            result = new Excel2007Handler<>(type, reader).parse();
        } else {
            if (reader.getExcelFile().getName().endsWith(".csv")) {
                result = new CSVHandler<>(type, reader).parse();
            } else {
                result = new DefaultExcelHandler<>(type, reader).parse();
            }
        }
        return new ReaderResult<>(result);
    }

    private void beforeCheck(Reader reader) throws ExcelException {
        if (null == reader) {
            throw new ExcelException("Reader not is null.");
        }
        if (null == reader.getExcelFile()) {
            throw new ExcelException("Excel file not is null.");
        }
        if (null == reader.getParseType()) {
            throw new ExcelException("Excel parse type not is null.");
        }
        if (reader.getSheetIndex() < 0) {
            throw new ExcelException("SheetIndex Can't be less than 0.");
        }
        if (reader.getStartRowIndex() < 0) {
            throw new ExcelException("StartRowIndex Can't be less than 0.");
        }
        if (ParseType.SAX.equals(reader.getParseType()) && ExcelUtils.isNotEmpty(reader.getSheetName())) {
            throw new ExcelException("SAX mode don't support custom SheetName.");
        }
    }
}
