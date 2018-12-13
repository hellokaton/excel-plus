/**
 * Copyright (c) 2018, biezhi (biezhi.me@gmail.com)
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
package io.github.biezhi.excel.plus.writer;

import io.github.biezhi.excel.plus.Writer;
import io.github.biezhi.excel.plus.exception.WriterException;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Excel Writer by 2007
 *
 * @author biezhi
 * @date 2018-12-11
 */
public class WriterWith2007 extends ExcelWriter {

    public WriterWith2007(OutputStream outputStream) {
        super(outputStream);
    }

    @Override
    public void writeSheet(Writer writer) throws WriterException {
        if (writer.template() != null) {
            try {
                this.workbook = WorkbookFactory.create(writer.template());
                super.writeSheet(writer);
            } catch (IOException e) {
                throw new WriterException(e);
            }
        } else {
            this.workbook = new SXSSFWorkbook(writer.bufferSize());
            super.writeSheet(writer);
        }
    }

}
