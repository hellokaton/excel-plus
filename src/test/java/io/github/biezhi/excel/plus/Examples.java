package io.github.biezhi.excel.plus;

import io.github.biezhi.excel.plus.model.CardSecret;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel read write examples
 *
 *
 * @author biezhi
 * @date 2018/2/5
 */
public class Examples {

    public static void main(String[] args) throws Exception {
        ExcelPlus excelPlus = new ExcelPlus();
        List<CardSecret> cardSecrets = new ArrayList<>();
        cardSecrets.add(new CardSecret(1, "vlfdzepjmlz2y43z7er4", new BigDecimal("20"), false));
        cardSecrets.add(new CardSecret(2, "rasefq2rzotsmx526z6g", new BigDecimal("10"), false));
        cardSecrets.add(new CardSecret(2, "2ru44qut6neykb2380wt", new BigDecimal("50"), true));
        cardSecrets.add(new CardSecret(1, "srcb4c9fdqzuykd6q4zl", new BigDecimal("15"), false));

        excelPlus.export(cardSecrets).writeAsFile(new File("卡密列表.xlsx"));
    }

}
