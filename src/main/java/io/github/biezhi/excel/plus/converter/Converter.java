package io.github.biezhi.excel.plus.converter;

/**
 * Data type converter interface
 *
 * @author biezhi
 * @date 2018/2/4
 */
public interface Converter<T> {

    /**
     * Converts data from Java objects to a particular string that Excel wants to output
     *
     * @param value
     * @return
     */
    String write(T value);

    /**
     * Convert the fields that Excel reads to Java object types
     *
     * @param value
     * @return
     */
    default T read(String value) {
        return null;
    }

}