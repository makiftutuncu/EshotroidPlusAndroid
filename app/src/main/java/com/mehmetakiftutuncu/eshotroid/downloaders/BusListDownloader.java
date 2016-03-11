package com.mehmetakiftutuncu.eshotroid.downloaders;

import com.mehmetakiftutuncu.eshotroid.models.Bus;
import com.mehmetakiftutuncu.eshotroid.utilities.Conf;
import com.mehmetakiftutuncu.eshotroid.utilities.Log;
import com.mehmetakiftutuncu.eshotroid.utilities.Web;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BusListDownloader {
    private static final String TAG = "BusListDownloader";

    public static void download(final BusListDownloadListener busListDownloadListener) {
        Log.debug(TAG, "Downloading bus list...");

        Web.instance().get(Conf.Url.BUS_LIST, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.error(TAG, "Failed to download bus list, request failed with exception!", e);

                busListDownloadListener.onBusListDownloadFailed(Web.FailureType.REQUEST_FAILED_WITH_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (Web.isResponseSuccessfulAndJson(response)) {
                    String busListJsonAsString = response.body().string();

                    parse(busListJsonAsString, busListDownloadListener);
                } else {
                    Log.error(TAG, "Failed to download bus list, request was unsuccessful!");

                    busListDownloadListener.onBusListDownloadFailed(Web.FailureType.REQUEST_WAS_UNSUCCESSFUL);
                }
            }
        });
    }

    private static void parse(String busListJsonAsString, final BusListDownloadListener busListDownloadListener) {
        try {
            JSONObject json       = new JSONObject(busListJsonAsString);
            JSONArray busListJson = json.getJSONArray("success");
            JSONArray errorsJson  = json.optJSONArray("errors");

            if (errorsJson != null) {
                Log.error(TAG, "Eshotroid+ Server returned errors: " + errorsJson.toString());

                busListDownloadListener.onBusListDownloadFailed(Web.FailureType.SERVER_RETURNED_ERRORS);
            }

            ArrayList<Bus> busList = new ArrayList<>();

            for (int i = 0, length = busListJson.length(); i < length; i++) {
                JSONObject currentBusJson = busListJson.getJSONObject(i);

                int id           = currentBusJson.getInt("id");
                String departure = currentBusJson.getString("departure");
                String arrival   = currentBusJson.getString("arrival");

                Bus bus = new Bus(id, departure, arrival);

                busList.add(bus);
            }

            busListDownloadListener.onBusListDownloaded(busList);
        } catch (Throwable t) {
            Log.error(TAG, "Failed to parse bus list Json!", t);
            Log.error(TAG, "Json: " + busListJsonAsString);

            busListDownloadListener.onBusListDownloadFailed(Web.FailureType.SERVER_RETURNED_INVALID_DATA);
        }
    }

    public interface BusListDownloadListener {
        void onBusListDownloaded(ArrayList<Bus> busList);
        void onBusListDownloadFailed(Web.FailureType failureType);
    }
}
