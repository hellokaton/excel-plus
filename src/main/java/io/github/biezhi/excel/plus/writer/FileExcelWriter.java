/**
 *  Copyright (c) 2018, biezhi 王爵 (biezhi.me@gmail.com)
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

import io.github.biezhi.excel.plus.enums.ExcelType;
import io.github.biezhi.excel.plus.exception.ExcelException;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Write data to Excel file.
 *
 * @author biezhi
 * @date 2018/2/4
 */
public class FileExcelWriter implements ExcelWriter {

    private File savePath;

    public FileExcelWriter(File file) {
        this.savePath = file;
    }

    @Override
    public <T> void export(Exporter<T> exporter) throws ExcelException {
        if (null == savePath) {
            throw new ExcelException("Save the Excel path can not be null.");
        }
        try {
            ExcelType excelType = ExcelType.getExcelType(savePath.getName());
            exporter.setExcelType(excelType);
            this.export(exporter, new FileOutputStream(savePath));
        } catch (Exception e) {
            throw new ExcelException(e);
        }
    }

}
