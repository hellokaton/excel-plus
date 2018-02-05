package io.github.biezhi.excel.plus;

import io.github.biezhi.excel.plus.exception.ExcelException;
import io.github.biezhi.excel.plus.model.Order;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * @author biezhi
 * @date 2018/2/4
 */
public class OrderExample {

    private static final ExcelPlus excelPlus = new ExcelPlus();

    public static void main(String[] args) throws ExcelException, FileNotFoundException {
        List<Order> orders = BeanData.randOrders(10);
        // System.out.println(orders);

        excelPlus.export(orders)
                .writeAsFile(new File("订单列表.xls"));

        List<Order> orders1 = excelPlus.read(new File(""), Order.class).asList();
//        List<CardSecret> cardSecrets = excelPlus.read(new FileInputStream(new File("")), CardSecret.class);


    }

}
