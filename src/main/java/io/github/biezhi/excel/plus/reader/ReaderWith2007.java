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
package io.github.biezhi.excel.plus.reader;

import io.github.biezhi.excel.plus.Reader;
import io.github.biezhi.excel.plus.exception.ReaderException;
import io.github.biezhi.excel.plus.util.StringUtil;
import org.apache.poi.ooxml.util.SAXHelper;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.Styles;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

/**
 * Read 2007 Excel
 *
 * @author biezhi
 * @date 2018-12-11
 */
public class ReaderWith2007 implements ExcelReader {

    public ReaderWith2007(Workbook workbook) {
        //
    }

    public <T> Stream<T> readExcel(Reader reader) throws ReaderException {
        Class<T> type = reader.modelType();
        try {
            // The package open is instantaneous, as it should be.
            try (OPCPackage p = getPackage(reader)) {

                SheetToCSV<T> sheetToCSV = new SheetToCSV<>(p, reader.startRow(), type);

                this.process(reader, sheetToCSV);

                Stream.Builder<T> stream = sheetToCSV.getRowsStream();
                return stream.build();
            }
        } catch (Exception e) {
            throw new ReaderException(e);
        }
    }

    private OPCPackage getPackage(Reader reader) throws Exception {
        if (reader.fromFile() != null) {
            return OPCPackage.open(reader.fromFile(), PackageAccess.READ);
        } else {
            return OPCPackage.open(reader.fromStream());
        }
    }

    /**
     * Initiates the processing of the XLS workbook file to CSV.
     *
     * @throws IOException  If reading the data from the package fails.
     * @throws SAXException if parsing the XML data fails.
     */
    public void process(Reader reader, SheetToCSV sheetToCSV) throws IOException, OpenXML4JException, SAXException {
        ReadOnlySharedStringsTable strings    = new ReadOnlySharedStringsTable(sheetToCSV.getOpcPackage());
        XSSFReader                 xssfReader = new XSSFReader(sheetToCSV.getOpcPackage());
        StylesTable                styles     = xssfReader.getStylesTable();
        XSSFReader.SheetIterator   iter       = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        int                        index      = 0;

        boolean bySheetName = StringUtil.isNotEmpty(reader.sheetName());

        while (iter.hasNext()) {
            try (InputStream stream = iter.next()) {
                String sheetName = iter.getSheetName();
                if (bySheetName && reader.sheetName().equals(sheetName)) {
                    processSheet(styles, strings, sheetToCSV, stream);
                    break;
                }
                if (!bySheetName && reader.sheetIndex() == index) {
                    processSheet(styles, strings, sheetToCSV, stream);
                    break;
                }
            }
            ++index;
        }
    }

    /**
     * Parses and shows the content of one sheet
     * using the specified styles and shared-strings tables.
     *
     * @param styles           The table of styles that may be referenced by cells in the sheet
     * @param strings          The table of strings that may be referenced by cells in the sheet
     * @param sheetInputStream The stream to read the sheet-data from.
     * @throws java.io.IOException An IO exception from the parser,
     *                             possibly from a byte stream or character stream
     *                             supplied by the application.
     * @throws SAXException        if parsing the XML data fails.
     */
    public void processSheet(
            Styles styles,
            SharedStrings strings,
            XSSFSheetXMLHandler.SheetContentsHandler sheetHandler,
            InputStream sheetInputStream) throws IOException, SAXException {

        DataFormatter formatter   = new XLSXDataFormatter();
        InputSource   sheetSource = new InputSource(sheetInputStream);

        try {
            XMLReader sheetParser = SAXHelper.newXMLReader();
            ContentHandler handler = new XSSFSheetXMLHandler(
                    styles, null, strings, sheetHandler, formatter, false);
            sheetParser.setContentHandler(handler);
            sheetParser.parse(sheetSource);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("SAX parser appears to be broken - " + e.getMessage());
        }
    }

}
