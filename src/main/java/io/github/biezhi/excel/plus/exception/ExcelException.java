package io.github.biezhi.excel.plus.exception;

/**
 * Excel Read Write Exception
 *
 * @author biezhi
 * @date 2018/2/4
 */
public class ExcelException extends Exception {

    public ExcelException() {
    }

    public ExcelException(String message) {
        super(message);
    }

    public ExcelException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelException(Throwable cause) {
        super(cause);
    }

    public ExcelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
