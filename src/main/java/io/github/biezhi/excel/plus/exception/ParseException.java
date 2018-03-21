package io.github.biezhi.excel.plus.exception;

/**
 * 表格转换错误
 */
public class ParseException extends Exception {
    private static final long serialVersionUID = -2451526411018517607L;

    public ParseException(Throwable t) {
        super("表格转换错误", t);
    }

}