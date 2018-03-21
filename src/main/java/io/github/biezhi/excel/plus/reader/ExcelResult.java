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
package io.github.biezhi.excel.plus.reader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Excel valid result
 *
 * @author biezhi
 * @date 2018/2/5
 */
public class ExcelResult<T> {

    /**
     * Store all ValidRow
     */
    private List<ValidRow> validRows = new ArrayList<>();

    /**
     * Store all verify the row data passed
     */
    private List<T> successRows = new ArrayList<>();

    /**
     * Store all row data, include the valid fail rows.
     */
    private List<T> rows = new ArrayList<>();

    /**
     * The number of rows read.
     */
    private int totalRows;

    /**
     * Whether the valid is passed or not, according to whether there is a valid or not.
     */
    private Boolean isValid = true;

    void addValidRow(ValidRow validRow) {
        validRows.add(validRow);
        if (validRow.isValidFail()) {
            isValid = false;
        }
    }

    void addSuccessRow(T item) {
        successRows.add(item);
    }

    void rows(List<T> rows) {
        this.rows = rows;
        this.totalRows = rows.size();
    }

    /**
     * @return Gets all the row data
     */
    public List<T> rows() {
        return rows;
    }

    /**
     * @return Gets all the row data passed by the valid
     */
    public List<T> successRows() {
        return successRows;
    }

    /**
     * @return The first error message pops up
     */
    public String popMsg() {
        List<ValidRow> errors = errors();
        if (null != errors && errors.size() > 0) {
            return errors.get(0).msg();
        }
        return null;
    }

    /**
     * @return Gets a list of all the valid fail lists
     */
    public List<ValidRow> errors() {
        return validRows.stream()
                .filter(ValidRow::isValidFail)
                .filter(ValidRow::hasMsg)
                .collect(Collectors.toList());
    }

    public int totalRows() {
        return totalRows;
    }

    /**
     * Get the statistics according to Key
     *
     * @param key counter key
     * @return count
     */
    public int counter(String key) {
        return validRows.stream().map(v -> v.counter(key)).reduce(0, (x, y) -> x + y);
    }

    public boolean isValid() {
        return isValid;
    }

}
