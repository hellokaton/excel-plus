package io.github.biezhi.excel.plus.types;

@FunctionalInterface
public interface StyleConsumer<T, U> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     */
    U accept(T t, U u);

}