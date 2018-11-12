package io.github.biezhi.excel.plus.model;

import io.github.biezhi.excel.plus.annotation.ExcelField;
import lombok.Data;

/**
 * @author darren <yangdaiquan@yiguo.com>
 * @description
 * @date 2018/11/9 15:03
 */
@Data
public class TestBean {

    Integer rowNum;

    @ExcelField(columnName = "One",order = 0)
	String cellOne;

    @ExcelField(columnName = "Two",order = 1)
	String cellTwo;

    @ExcelField(columnName = "Thread",order = 2)
	String cellThread;

    @ExcelField(columnName = "Four",order = 3)
	String cellFour;

    @ExcelField(columnName = "Five",order = 4)
	String cellFive;

    @ExcelField(columnName = "Six",order = 5)
	String cellSix;
}
