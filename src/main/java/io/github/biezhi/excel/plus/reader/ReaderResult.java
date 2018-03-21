package io.github.biezhi.excel.plus.reader;

import io.github.biezhi.excel.plus.utils.Pair;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    public Predicate<T> filter() {
        return null;
    }

    public Function<T, ValidRow> valid() {
        return null;
    }

}
