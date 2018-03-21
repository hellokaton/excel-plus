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

import io.github.biezhi.excel.plus.utils.Pair;

import java.util.List;
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

    public ReaderResult(List<Pair<Integer, T>> result) {
        this.result = result;
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
        return result.stream().map(Pair::getV).collect(Collectors.toList());
    }

    public Stream<T> filter(Predicate<T> predicate) {
        return asList().stream().filter(predicate);
    }

    public <R> Stream<R> map(Function<T, R> function) {
        return asList().stream().map(function);
    }

    public Function<T, ValidRow> valid() {
        return null;
    }

}
