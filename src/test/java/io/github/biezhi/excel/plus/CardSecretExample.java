package io.github.biezhi.excel.plus;

import io.github.biezhi.excel.plus.exception.ExcelException;
import io.github.biezhi.excel.plus.model.CardSecret;

import java.io.File;
import java.util.List;

/**
 * @author biezhi
 * @date 2018/2/4
 */
public class CardSecretExample {

    private static final ExcelPlus excelPlus = new ExcelPlus();

    public static void main(String[] args) throws ExcelException {
//        List<CardSecret> cardSecrets = new ArrayList<>();
//        cardSecrets.add(new CardSecret(1, "vlfdzepjmlz2y43z7er4", new BigDecimal("20")));
//        cardSecrets.add(new CardSecret(2, "rasefq2rzotsmx526z6g", new BigDecimal("10")));
//        cardSecrets.add(new CardSecret(2, "2ru44qut6neykb2380wt", new BigDecimal("50")));
//        cardSecrets.add(new CardSecret(1, "srcb4c9fdqzuykd6q4zl", new BigDecimal("15")));
//
//        excelPlus.export(cardSecrets)
//                .writeAsFile(new File("卡密列表.xls"));

        List<CardSecret> readList = excelPlus.read(new File("卡密列表.xls"), CardSecret.class)
                .startRow(1)
                .filter(cardSecret -> cardSecret.getSecret() != null)
                .asList();
        System.out.println(readList);

    }

}
