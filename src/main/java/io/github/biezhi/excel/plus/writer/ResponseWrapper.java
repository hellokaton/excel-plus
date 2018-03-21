/**
 *  Copyright (c) 2018, biezhi 王爵 (biezhi.me@gmail.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.github.biezhi.excel.plus.writer;

import javax.servlet.http.HttpServletResponse;

/**
 * Used to wrap the HttpServletResponse and download file name
 *
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
