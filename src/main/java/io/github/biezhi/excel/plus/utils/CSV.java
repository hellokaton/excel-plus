/*
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
package io.github.biezhi.excel.plus.utils;

import java.io.InputStream;
import java.io.PushbackInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.BufferedReader;

import java.util.List;
import java.util.ArrayList;

public class CSV {

    static final private int  NUMMARK = 10;
    static final private char COMMA   = ',';
    static final private char DQUOTE  = '"';
    static final private char CRETURN = '\r';
    static final private char LFEED   = '\n';
    static final private char SQUOTE  = '\'';
    static final private char COMMENT = '#';

    /**
     * Should we ignore multiple carriage-return/newline characters
     * at the end of the record?
     */
    private boolean stripMultipleNewlines;

    /**
     * What should be used as the separator character?
     */
    private char              separator;
    private ArrayList<String> fields;
    private boolean           eofSeen;
    private Reader            in;

    private static Reader stripBom(InputStream in) throws java.io.IOException {
        PushbackInputStream pin = new PushbackInputStream(in, 3);
        byte[]              b   = new byte[3];
        int                 len = pin.read(b, 0, b.length);
        if ((b[0] & 0xFF) == 0xEF && len == 3) {
            if ((b[1] & 0xFF) == 0xBB &&
                    (b[2] & 0xFF) == 0xBF) {
                return new InputStreamReader(pin, "UTF-8");
            } else {
                pin.unread(b, 0, len);
            }
        } else if (len >= 2) {
            if ((b[0] & 0xFF) == 0xFE &&
                    (b[1] & 0xFF) == 0xFF) {
                return new InputStreamReader(pin, "UTF-16BE");
            } else if ((b[0] & 0xFF) == 0xFF &&
                    (b[1] & 0xFF) == 0xFE) {
                return new InputStreamReader(pin, "UTF-16LE");
            } else {
                pin.unread(b, 0, len);
            }
        } else if (len > 0) {
            pin.unread(b, 0, len);
        }
        return new InputStreamReader(pin, "UTF-8");
    }

    public CSV(boolean stripMultipleNewlines,
               char separator,
               Reader input) {
        this.stripMultipleNewlines = stripMultipleNewlines;
        this.separator = separator;
        this.fields = new ArrayList<>();
        this.eofSeen = false;
        this.in = new BufferedReader(input);
    }

    public CSV(boolean stripMultipleNewlines, char separator, InputStream input)
            throws java.io.IOException {
        this.stripMultipleNewlines = stripMultipleNewlines;
        this.separator = separator;
        this.fields = new ArrayList<>();
        this.eofSeen = false;
        this.in = new BufferedReader(stripBom(input));
    }

    public boolean hasNext() throws java.io.IOException {
        if (eofSeen) {
            return false;
        }
        fields.clear();
        eofSeen = split(in, fields);
        if (eofSeen) {
            return !fields.isEmpty();
        }
        return true;
    }

    public List<String> next() {
        return fields;
    }

    // Returns true if EOF seen.
    static private boolean discardLinefeed(Reader in,
                                           boolean stripMultiple)
            throws java.io.IOException {
        if (stripMultiple) {
            in.mark(NUMMARK);
            int value = in.read();
            while (value != -1) {
                char c = (char) value;
                if (c != CRETURN && c != LFEED) {
                    in.reset();
                    return false;
                } else {
                    in.mark(NUMMARK);
                    value = in.read();
                }
            }
            return true;
        } else {
            in.mark(NUMMARK);
            int value = in.read();
            if (value == -1) return true;
            else if ((char) value != LFEED) in.reset();
            return false;
        }
    }

    private boolean skipComment(Reader in) throws java.io.IOException {
        /* Discard line. */
        int value;
        while ((value = in.read()) != -1) {
            char c = (char) value;
            if (c == CRETURN)
                return discardLinefeed(in, stripMultipleNewlines);
        }
        return true;
    }

    // Returns true when EOF has been seen.
    private boolean split(Reader in, ArrayList<String> fields) throws java.io.IOException {
        StringBuilder sbuf = new StringBuilder();
        int           value;
        while ((value = in.read()) != -1) {
            char c = (char) value;
            switch (c) {
                case CRETURN:
                    if (sbuf.length() > 0) {
                        fields.add(sbuf.toString());
                        sbuf.delete(0, sbuf.length());
                    }
                    return discardLinefeed(in, stripMultipleNewlines);

                case LFEED:
                    if (sbuf.length() > 0) {
                        fields.add(sbuf.toString());
                        sbuf.delete(0, sbuf.length());
                    }
                    if (stripMultipleNewlines)
                        return discardLinefeed(in, stripMultipleNewlines);
                    else return false;

                case DQUOTE: {
                    // Processing double-quoted string ..
                    while ((value = in.read()) != -1) {
                        c = (char) value;
                        if (c == DQUOTE) {
                            // Saw another double-quote. Check if
                            // another char can be read.
                            in.mark(NUMMARK);
                            if ((value = in.read()) == -1) {
                                // Nope, found EOF; means End of
                                // field, End of record and End of
                                // File
                                if (sbuf.length() > 0) {
                                    fields.add(sbuf.toString());
                                    sbuf.delete(0, sbuf.length());
                                }
                                return true;
                            } else if ((c = (char) value) == DQUOTE) {
                                // Found a second double-quote
                                // character. Means the double-quote
                                // is included.
                                sbuf.append(DQUOTE);
                            } else if (c == CRETURN) {
                                // Found End of line. Means End of
                                // field, and End of record.
                                if (sbuf.length() > 0) {
                                    fields.add(sbuf.toString());
                                    sbuf.delete(0, sbuf.length());
                                }
                                // Read and discard a line-feed if we
                                // can indeed do so.
                                return discardLinefeed(in,
                                        stripMultipleNewlines);
                            } else if (c == LFEED) {
                                // Found end of line. Means End of
                                // field, and End of record.
                                if (sbuf.length() > 0) {
                                    fields.add(sbuf.toString());
                                    sbuf.delete(0, sbuf.length());
                                }
                                // No need to check further. At this
                                // point, we have not yet hit EOF, so
                                // we return false.
                                if (stripMultipleNewlines)
                                    return discardLinefeed(in, stripMultipleNewlines);
                                else return false;
                            } else {
                                // Not one of EOF, double-quote,
                                // newline or line-feed. Means end of
                                // double-quote processing. Does NOT
                                // mean end-of-field or end-of-record.
                                // System.err.println("EOR on '" + c +
                                // "'");
                                in.reset();
                                break;
                            }
                        } else {
                            // Not a double-quote, so no special meaning.
                            sbuf.append(c);
                        }
                    }
                    // Hit EOF, and did not see the terminating double-quote.
                    if (value == -1) {
                        // We ignore this error, and just add whatever
                        // left as the next field.
                        if (sbuf.length() > 0) {
                            fields.add(sbuf.toString());
                            sbuf.delete(0, sbuf.length());
                        }
                        return true;
                    }
                }
                break;

                default:
                    if (c == separator) {
                        fields.add(sbuf.toString());
                        sbuf.delete(0, sbuf.length());
                    } else {
                        /* A comment line is a line starting with '#' with
                         * optional whitespace at the start. */
                        if (c == COMMENT && fields.isEmpty() &&
                                sbuf.toString().trim().isEmpty()) {
                            boolean eof = skipComment(in);
                            if (eof) return eof;
                            else sbuf.delete(0, sbuf.length());
                            /* Continue with next line if not eof. */
                        } else sbuf.append(c);
                    }
            }
        }
        if (sbuf.length() > 0) {
            fields.add(sbuf.toString());
            sbuf.delete(0, sbuf.length());
        }
        return true;
    }
}