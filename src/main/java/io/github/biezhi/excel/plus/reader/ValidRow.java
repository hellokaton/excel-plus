package io.github.biezhi.excel.plus.reader;

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
    private int     rowNum;

    public ValidRow(boolean valid, String msg) {
        this.valid = valid;
        this.msg = msg;
    }

    public static ValidRow ok() {
        return new ValidRow(true, null);
    }

    public static ValidRow fail(String msg) {
        return new ValidRow(false, msg);
    }

    boolean valid() {
        return valid;
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

    @Override
    public String toString() {
        if (valid) {
            return String.format("Row %d validate success.", this.rowNum);
        } else {
            return String.format("Row %d validate fail, the reason is: %s", this.rowNum, this.msg);
        }
    }

}
