/**
 * Copyright (c) 2018, biezhi (biezhi.me@gmail.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.biezhi.excel.plus.util;

import io.github.biezhi.excel.plus.exception.ReaderException;
import lombok.experimental.UtilityClass;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Excel Utils
 *
 * @author biezhi
 * @date 2018-12-11
 */
@UtilityClass
public class ExcelUtil {

    public static <T> T newInstance(Class<T> type) {
        try {
            return type.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public static Workbook create(File file) throws ReaderException {
        try {
            return WorkbookFactory.create(file);
        } catch (IOException e) {
            throw new ReaderException(e);
        }
    }

    public static Workbook create(InputStream inputStream) throws ReaderException {
        try {
            return WorkbookFactory.create(inputStream);
        } catch (IOException e) {
            throw new ReaderException(e);
        }
    }

    public static String getFileExtension(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return fileName.substring(lastIndexOf + 1);
    }

    public static boolean isXLSX(File file) {
        if(null == file || !file.exists()){
            return false;
        }
        String ext = getFileExtension(file.getName());
        return ext.toUpperCase().equals("XLSX");
    }

    public static boolean isXLS(File file) {
        if(null == file || !file.exists()){
            return false;
        }
        String ext = getFileExtension(file.getName());
        return ext.toUpperCase().equals("XLS");
    }

    public static boolean isCSV(File file) {
        if(null == file || !file.exists()){
            return false;
        }
        String ext = getFileExtension(file.getName());
        return ext.toUpperCase().equals("CSV");
    }

    public static boolean isXLSX(InputStream inputStream) {
        try {
            new XSSFWorkbook(inputStream);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isXLS(InputStream inputStream) {
        try {
            new HSSFWorkbook(inputStream);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
