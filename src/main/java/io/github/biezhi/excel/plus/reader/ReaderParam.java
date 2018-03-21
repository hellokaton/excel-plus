package io.github.biezhi.excel.plus.reader;

import io.github.biezhi.excel.plus.enums.ParseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

/**
 * @author biezhi
 * @date 2018/3/20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReaderParam {

    private String sheetName;

    @Builder.Default
    private int sheetIndex = 0;

    @Builder.Default
    private int startRowIndex = 1;

    @Builder.Default
    private ParseType parseType = ParseType.DOM;

    private File excelFile;

}
