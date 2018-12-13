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
package io.github.biezhi.excel.plus.conveter;

import io.github.biezhi.excel.plus.utils.StringUtils;

import java.math.BigInteger;

/**
 * @author biezhi
 * @date 2018-12-12
 */
public class BigIntConverter extends NumberConverter implements Converter<String, BigInteger> {

    @Override
    public BigInteger stringToR(String value) {
        value = replaceComma(value);
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return new BigInteger(value);
    }

}
