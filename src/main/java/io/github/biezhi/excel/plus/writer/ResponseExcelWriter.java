package io.github.biezhi.excel.plus.writer;

import io.github.biezhi.excel.plus.exception.ExcelException;
import jxl.Workbook;

import javax.servlet.http.HttpServletResponse;

/**
 * ServletResponse exporter
 *
 * @author biezhi
 * @date 2018/2/4
 */
public class ResponseExcelWriter implements ExcelWriter {

    private ResponseWrapper wrapper;

    public ResponseExcelWriter(ResponseWrapper wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public <T> void export(Exporter<T> exportor) throws ExcelException {
        HttpServletResponse servletResponse = this.wrapper.getServletResponse();
        try {
            String fileName = wrapper.getFileName();

            servletResponse.setContentType("application/x-xls");
            fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            servletResponse.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            this.export(exportor, Workbook.createWorkbook(servletResponse.getOutputStream()));
        } catch (Exception e) {
            throw new ExcelException(e);
        }
    }

}
