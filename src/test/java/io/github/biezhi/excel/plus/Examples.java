package io.github.biezhi.excel.plus;

import io.github.biezhi.excel.plus.exception.ExcelException;
import io.github.biezhi.excel.plus.model.CardSecret;
import io.github.biezhi.excel.plus.reader.ExcelResult;
import io.github.biezhi.excel.plus.reader.ValidRow;
import io.github.biezhi.excel.plus.writer.Exporter;
import io.github.biezhi.excel.plus.writer.ResponseWrapper;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel read write examples
 *
 * @author biezhi
 * @date 2018/2/5
 */
public class Examples {

    private ExcelPlus excelPlus = new ExcelPlus();

    private InputStream getCardStream() {
        return Examples.class.getResourceAsStream("/卡密列表.xlsx");
    }

    @Test
    public void testReadExcel() throws ExcelException {

        List<CardSecret> cardSecrets = excelPlus.read(getCardStream(), CardSecret.class).asList();
        Assert.assertNotNull(cardSecrets);
        Assert.assertEquals(4, cardSecrets.size());
    }

    @Test
    public void testReadFilter() throws ExcelException {
        List<CardSecret> cardSecrets = excelPlus.read(getCardStream(), CardSecret.class)
                .filter(cardSecret -> cardSecret.getAmount().doubleValue() > 50)
                .asList();

        Assert.assertNotNull(cardSecrets);
        Assert.assertEquals(3, cardSecrets.size());
    }

    @Test
    public void testReadValid() throws ExcelException {
        ExcelResult<CardSecret> excelResult = excelPlus.read(getCardStream(), CardSecret.class)
                .startRow(2)
                .valid(cardSecret -> {
                    BigDecimal amount = cardSecret.getAmount();
                    if (amount.doubleValue() < 20) {
                        return ValidRow.fail("最小金额为20");
                    }
                    return ValidRow.ok();
                })
                .asResult();

        if (!excelResult.isValid()) {
            excelResult.errors().forEach(System.out::println);
        } else {
            Assert.assertEquals(3, excelResult.rows().size());
        }
    }

    @Test
    public void testReadCounter() throws ExcelException {
        ExcelResult<CardSecret> excelResult = excelPlus.read(getCardStream(), CardSecret.class)
                .startRow(2)
                .valid(cardSecret -> {
                    if (cardSecret.getCardType().equals(1)) {
                        return ValidRow.ok().addCounter("CARD_TYPE_1");
                    }
                    return ValidRow.ok();
                })
                .asResult();

        if (!excelResult.isValid()) {
            excelResult.errors().forEach(System.out::println);
        } else {
            Assert.assertEquals(3, excelResult.rows().size());
        }
    }

    @Test
    public void testExport() throws ExcelException {
        List<CardSecret> cardSecrets = this.buildCardSecrets();
        excelPlus.export(cardSecrets).writeAsFile(new File("export.xls"));
    }

    @Test
    public void testExportTitle() throws ExcelException {
        List<CardSecret> cardSecrets = this.buildCardSecrets();
        excelPlus.export(Exporter.create(cardSecrets).title("卡密列表第一季数据")).writeAsFile(new File("卡密列表.xls"));
    }

    private List<CardSecret> buildCardSecrets() {
        List<CardSecret> cardSecrets = new ArrayList<>();
        cardSecrets.add(new CardSecret(1, "vlfdzepjmlz2y43z7er4", new BigDecimal("20"), true));
        cardSecrets.add(new CardSecret(2, "rasefq2rzotsmx526z6g", new BigDecimal("10"), true));
        cardSecrets.add(new CardSecret(2, "2ru44qut6neykb2380wt", new BigDecimal("50"), false));
        cardSecrets.add(new CardSecret(1, "srcb4c9fdqzuykd6q4zl", new BigDecimal("15"), true));
        return cardSecrets;
    }

    @Test
    public void testExportStyle() throws ExcelException {
        List<CardSecret> cardSecrets = this.buildCardSecrets();
        excelPlus.export(Exporter.create(cardSecrets).headerStyle(workbook -> {
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setAlignment(HorizontalAlignment.LEFT);

            headerStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Font headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            return headerStyle;
        })).writeAsFile(new File("卡密列表.xls"));
    }

    @Test
    public void testExportByTpl() throws ExcelException {
        List<CardSecret> cardSecrets = this.buildCardSecrets();
        excelPlus.export(
                Exporter.create(cardSecrets).byTemplate("tpl.xls").startRow(2)
        ).writeAsFile(new File("template_rows.xls"));
    }

//    @Test
    public void testDownload() throws ExcelException {
        List<CardSecret> cardSecrets = this.buildCardSecrets();
        excelPlus.export(cardSecrets).writeAsResponse(ResponseWrapper.create(null, "xxx表格.xls"));
    }

}
