package io.github.biezhi.excel.plus.annotation;

import io.github.biezhi.excel.plus.Constant;
import io.github.biezhi.excel.plus.converter.Converter;
import io.github.biezhi.excel.plus.converter.EmptyConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Model excel write field bind
 *
 * @author biezhi
 * @date 2018/2/4
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface WriteField {

    /**
     * When the same field sequential read and write fields of use, for most cleanup.
     * start from 0
     *
     * @return field order
     */
    int order() default Constant.DEFAULT_ORDER;

    /**
     * Set the field to write to the header name of Excel.
     *
     * @return excel header column title
     */
    String columnName() default "";

    /**
     * When a field is a Date or a Time type,
     * the configuration is valid for Date, LocalDate, LocalDateTime.
     *
     * @return Date format pattern
     */
    String datePattern() default "";

    /**
     * @return
     */
    Class<? extends Converter> convertType() default EmptyConverter.class;

}
