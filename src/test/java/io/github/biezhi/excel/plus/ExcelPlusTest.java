package io.github.biezhi.excel.plus;

import io.github.biezhi.excel.plus.enums.ParseType;
import io.github.biezhi.excel.plus.exception.ParseException;
import io.github.biezhi.excel.plus.model.CardSecret;
import io.github.biezhi.excel.plus.reader.ReaderParam;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * @author biezhi
 * @date 2018/3/21
 */
public class ExcelPlusTest {

    private ExcelPlus excelPlus = new ExcelPlus();

    private File getCardExcelFile() {
        try {
            return new File(URLDecoder.decode(ExcelPlusTest.class.getResource("/卡密列表.xlsx").getPath(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    @Test
    public void testReadByDOM() throws ParseException {
        List<CardSecret> cardSecrets = excelPlus.read(CardSecret.class,
                ReaderParam.builder().excelFile(getCardExcelFile()).build()).asList();

        System.out.println(cardSecrets);
        Assert.assertNotNull(cardSecrets);
        Assert.assertEquals(4, cardSecrets.size());
    }

    @Test
    public void testReadBySAX() throws ParseException {
        List<CardSecret> cardSecrets = excelPlus.read(CardSecret.class,
                ReaderParam.builder().parseType(ParseType.SAX).excelFile(getCardExcelFile()).build()).asList();

        System.out.println(cardSecrets);
        Assert.assertNotNull(cardSecrets);
        Assert.assertEquals(4, cardSecrets.size());
    }

}
