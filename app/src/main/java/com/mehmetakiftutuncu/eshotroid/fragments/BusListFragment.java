package com.mehmetakiftutuncu.eshotroid.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mehmetakiftutuncu.eshotroid.R;
import com.mehmetakiftutuncu.eshotroid.adapters.BusListAdapter;
import com.mehmetakiftutuncu.eshotroid.database.Database;
import com.mehmetakiftutuncu.eshotroid.downloaders.BusListDownloader;
import com.mehmetakiftutuncu.eshotroid.models.Bus;
import com.mehmetakiftutuncu.eshotroid.models.ContentState;
import com.mehmetakiftutuncu.eshotroid.utilities.Log;
import com.mehmetakiftutuncu.eshotroid.utilities.Web;
import com.mehmetakiftutuncu.eshotroid.utilities.option.Option;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;

import ru.vang.progressswitcher.ProgressWidget;

public class BusListFragment
        extends FragmentWithContentState
        implements BusListDownloader.BusListDownloadListener, SwipeRefreshLayout.OnRefreshListener, BusListAdapter.OnBusSelectedListener {
    public static final String TAG = "BusListFragment";

    private ProgressWidget mProgressWidget;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FastScrollRecyclerView mRecyclerView;

    private ArrayList<Bus> busList;

    public BusListFragment() {}

    public static BusListFragment instance() {
        return new BusListFragment();
    }

    private void setBusList(ArrayList<Bus> busList) {
        this.busList = busList;
    }

    private void loadBusList() {
        Log.debug(TAG, "Loading bus list...");

        Option<ArrayList<Bus>> maybeBusListFromDatabase = Database.with(getContext()).getBusList();

        if (maybeBusListFromDatabase.isDefined) {
            ArrayList<Bus> busListFromDatabase = maybeBusListFromDatabase.get();

            if (!busListFromDatabase.isEmpty()) {
                Log.debug(TAG, "Loaded bus list from database!");

                setBusList(busListFromDatabase);
                changeStateTo(ContentState.DATA);

                return;
            }
        }

        BusListDownloader.download(this);
    }

    @Override public void initializeState() {
        if (busList == null || busList.isEmpty()) {
            changeStateTo(ContentState.LOADING);
            loadBusList();
        } else {
            changeStateTo(ContentState.DATA);
        }
    }

    @Override public void changeStateTo(final ContentState newState) {
        if (contentState != null && contentState.equals(newState)) {
            return;
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (newState) {
                    case LOADING:
                        mProgressWidget.showProgress(true);
                        break;

                    case DATA:
                        mProgressWidget.showContent(true);

                        mSwipeRefreshLayout.setRefreshing(false);
                        mRecyclerView.setAdapter(new BusListAdapter(busList, BusListFragment.this));
                        break;

                    case NO_DATA:
                        mProgressWidget.showEmpty(true);
                        break;

                    case ERROR:
                        mProgressWidget.showError(true);

                        final Snackbar snackbar = Snackbar.make(mSwipeRefreshLayout, "Failed to download bus list!", Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAction("OK", new View.OnClickListener() {
                            @Override public void onClick(View v) {
                                snackbar.dismiss();
                            }
                        }).show();

                        mSwipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("busList", busList);

        super.onSaveInstanceState(outState);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.debug(TAG, "onCreateView");

        View layout = inflater.inflate(R.layout.fragment_bus_list, container, false);

        mProgressWidget     = (ProgressWidget) layout.findViewById(R.id.progressWidget_busList);
        mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipeRefreshLayout_busList);
        mRecyclerView       = (FastScrollRecyclerView) layout.findViewById(R.id.recyclerView_busList);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (savedInstanceState != null) {
            setBusList(savedInstanceState.<Bus>getParcelableArrayList("busList"));
        } else {
            setBusList(new ArrayList<Bus>());
        }

        // Need to set an adapter because stupid FastScrollRecyclerView library doesn't do null check on adapter when using it to set up the scroller!
        mRecyclerView.setAdapter(new BusListAdapter(busList, this));

        initializeState();

        return layout;
    }

    @Override public void onBusListDownloaded(ArrayList<Bus> busList) {
        Log.debug(TAG, "Bus list is downloaded!");

        setBusList(busList);
        Database.with(getContext()).saveBusList(busList);

        changeStateTo(ContentState.DATA);
    }

    @Override public void onBusListDownloadFailed(Web.FailureType failureType) {
        changeStateTo(ContentState.ERROR);
    }

    @Override public void onRefresh() {
        changeStateTo(ContentState.LOADING);

        BusListDownloader.download(this);
    }

    @Override public void onBusSelected(Bus bus) {
        Log.debug(TAG, "Bus " + bus + " is selected!");
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        Log.debug(TAG, "onCreate");

        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }
}
