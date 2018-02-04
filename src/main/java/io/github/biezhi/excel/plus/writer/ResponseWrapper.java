package io.github.biezhi.excel.plus.writer;

import javax.servlet.http.HttpServletResponse;

/**
 * @author biezhi
 * @date 2018/2/4
 */
public class ResponseWrapper {

    private HttpServletResponse servletResponse;
    private String              fileName;

    public ResponseWrapper(HttpServletResponse servletResponse, String fileName) {
        this.servletResponse = servletResponse;
        this.fileName = fileName;
    }

    public static ResponseWrapper create(HttpServletResponse servletResponse, String fileName) {
        return new ResponseWrapper(servletResponse, fileName);
    }

    public HttpServletResponse getServletResponse() {
        return servletResponse;
    }

    public String getFileName() {
        return fileName;
    }

}
