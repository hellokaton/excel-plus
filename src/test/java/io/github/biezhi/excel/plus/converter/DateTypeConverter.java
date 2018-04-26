/*
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
package io.github.biezhi.excel.plus.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTypeConverter implements Converter<Date> {

    @Override
    public String write(Date value) {
        if (null == value) {
            return "";
        }
        return "";
    }

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

    @Override
    public Date read(String value) {
        try {
            if (null == value) {
                return null;
            }
            Long unixTime = Long.valueOf(value);
            return new Date(unixTime);
        } catch (Exception e) {
            try {
                return sdf.parse(value);
            } catch (ParseException e2) {
                e2.printStackTrace();
                return null;
            }
        }
    }
}