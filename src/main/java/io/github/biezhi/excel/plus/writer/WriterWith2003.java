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
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.OutputStream;

/**
 * @author biezhi
 * @date 2018-12-11
 */
public class WriterWith2003 extends ExcelWriter {

    public WriterWith2003(OutputStream outputStream) {
        super(outputStream);
    }

    @Override
    public void writeSheet(Writer writer) throws WriterException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        super.writeSheet(writer, workbook);
    }

}
