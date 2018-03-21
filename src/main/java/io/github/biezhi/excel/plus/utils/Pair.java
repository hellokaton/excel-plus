package io.github.biezhi.excel.plus.utils;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pair
 *
 * @author biezhi
 * @date 2018/2/5
 */
@Data
@NoArgsConstructor
public class Pair<K, V> {

    private K k;
    private V v;

    public Pair(K k, V v) {
        this.k = k;
        this.v = v;
    }

}
