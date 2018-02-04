package io.github.biezhi.excel.plus;

import io.github.biezhi.excel.plus.exception.ExcelException;
import io.github.biezhi.excel.plus.export.Exporter;
import io.github.biezhi.excel.plus.model.Order;

import java.io.File;
import java.util.List;

/**
 * @author biezhi
 * @date 2018/2/4
 */
public class OrderExample {

    public static void main(String[] args) throws ExcelException {
        List<Order> orders = BeanData.randOrders(10);
        System.out.println(orders);

        ExcelPlus excelPlus = new ExcelPlus();
        excelPlus.export(
                Exporter.create(orders)
                        .asFile(new File("订单列表.xls")));


    }

}
