package io.github.biezhi.excel.plus;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author biezhi
 * @date 2018/3/21
 */
@Data
public class ParsedRow {

    private Integer rowIndex = 1;
    private Map<Integer, String> cellMap = new HashMap<>();

}
