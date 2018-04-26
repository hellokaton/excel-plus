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
package io.github.biezhi.excel.plus.model;

import io.github.biezhi.excel.plus.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author biezhi
 * @date 2018/4/25
 */
@Data
public class UserRequestModel implements Serializable {

    @ExcelField(order = 0)
    private String A;

    @ExcelField(order = 1)
    private String B;

    @ExcelField(order = 2)
    private String C;

    @ExcelField(order = 3)
    private String D;

    @ExcelField(order = 4)
    private String E;

    @ExcelField(order = 5)
    private String F;

    @ExcelField(order = 6)
    private String G;

    @ExcelField(order = 7)
    private String H;

    @ExcelField(order = 8)
    private String I;

    @ExcelField(order = 9)
    private String J;

    @ExcelField(order = 10)
    private String K;

    @ExcelField(order = 11)
    private String L;

}
