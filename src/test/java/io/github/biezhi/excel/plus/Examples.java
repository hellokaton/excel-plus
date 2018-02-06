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
import org.junit.Test;

import java.io.File;
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

    @Test
    public void testReadExcel() throws ExcelException {
        List<CardSecret> cardSecrets = excelPlus.read(new File("卡密列表.xls"), CardSecret.class).asList();
        System.out.println(cardSecrets);
    }

    @Test
    public void testReadFilter() throws ExcelException {
        List<CardSecret> cardSecrets = excelPlus.read(new File("卡密列表.xls"), CardSecret.class)
                .filter(cardSecret -> cardSecret.getAmount().doubleValue() > 10)
                .asList();
        System.out.println(cardSecrets);
    }

    @Test
    public void testReadValid() throws ExcelException {
        ExcelResult<CardSecret> excelResult = excelPlus.read(new File("卡密列表.xls"), CardSecret.class)
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
            System.out.println(excelResult.rows().size());
        }
    }

    @Test
    public void testExport() throws ExcelException {
        List<CardSecret> cardSecrets = this.buildCardSecrets();
        excelPlus.export(cardSecrets).writeAsFile(new File("卡密列表.xls"));
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

    @Test
    public void testDownload() throws ExcelException {
        List<CardSecret> cardSecrets = this.buildCardSecrets();
        excelPlus.export(cardSecrets).writeAsResponse(ResponseWrapper.create(null, "xxx表格.xls"));
    }

}
