package io.github.biezhi.excel.plus;

import io.github.biezhi.excel.plus.enums.ParseType;
import io.github.biezhi.excel.plus.model.CardSecret;
import io.github.biezhi.excel.plus.model.Station;
import io.github.biezhi.excel.plus.reader.Reader;

import java.io.File;
import java.util.List;

/**
 * Excel read write examples
 *
 * @author biezhi
 * @date 2018/2/5
 */
public class Examples {

    public static void main(String[] args) throws Exception {

        long start = System.currentTimeMillis();

        ExcelPlus excelPlus = new ExcelPlus();

        Reader reader = Reader.create()
                .parseType(ParseType.SAX)
                .startRowIndex(2)
                .sheetIndex(5)
                .excelFile(new File("test_data.xlsx"));

        List<Station> stations = excelPlus.read(Station.class, reader).asList();

        System.out.println(stations.size());
        System.out.println(stations);
        System.out.println((System.currentTimeMillis() - start) + "ms");

        reader = Reader.create()
                .parseType(ParseType.SAX)
                .startRowIndex(1)
                .sheetIndex(0)
                .sheetName("Hello")
                .excelFile(new File("卡密列表.xlsx"));

        List<CardSecret> cardSecrets = excelPlus.read(CardSecret.class, reader).asList();

        System.out.println(cardSecrets.size());
        System.out.println(cardSecrets);
//        System.out.println((System.currentTimeMillis() - start) + "ms");
    }

}
