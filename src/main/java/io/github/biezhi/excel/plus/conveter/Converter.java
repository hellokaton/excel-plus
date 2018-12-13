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
package io.github.biezhi.excel.plus.conveter;

import io.github.biezhi.excel.plus.exception.ConverterException;

/**
 * @author biezhi
 * @date 2018-12-12
 */
public interface Converter<String, R> {

    R stringToR(String value) throws ConverterException;

    default java.lang.String toString(R fieldValue) throws ConverterException {
        if (null == fieldValue) {
            return null;
        }
        return fieldValue.toString();
    }

}
