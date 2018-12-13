/**
 *  Copyright (c) 2018, biezhi (biezhi.me@gmail.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.github.biezhi.excel.plus.writer;

import io.github.biezhi.excel.plus.Writer;
import io.github.biezhi.excel.plus.exception.WriterException;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.http.HttpServletResponse;

/**
 * @author biezhi
 * @date 2018-12-12
 */
public class ResponseExcelWriter extends ExcelWriter {

    private ResponseWrapper wrapper;

    public ResponseExcelWriter(ResponseWrapper wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public void writeSheet(Writer writer) throws WriterException {
        HttpServletResponse servletResponse = this.wrapper.getServletResponse();
        try {
            String fileName = wrapper.getFileName();

            servletResponse.setContentType("application/x-xls");
            fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            servletResponse.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            super.outputStream = servletResponse.getOutputStream();

            SXSSFWorkbook workbook = new SXSSFWorkbook(writer.bufferSize());
            super.writeSheet(writer, workbook);
        } catch (Exception e) {
            throw new WriterException(e);
        }
    }
}
