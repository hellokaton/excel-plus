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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Date to string converter
 *
 * @author biezhi
 * @date 2018-12-12
 */
public class DateConverter implements Converter<String, Date> {

    private ThreadLocal<DateFormat> df;

    public DateConverter(String pattern) {
        this.df = ThreadLocal.withInitial(() -> new SimpleDateFormat(pattern));
    }

    @Override
    public Date stringToR(String value) throws ConverterException {
        try {
            if(null == value){
                return null;
            }
            return df.get().parse(value);
        } catch (Exception e) {
            throw new ConverterException("convert [" + value + "] to Date error", e);
        }
    }

    @Override
    public String toString(Date date) throws ConverterException {
        try {
            if(null == date){
                return null;
            }
            return df.get().format(date);
        } catch (Exception e) {
            throw new ConverterException("convert [" + date + "] to String error", e);
        }
    }

}
