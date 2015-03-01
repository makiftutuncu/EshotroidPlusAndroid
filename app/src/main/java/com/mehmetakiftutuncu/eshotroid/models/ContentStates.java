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
 * Possible states of content that needs to be loaded/downloaded then shown to user
 *
 * @author mehmetakiftutuncu
 */
public enum ContentStates {
    /** Content is currently being loaded/downloaded */
    LOADING,
    /** Content is currently being refreshed */
    REFRESHING,
    /** Content is successfully loaded/downloaded */
    CONTENT,
    /** Content is loaded/downloaded but it is empty */
    NO_CONTENT,
    /** Loading/downloading content failed */
    ERROR
}
