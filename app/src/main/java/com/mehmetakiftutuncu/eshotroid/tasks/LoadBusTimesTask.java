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
package com.mehmetakiftutuncu.eshotroid.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.mehmetakiftutuncu.eshotroid.models.BusListItem;
import com.mehmetakiftutuncu.eshotroid.utilities.Data;
import com.mehmetakiftutuncu.eshotroid.utilities.Log;
import com.mehmetakiftutuncu.eshotroid.utilities.NetworkUtils;
import com.mehmetakiftutuncu.eshotroid.utilities.Request;
import com.mehmetakiftutuncu.eshotroid.utilities.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoadBusTimesTask extends AsyncTask<Void, Void, List<BusListItem>> {
    private Context context;
    private OnBusListLoadedListener listener;
    private boolean forceDownload;

    public LoadBusTimesTask(Context context, OnBusListLoadedListener listener) {
        this(context, listener, false);
    }

    public LoadBusTimesTask(Context context, OnBusListLoadedListener listener, boolean forceDownload) {
        this.context = context;
        this.listener = listener;
        this.forceDownload = forceDownload;
    }

    @Override
    protected List<BusListItem> doInBackground(Void... params) {
        if (!forceDownload) {
            // Load data from disk
            List<BusListItem> busListFromData = Data.loadBusList();

            if (busListFromData != null && !busListFromData.isEmpty()) {
                // Data found on disk
                return busListFromData;
            }
        }

        return download();
    }

    @Override
    protected void onPostExecute(List<BusListItem> busList) {
        super.onPostExecute(busList);

        if (listener == null) {
            Log.error(this, "Failed to load bus list, listener is null!");
        } else {
            listener.onBusListLoaded(busList);
        }
    }

    private List<BusListItem> download() {
        if (!NetworkUtils.isConnectedToInternet(context)) {
            Log.error(this, "Failed to load bus list, not connected to internet!");
            return null;
        } else {
            // Data not found on disk

            // Download data from server
            try {
                String busListJSONString = Request.get(URL.busList());

                JSONObject busListJSON = new JSONObject(busListJSONString);
                JSONArray busListJSONArray = busListJSON.getJSONArray("buses");

                List<BusListItem> busList = new ArrayList<>();

                for (int i = 0; i < busListJSONArray.length(); i++) {
                    BusListItem item = BusListItem.fromJson(busListJSONArray.getJSONObject(i).toString());
                    busList.add(item);
                }

                Data.saveBusList(busList);

                return busList;
            } catch (Exception e) {
                Log.error(this, "Failed to load bus list!", e);
                return null;
            }
        }
    }

    public interface OnBusListLoadedListener {
        public void onBusListLoaded(List<BusListItem> busList);
    }
}
