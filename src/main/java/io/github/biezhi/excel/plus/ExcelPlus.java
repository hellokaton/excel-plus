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
package io.github.biezhi.excel.plus;

import io.github.biezhi.excel.plus.enums.ExcelType;

import java.io.File;
import java.io.InputStream;

/**
 * ExcelPlus
 * <p>
 * The class used to create the Excel reader,
 * if you prefer to use Reader or Writer directly
 *
 * @author biezhi
 * @date 2018-12-11
 */
@Deprecated
public final class ExcelPlus {

    public Reader read(Class<?> modelType) {
        return Reader.create(modelType);
    }

    public Reader read(Class<?> modelType, File fromFile) {
        return Reader.create(modelType, fromFile);
    }

    public Reader read(Class<?> modelType, InputStream fromStream) {
        return Reader.create(modelType, fromStream);
    }

    public Writer write() {
        return this.write(ExcelType.XLSX);
    }

    public Writer write(ExcelType excelType) {
        return Writer.create(excelType);
    }

}
