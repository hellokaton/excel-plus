package io.github.biezhi.excel.plus.writer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author biezhi
 * @date 2018/3/20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WriterParam {

    private String sheetName;
    @Builder.Default
    private int       sheetIndex    = -1;
    @Builder.Default
    private int       startRowIndex = 1;

}
