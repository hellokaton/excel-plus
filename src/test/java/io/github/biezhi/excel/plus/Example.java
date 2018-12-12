package io.github.biezhi.excel.plus;

import io.github.biezhi.excel.plus.exception.ReaderException;
import io.github.biezhi.excel.plus.model.Sample;

import java.io.File;
import java.util.List;

/**
 * @author biezhi
 * @date 2018-12-12
 */
public class Example {

    public static void main(String[] args) throws ReaderException {

        ExcelPlus excelPlus = new ExcelPlus();

        long start = System.currentTimeMillis();
//        List<Member> members = excelPlus.read()
//                .from(new File("卡详情一览.xlsx"))
//                .asList(Member.class);
//
//        System.out.println( (System.currentTimeMillis() - start)/1000 + "s" );
//        System.out.println(members.size());

//        List<Financial> financials = excelPlus.read()
//                .from(new File("FinancialSample.xlsx"))
//                .asList(Financial.class);
//        System.out.println(financials);

        start = System.currentTimeMillis();

        List<Sample> samples = excelPlus.read()
                .sheetIndex(1)
                .from(new File("SampleData.xlsx"))
                .asList(Sample.class);

        System.out.println( (System.currentTimeMillis() - start)/1000 + "s" );
        System.out.println(samples.size());
    }

}
