package com.mehmetakiftutuncu.eshotroid.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.mehmetakiftutuncu.eshotroid.R;
import com.mehmetakiftutuncu.eshotroid.activities.HomeActivity;
import com.mehmetakiftutuncu.eshotroid.adapters.BusListAdapter;
import com.mehmetakiftutuncu.eshotroid.database.Database;
import com.mehmetakiftutuncu.eshotroid.downloaders.BusListDownloader;
import com.mehmetakiftutuncu.eshotroid.models.Bus;
import com.mehmetakiftutuncu.eshotroid.models.ContentState;
import com.mehmetakiftutuncu.eshotroid.utilities.Log;
import com.mehmetakiftutuncu.eshotroid.utilities.Web;
import com.mehmetakiftutuncu.eshotroid.utilities.option.Option;
import com.pluscubed.recyclerfastscroll.RecyclerFastScroller;

import java.util.ArrayList;

import ru.vang.progressswitcher.ProgressWidget;
import xyz.hanks.library.SmallBang;

public class BusListFragment
        extends FragmentWithContentState
        implements BusListDownloader.BusListDownloadListener,
                   SwipeRefreshLayout.OnRefreshListener,
                   BusListAdapter.OnBusSelectedListener {
    public static final String TAG = "BusListFragment";

    private ProgressWidget mProgressWidget;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private ArrayList<Bus> busList;
    private boolean isSearching;

    private LinearLayoutManager mLinearLayoutManager;
    private BusListAdapter mBusListAdapter;

    private RecyclerView.OnScrollListener mOnRecyclerViewScrollListener = new RecyclerView.OnScrollListener() {
        int lastFirstVisibleItem = 0;

        @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            final int currentFirstVisibleItem    = mLinearLayoutManager != null ? mLinearLayoutManager.findFirstVisibleItemPosition() : 0;
            final int firstCompletelyVisibleItem = mLinearLayoutManager != null ? mLinearLayoutManager.findFirstCompletelyVisibleItemPosition() : 0;

            if (!isSearching) {
                if (currentFirstVisibleItem > this.lastFirstVisibleItem) {
                    toggleFloatingSearchButton(false);
                } else if (currentFirstVisibleItem < this.lastFirstVisibleItem) {
                    toggleFloatingSearchButton(true);
                }

                if (firstCompletelyVisibleItem == 0) {
                    mSwipeRefreshLayout.setEnabled(true);
                } else {
                    mSwipeRefreshLayout.setEnabled(false);
                }
            }

            this.lastFirstVisibleItem = currentFirstVisibleItem;
        }
    };

    public BusListFragment() {}

    public static BusListFragment instance() {
        return new BusListFragment();
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
                        mBusListAdapter = new BusListAdapter(busList, BusListFragment.this);
                        mRecyclerView.setAdapter(mBusListAdapter);
                        break;

                    case NO_DATA:
                        mProgressWidget.showEmpty(true);
                        break;

                    case ERROR:
                        mProgressWidget.showError(true);

                        final Snackbar snackbar = Snackbar.make(mSwipeRefreshLayout, "Failed to download bus list!", Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAction("OK", new View.OnClickListener() {
                            @Override public void onClick(View v) {}
                        }).show();

                        mSwipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("busList", busList);
        outState.putBoolean("isSearching", isSearching);

        super.onSaveInstanceState(outState);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.debug(TAG, "onCreateView");

        View layout = inflater.inflate(R.layout.fragment_bus_list, container, false);

        mProgressWidget     = (ProgressWidget) layout.findViewById(R.id.progressWidget_busList);
        mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipeRefreshLayout_busList);
        mRecyclerView       = (RecyclerView) layout.findViewById(R.id.recyclerView_busList);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        if (savedInstanceState != null) {
            setBusList(savedInstanceState.<Bus>getParcelableArrayList("busList"));
            mSwipeRefreshLayout.setEnabled(!savedInstanceState.getBoolean("isSearching"));
        } else {
            setBusList(new ArrayList<Bus>());
        }

        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mBusListAdapter = new BusListAdapter(busList, this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mBusListAdapter);

        mRecyclerView.addOnScrollListener(mOnRecyclerViewScrollListener);

        RecyclerFastScroller recyclerFastScroller = (RecyclerFastScroller) layout.findViewById(R.id.recyclerFastScroller_busList);
        recyclerFastScroller.attachRecyclerView(mRecyclerView);

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
        Log.debug(TAG, "Bus %d is selected!", bus.id);
    }

    @Override public void onBusStarSelected(final CheckBox starView, final Bus bus) {
        if (starView.isChecked() ^ bus.isStarred) {
            bus.isStarred = !bus.isStarred;

            if (bus.isStarred) {
                Log.debug(TAG, "Bus %d is starred!", bus.id);

                // TODO Instead of simply updating, get all data for the bus, save them to DB and call this on success.
                Database.with(getContext()).updateBus(bus);
            } else {
                Log.debug(TAG, "Bus %d is un-starred!", bus.id);
            }

            SmallBang smallBang = SmallBang.attach2Window(getActivity());
            smallBang.bang(starView);
        }
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        Log.debug(TAG, "onCreate");

        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    public void search(String query) {
        mBusListAdapter.search(query);

        if (mBusListAdapter.getItemCount() <= 0) {
            mProgressWidget.showEmpty(true);
        } else {
            mProgressWidget.showContent(true);
        }
    }

    public void setSearching(boolean isSearching) {
        this.isSearching = isSearching;

        if (isSearching) {
            mSwipeRefreshLayout.setEnabled(false);
        }
    }

    public void toggleFloatingSearchButton(boolean show) {
        FloatingActionButton floatingSearchButton = ((HomeActivity) getActivity()).getFloatingActionButton();

        if (show) {
            floatingSearchButton.show();
        } else {
            floatingSearchButton.hide();
        }
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
}
