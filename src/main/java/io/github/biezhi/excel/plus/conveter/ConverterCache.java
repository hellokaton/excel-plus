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

import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

/**
 * @author biezhi
 * @date 2018-12-12
 */
@UtilityClass
public class ConverterCache {

    private static final Map<Class<? extends Converter>, Converter> CONVERTER_MAP = new HashMap<>(64);

    static {
        CONVERTER_MAP.put(StringConverter.class, new StringConverter());
        CONVERTER_MAP.put(IntConverter.class, new IntConverter());
        CONVERTER_MAP.put(LongConverter.class, new LongConverter());
        CONVERTER_MAP.put(ShortConverter.class, new ShortConverter());
        CONVERTER_MAP.put(ByteConverter.class, new ByteConverter());
        CONVERTER_MAP.put(BooleanConverter.class, new BooleanConverter());
        CONVERTER_MAP.put(DoubleConverter.class, new DoubleConverter());
        CONVERTER_MAP.put(FloatConverter.class, new FloatConverter());
        CONVERTER_MAP.put(DecimalConverter.class, new DecimalConverter());
        CONVERTER_MAP.put(BigIntConverter.class, new BigIntConverter());
    }

    public static void addConvert(Converter converter) {
        CONVERTER_MAP.put(converter.getClass(), converter);
    }

    public static Converter getConvert(
            Class<? extends Converter> type) {
        return CONVERTER_MAP.get(type);
    }

}
