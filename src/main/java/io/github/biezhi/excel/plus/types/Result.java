/*
 *  Copyright (c) 2018, biezhi <biezhi.me@gmail.com>
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
package io.github.biezhi.excel.plus.types;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Excel Result
 *
 * @author biezhi
 * @date 2019-03-14
 */
public class Result<T> {

    private AtomicInteger atomicSuccess = new AtomicInteger(0);
    private AtomicInteger atomicError   = new AtomicInteger(0);

    private Map<Integer, String> errorMap = new TreeMap<>();

    private List<T> rows;

    private List<T> successRows;
    private List<T> errorRows;

    public Result(List<T> rows) {
        this.rows = rows;
        this.successRows = new ArrayList<>(rows.size());
        this.errorRows = new ArrayList<>();
    }

    public Result<T> valid(RowPredicate<Integer, T> rowPredicate) {
        for (int i = 0, size = rows.size(); i < size; i++) {
            Valid valid = rowPredicate.test(i + 1, rows.get(i));
            if (null == valid) {
                continue;
            }
            if (valid.isSuccess()) {
                successRows.add(rows.get(i));
                atomicSuccess.incrementAndGet();
            } else {
                atomicError.incrementAndGet();
                errorRows.add(rows.get(i));
                errorMap.put(i + 1, valid.msg());
            }
        }
        return this;
    }

    public boolean isValid() {
        return atomicError.get() > 0;
    }

    public int count() {
        return rows.size();
    }

    public int successCount() {
        return atomicSuccess.get();
    }

    public int errorCount() {
        return atomicError.get();
    }

    public List<T> rows() {
        return rows;
    }

    public List<T> successRows() {
        return successRows;
    }

    public List<T> errorRows() {
        return errorRows;
    }

    public List<String> errorMessages() {
        return new ArrayList<>(errorMap.values());
    }

    public Map<Integer, String> errorMap() {
        return errorMap;
    }

}
