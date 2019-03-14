/*
 *  Copyright (c) 2018, biezhi <biezhi.me@gmail.com>
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
package io.github.biezhi.excel.plus.types;

/**
 * Valid
 * <p>
 * Used to verify compliance with rules when reading excel lines
 *
 * @author biezhi
 * @date 2019-03-14
 */
public class Valid {

    /**
     * Current row verification success identifier
     */
    private boolean success;

    /**
     * Prompt message when verification fails
     */
    private String msg;

    private Valid() {
    }

    public boolean isSuccess() {
        return success;
    }

    public String msg() {
        return msg;
    }

    public static Valid ok() {
        Valid valid = new Valid();
        valid.success = true;
        return valid;
    }

    public static Valid error(String msg) {
        Valid valid = new Valid();
        valid.success = false;
        valid.msg = msg;
        return valid;
    }

}
