package io.github.biezhi.excel.plus.reader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Verify that the row data is passed and the row number
 * and error messages are stored internally.
 *
 * @author biezhi
 * @date 2018/2/5
 */
public class ValidRow {

    private String  msg;
    private boolean valid;
    private boolean allowAdd;
    private int     rowNum;
    private Map<String, Integer> counter = new ConcurrentHashMap<>();

    public ValidRow(boolean valid, boolean allowAdd, String msg) {
        this.valid = valid;
        this.allowAdd = allowAdd;
        this.msg = msg;
    }

    public static ValidRow ok() {
        return new ValidRow(true, true, null);
    }

    public static ValidRow fail(String msg) {
        return new ValidRow(false, false, msg);
    }

    boolean isValid() {
        return valid;
    }

    boolean isValidFail() {
        return !valid;
    }

    boolean hasMsg() {
        return null != msg;
    }

    boolean allowAdd() {
        return this.allowAdd;
    }

    public ValidRow allowAdd(boolean allowAdd) {
        this.allowAdd = allowAdd;
        return this;
    }

    public String msg() {
        return this.msg;
    }

    void rowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public int rowNum() {
        return this.rowNum;
    }

    public ValidRow addCounter(String key) {
        return this.addCounter(key, 1);
    }

    public ValidRow addCounter(String key, int count) {
        Integer currentCount = counter.get(key);
        if (null == currentCount) {
            currentCount = count;
        }
        counter.put(key, currentCount);
        return this;
    }

    public ValidRow subtract(String key) {
        return this.subtract(key, 1);
    }

    public ValidRow subtract(String key, int count) {
        Integer currentCount = counter.get(key);
        if (null != currentCount) {
            currentCount = currentCount - count;
            counter.put(key, currentCount);
        }
        return this;
    }

    public int counter(String key) {
        return counter.getOrDefault(key, 0);
    }

    @Override
    public String toString() {
        if (valid) {
            return String.format("Row %d validate success.", this.rowNum);
        } else {
            return String.format("Row %d validate fail, the reason is: %s", this.rowNum, this.msg);
        }
    }

}
