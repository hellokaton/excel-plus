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
package io.github.biezhi.excel.plus.handler;

import io.github.biezhi.excel.plus.exception.ExcelException;
import io.github.biezhi.excel.plus.reader.Reader;
import io.github.biezhi.excel.plus.utils.CSV;
import io.github.biezhi.excel.plus.utils.ExcelUtils;
import io.github.biezhi.excel.plus.utils.Pair;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author biezhi
 * @date 2018/3/21
 */
@Slf4j
public class CSVHandler<T> implements ExcelHandler {

    private Class<T> type;
    private Reader   reader;

    public CSVHandler(Class<T> type, Reader reader) {
        this.type = type;
        this.reader = reader;
    }

    @Override
    public List<Pair<Integer, T>> parse() throws ExcelException {
        List<Pair<Integer, T>> list = new ArrayList<>();
        try (InputStream in = new FileInputStream(reader.getExcelFile())) {
            CSV          csv      = new CSV(true, ',', in);
            List<String> colNames = null;
            if (csv.hasNext()) colNames = new ArrayList<>(csv.next());

            int rowNum = 1;
            while (csv.hasNext()) {
                if (rowNum < reader.getStartRowIndex()) {
                    rowNum++;
                    continue;
                }
                List<String> row  = new ArrayList<>(csv.next());
                T            item = this.buildItem(row);
                if (null != item) {
                    list.add(new Pair<>(rowNum, item));
                }
                rowNum++;
            }
        } catch (IOException e) {
            throw new ExcelException(e);
        }
        return list;
    }

    /**
     * Set the Excel row data to the item object.
     *
     * @param row excel row
     * @return return java instance
     */
    private T buildItem(List<String> row) {
        T item = ExcelUtils.newInstance(type);
        if (null == item) {
            return null;
        }
        for (int i = 0; i < row.size(); i++) {
            ExcelUtils.writeToField(item, i, row.get(i));
        }
        return item;
    }

}
