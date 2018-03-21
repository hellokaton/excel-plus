package io.github.biezhi.excel.plus;

import io.github.biezhi.excel.plus.enums.ParseType;
import io.github.biezhi.excel.plus.model.Station;
import io.github.biezhi.excel.plus.reader.ReaderParam;

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

        long      start     = System.currentTimeMillis();

        ExcelPlus excelPlus = new ExcelPlus();

        List<Station> stations = excelPlus.read(
                                    Station.class, ReaderParam.builder()
                                    .parseType(ParseType.SAX)
                                    .startRowIndex(2)
//                                    .sheetIndex(2)
                                    .sheetIndex(5)
//                                    .sheetName("山西")
                                    .excelFile(new File("test_data.xls"))
                                    .build()
                                ).asList();

        System.out.println(stations.size());
        System.out.println(stations);
        System.out.println((System.currentTimeMillis() - start) + "ms");
    }

}
