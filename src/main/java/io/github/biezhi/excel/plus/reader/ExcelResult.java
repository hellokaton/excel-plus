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

    private List<ValidRow> validRows   = new ArrayList<>();
    private List<T>        successRows = new ArrayList<>();
    private List<T>        rows        = new ArrayList<>();
    private int totalRows;
    private Boolean isValid = true;

    public void addValidRow(ValidRow validRow) {
        validRows.add(validRow);
        if (validRow.isValidFail()) {
            isValid = false;
        }
    }

    public void addSuccessRow(T item) {
        successRows.add(item);
    }

    public void rows(List<T> rows) {
        this.rows = rows;
        this.totalRows = rows.size();
    }

    public List<T> rows() {
        return rows;
    }

    public List<T> successRows() {
        return successRows;
    }

    public String popMsg() {
        List<ValidRow> errors = errors();
        if (null != errors && errors.size() > 0) {
            return errors.get(0).msg();
        }
        return null;
    }

    public List<ValidRow> errors() {
        return validRows.stream()
                .filter(ValidRow::isValidFail)
                .filter(ValidRow::hasMsg)
                .collect(Collectors.toList());
    }

    public int totalRows() {
        return totalRows;
    }

    public int counter(String key) {
        return validRows.stream().map(v -> v.counter(key)).reduce(0, (x, y) -> x + y);
    }

    public boolean isValid() {
        return isValid;
    }

}
