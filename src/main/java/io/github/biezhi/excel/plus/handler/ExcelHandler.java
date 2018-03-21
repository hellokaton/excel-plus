package io.github.biezhi.excel.plus.handler;

import io.github.biezhi.excel.plus.exception.ParseException;
import io.github.biezhi.excel.plus.utils.Pair;

import java.util.List;

/**
 * @author biezhi
 * @date 2018/3/21
 */
public interface ExcelHandler<T> {

    List<Pair<Integer, T>> parse() throws ParseException;

}