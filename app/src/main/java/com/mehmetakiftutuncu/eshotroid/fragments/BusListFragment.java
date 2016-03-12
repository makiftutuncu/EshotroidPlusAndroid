package com.mehmetakiftutuncu.eshotroid.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mehmetakiftutuncu.eshotroid.R;
import com.mehmetakiftutuncu.eshotroid.adapters.BusListAdapter;
import com.mehmetakiftutuncu.eshotroid.database.Database;
import com.mehmetakiftutuncu.eshotroid.models.Bus;
import com.mehmetakiftutuncu.eshotroid.downloaders.BusListDownloader;
import com.mehmetakiftutuncu.eshotroid.utilities.Log;
import com.mehmetakiftutuncu.eshotroid.utilities.Web;
import com.mehmetakiftutuncu.eshotroid.utilities.option.Option;
import com.turingtechnologies.materialscrollbar.CustomIndicator;
import com.turingtechnologies.materialscrollbar.DragScrollBar;

import java.util.ArrayList;

public class BusListFragment extends Fragment implements BusListDownloader.BusListDownloadListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "BusListFragment";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private BusListAdapter busListAdapter;
    private LinearLayoutManager linearLayoutManager;

    public BusListFragment() {}

    public static BusListFragment instance() {
        return new BusListFragment();
    }

    private void loadBusList() {
        Log.debug(TAG, "Loading bus list...");

        Option<ArrayList<Bus>> maybeBusListFromDatabase = Database.with(getContext()).getBusList();

        if (maybeBusListFromDatabase.isDefined) {
            ArrayList<Bus> busListFromDatabase = maybeBusListFromDatabase.get();

            if (!busListFromDatabase.isEmpty()) {
                Log.debug(TAG, "Loaded bus list from database!");

                setBusList(busListFromDatabase);

                return;
            }
        }

        BusListDownloader.download(this);
    }

    private void setBusList(ArrayList<Bus> busList) {
        busListAdapter = new BusListAdapter(busList);
        linearLayoutManager = new LinearLayoutManager(getContext());

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                mRecyclerView.setLayoutManager(linearLayoutManager);
                mRecyclerView.setAdapter(busListAdapter);
            }
        });
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

        super.onResume();
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        Log.debug(TAG, "onCreate");

        super.onCreate(savedInstanceState);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.debug(TAG, "onCreateView");

        View layout = inflater.inflate(R.layout.fragment_bus_list, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipeRefreshLayout_busList);
        mRecyclerView       = (RecyclerView) layout.findViewById(R.id.recyclerView_busList);

        DragScrollBar materialScrollBar = (DragScrollBar) layout.findViewById(R.id.dragScrollBar_busList);
        materialScrollBar.addIndicator(new CustomIndicator(getContext()), true);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    mSwipeRefreshLayout.setEnabled(true);
                } else {
                    mSwipeRefreshLayout.setEnabled(false);
                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(this);

        loadBusList();

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
        Log.debug(TAG, "Bus list is downloaded!");

        Database.with(getContext()).saveBusList(busList);

        setBusList(busList);
    }

    @Override public void onBusListDownloadFailed(Web.FailureType failureType) {
        final Snackbar snackbar = Snackbar.make(mSwipeRefreshLayout, "Failed to download bus list!", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override public void onClick(View v) {
                snackbar.dismiss();
            }
        }).show();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override public void onRefresh() {
        BusListDownloader.download(this);
    }
}
