/*
 * Copyright (C) 2015 Mehmet Akif Tütüncü
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mehmetakiftutuncu.eshotroid.utilities;

/**
 * A utility class for basic String operations
 *
 * @author mehmetakiftutuncu
 */
public class StringUtils {
    /**
     * Checks whether or not given String is empty
     *
     * @param s String to check
     *
     * @return true if given String is not null and not empty or false otherwise
     */
    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
