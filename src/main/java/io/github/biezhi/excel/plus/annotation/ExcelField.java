package io.github.biezhi.excel.plus.annotation;

import io.github.biezhi.excel.plus.Constant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author biezhi
 * @date 2018/2/4
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelField {

    int readOrder() default Constant.DEFAULT_ORDER;

    int writeOrder() default Constant.DEFAULT_ORDER;

    String title();

    String datePattern() default "yyyy-MM-dd";

}
