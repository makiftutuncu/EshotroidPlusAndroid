package com.mehmetakiftutuncu.eshotroid.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Web {
    private static Web instance;
    private OkHttpClient client;

    private Web() {
        client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
    }

    public static Web instance() {
        if (instance == null) {
            instance = new Web();
        }

        return instance;
    }

    public void get(String url, final Callback requestCallbackListener) {
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(requestCallbackListener);
    }

    public static boolean hasInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean isResponseSuccessfulAndJson(Response response) {
        return response != null && response.isSuccessful() && response.header("Content-Type", "").contains("application/json");
    }

    public enum FailureType {
        REQUEST_FAILED_WITH_EXCEPTION,
        REQUEST_WAS_UNSUCCESSFUL,
        SERVER_RETURNED_ERRORS,
        SERVER_RETURNED_INVALID_DATA
    }
}
