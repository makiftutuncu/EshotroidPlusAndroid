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
package com.mehmetakiftutuncu.eshotroid.models;

/**
 * Possible types of content layout types
 *
 * This will be determined by screen size and density of device on which Eshotroid runs
 *
 * @author mehmetakiftutuncu
 */
public enum ContentLayoutTypes {
    /** Content is shown in normal layout */
    NORMAL,
    /** Content is shown in two columns as master detail flow */
    MASTER_DETAIL
}
