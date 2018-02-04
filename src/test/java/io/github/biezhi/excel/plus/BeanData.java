package io.github.biezhi.excel.plus;

import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import io.github.benas.randombeans.api.Randomizer;
import io.github.biezhi.excel.plus.model.Order;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Bean对象生成器
 *
 * @author biezhi
 * @date 2018/1/15
 */
public final class BeanData {

    private static final EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
            .seed(123L)
            .objectPoolSize(100)
            .randomizationDepth(1)
            .charset(StandardCharsets.UTF_8)
            .dateRange(LocalDate.now().plusMonths(-2), LocalDate.now().plusMonths(3))
            .stringLengthRange(5, 10)
            .collectionSizeRange(1, 10)
            .scanClasspathForConcreteTypes(true)
            .overrideDefaultInitialization(false)
            .build();

    public static List<Order> randOrders(int count) {
        return enhancedRandom.objects(Order.class, count).collect(Collectors.toList());
    }

}