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
package io.github.biezhi.excel.plus.reader;

import io.github.biezhi.excel.plus.enums.ParseType;
import lombok.Data;

import java.io.File;
import java.io.InputStream;

/**
 * @author biezhi
 * @date 2018/3/20
 */
@Data
public class Reader {

    private String sheetName;

    private int sheetIndex    = 0;
    private int startRowIndex = 1;

    private ParseType parseType = ParseType.DOM;

    private File excelFile;

    public static Reader create() {
        return new Reader();
    }

    public Reader sheetName(String sheetName) {
        this.sheetName = sheetName;
        return this;
    }

    public Reader startRowIndex(int startRowIndex) {
        this.startRowIndex = startRowIndex;
        return this;
    }

    public Reader sheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
        return this;
    }

    public Reader parseType(ParseType parseType) {
        this.parseType = parseType;
        return this;
    }

    public Reader excelFile(File excelFile) {
        this.excelFile = excelFile;
        return this;
    }

}
