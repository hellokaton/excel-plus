package io.github.biezhi.excel.plus.reader;

import java.util.ArrayList;
import java.util.List;

/**
 * Excel valid result
 *
 * @author biezhi
 * @date 2018/2/5
 */
public class ExcelResult<T> {

    private List<ValidRow> errors = new ArrayList<>();
    private List<T>        rows;

    public boolean isValid() {
        return errors.size() == 0;
    }

    public void addError(ValidRow validRow) {
        errors.add(validRow);
    }

    public void rows(List<T> rows) {
        this.rows = rows;
    }

    public List<T> rows() {
        return rows;
    }

    public List<ValidRow> errors() {
        return errors;
    }

}
