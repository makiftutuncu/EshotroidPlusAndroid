package com.mehmetakiftutuncu.eshotroid.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mehmetakiftutuncu.eshotroid.R;
import com.mehmetakiftutuncu.eshotroid.adapters.BusListAdapter;
import com.mehmetakiftutuncu.eshotroid.models.Bus;
import com.mehmetakiftutuncu.eshotroid.data.BusListDownloader;
import com.mehmetakiftutuncu.eshotroid.utilities.Log;
import com.mehmetakiftutuncu.eshotroid.utilities.Web;

import java.util.ArrayList;

public class BusListFragment extends Fragment implements BusListDownloader.BusListDownloadListener {
    private static final String TAG = "BusListFragment";

    private RecyclerView mRecyclerView;
    private BusListAdapter busListAdapter;
    private LinearLayoutManager linearLayoutManager;

    public BusListFragment() {}

    public static BusListFragment instance() {
        return new BusListFragment();
    }

    @Override public void onStart() {
        Log.debug(TAG, "onStart");

        super.onStart();
    }

    @Override public void onPause() {
        Log.debug(TAG, "onPause");

        super.onPause();
    }

    @Override public void onStop() {
        Log.debug(TAG, "onStop");

        super.onStop();
    }

    @Override public void onResume() {
        Log.debug(TAG, "onResume");

        BusListDownloader.download(this);

        super.onResume();
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        Log.debug(TAG, "onCreate");

        super.onCreate(savedInstanceState);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.debug(TAG, "onCreateView");

        View layout = inflater.inflate(R.layout.fragment_bus_list, container, false);

        mRecyclerView = (RecyclerView) layout.findViewById(R.id.recyclerView_busList);

        return layout;
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        Log.debug(TAG, "onActivityCreated");

        super.onActivityCreated(savedInstanceState);
    }

    @Override public void onDetach() {
        Log.debug(TAG, "onDetach");

        super.onDetach();
    }

    @Override public void onAttach(Context context) {
        Log.debug(TAG, "onAttach");

        super.onAttach(context);
    }

    @Override public void onBusListDownloaded(ArrayList<Bus> busList) {
        busListAdapter = new BusListAdapter(busList);
        linearLayoutManager = new LinearLayoutManager(getContext());

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.setLayoutManager(linearLayoutManager);
                mRecyclerView.setAdapter(busListAdapter);
            }
        });
    }

    @Override public void onBusListDownloadFailed(Web.FailureType failureType) {

    }
}
