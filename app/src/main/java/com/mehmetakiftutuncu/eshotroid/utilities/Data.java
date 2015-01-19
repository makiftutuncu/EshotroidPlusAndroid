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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mehmetakiftutuncu.eshotroid.models.BusListItem;

import java.util.ArrayList;
import java.util.List;

public class Data {
    public static List<BusListItem> loadBusList() {
        Log.info(Data.class, "Loading bus list");

        if (FileUtils.dataPath == null) {
            Log.error(Data.class, "Failed to load bus list, data path is null!");
            return null;
        } else {
            String fileName = "bus.list";
            String data = FileUtils.readFile(fileName);

            if (StringUtils.isEmpty(data)) {
                Log.error(Data.class, "Failed to load bus list, loaded data is empty!");
                return null;
            } else {
                Gson gson = new Gson();
                List<BusListItem> busList = gson.fromJson(data, new TypeToken<ArrayList<BusListItem>>(){}.getType());

                if (busList == null || busList.isEmpty()) {
                    Log.error(Data.class, "Failed to load bus list, bus list object is empty!");
                    return null;
                } else {
                    return busList;
                }
            }
        }
    }

    public static boolean saveBusList(List<BusListItem> busList) {
        Log.info(Data.class, "Saving bus list: " + busList);

        if (busList == null) {
            Log.error(Data.class, "Failed to save bus list, bus list object is null!");
            return false;
        } else {
            if (FileUtils.dataPath == null) {
                Log.error(Data.class, "Failed to save bus list, data path is null! busList: " + busList);
                return false;
            } else {
                Gson gson = new Gson();
                String data = gson.toJson(busList, new TypeToken<ArrayList<BusListItem>>(){}.getType());

                return FileUtils.writeFile(data, "bus.list");
            }
        }
    }
}
