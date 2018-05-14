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

import io.github.biezhi.excel.plus.utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author biezhi
 * @date 2018/3/21
 */
public class ReaderResult<T> {

    private List<Pair<Integer, T>> result;

    /**
     * Store all ValidRow
     */
    private List<ValidRow> validRows = new ArrayList<>();

    /**
     * Store all verify the row data passed
     */
    private List<T> successRows;

    private List<T> allRows;

    /**
     * The number of rows read.
     */
    private int totalRows;

    /**
     * Whether the valid is passed or not, according to whether there is a valid or not.
     */
    private Boolean isValid = true;

    public ReaderResult(List<Pair<Integer, T>> result) {
        this.result = result;
        this.totalRows = result.size();
        this.allRows = result.stream().map(Pair::getV).collect(Collectors.toList());
        this.successRows = new ArrayList<>(this.allRows);
    }

    /**
     * Reading the Excel document content into a List container.
     * <p>
     * Get the sheet Name according to the Java Class type, and the sheet with an index of 0 if the sheet failure is read.
     * <p>
     * The cell data that is read from each row is injected into the field of the Java object by reflection,
     * and you need to make sure that the Java Type has a non-parameterized constructor,
     * otherwise the process will fail.
     *
     * @return excel rows
     */
    public List<T> asList() {
        return successRows;
    }

    public Stream<T> filter(Predicate<T> predicate) {
        return asList().stream().filter(predicate);
    }

    public <R> Stream<R> map(Function<T, R> function) {
        return asList().stream().map(function);
    }

    public int allRowCount() {
        return allRows.size();
    }

    public int successRowCount() {
        return successRows.size();
    }

    void addValidRow(ValidRow validRow) {
        validRows.add(validRow);
        if (isValid && validRow.isValidFail()) {
            isValid = false;
        }
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

    /**
     * By passing a functional lambda interface to verify the correctness of the row data,
     * you might write it like this in your code:
     * <p>
     * valid(cardSecret -> {
     * BigDecimal amount = cardSecret.getAmount();
     * if (amount.doubleValue() < 20) {
     * return ValidRow.fail("The minimum amount is 20");
     * }
     * return ValidRow.ok();
     * })
     * <p>
     * You can also do statistics in this function, such as recording all the empty rows, and you can do that.
     * <p>
     * .valid(cardSecret -> {
     * if(cardSecret.getCardType().equals(1)){
     * return ValidRow.ok().addCounter("CARD_TYPE_1");
     * }
     * return ValidRow.ok();
     * })
     *
     * @param validFunction valid the lambda implementation of the row data
     * @return self
     */
    public ReaderResult<T> valid(BiFunction<Integer, T, ValidRow> validFunction) {
        successRows.clear();
        result.forEach(pair -> {
            Integer  rowNum   = pair.getK();
            T        item     = pair.getV();
            ValidRow validRow = validFunction.apply(rowNum, item);
            validRow.rowNum(rowNum);
            if (validRow.isValid() && validRow.allowAdd()) {
                successRows.add(item);
            }
            this.addValidRow(validRow);
        });
        return this;
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
