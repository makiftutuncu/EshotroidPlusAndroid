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
package com.mehmetakiftutuncu.eshotroid.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mehmetakiftutuncu.eshotroid.R;
import com.mehmetakiftutuncu.eshotroid.adapters.BusListItemAdapter;
import com.mehmetakiftutuncu.eshotroid.interfaces.WithContentStates;
import com.mehmetakiftutuncu.eshotroid.models.BusListItem;
import com.mehmetakiftutuncu.eshotroid.models.ContentStates;
import com.mehmetakiftutuncu.eshotroid.tasks.LoadBusListTask;
import com.software.shell.fab.ActionButton;

import java.util.List;

import ru.vang.progressswitcher.ProgressWidget;

public class BusListFragment extends Fragment implements WithContentStates, SwipeRefreshLayout.OnRefreshListener {
    private ProgressWidget progressWidget;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActionButton searchActionButton;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        int lastFirstVisibleItem = 0;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            final int currentFirstVisibleItem = layoutManager != null ? layoutManager.findFirstVisibleItemPosition() : 0;

            if (currentFirstVisibleItem > this.lastFirstVisibleItem) {
                showHideSearchActionButton(false);
            } else if (currentFirstVisibleItem < this.lastFirstVisibleItem) {
                showHideSearchActionButton(true);
            }

            this.lastFirstVisibleItem = currentFirstVisibleItem;
        }
    };

    private LoadBusListTask.OnBusListLoadedListener onBusListLoadedListener = new LoadBusListTask.OnBusListLoadedListener() {
        @Override
        public void onBusListLoaded(List<BusListItem> busList) {
            setBusList(busList);
        }
    };

    private ContentStates state;

    private BusListItemAdapter adapter;

    public static BusListFragment with(ActionButton searchActionButton) {
        BusListFragment fragment = new BusListFragment();
        fragment.setSearchActionButton(searchActionButton);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View layout = inflater.inflate(R.layout.fragment_bus_list, container, false);

        progressWidget = (ProgressWidget) layout.findViewById(R.id.progressWidget_busList);

        swipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipeRefreshLayout_busList);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.primary), getResources().getColor(R.color.primaryDark));

        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerView_busList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setOnScrollListener(onScrollListener);
        recyclerView.setLayoutManager(layoutManager);

        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();

        loadBusList(false);
    }

    public void setBusList(List<BusListItem> busList) {
        if (busList == null || busList.isEmpty()) {
            changeStateTo(ContentStates.ERROR);
        } else {
            adapter = new BusListItemAdapter(busList);
            recyclerView.setAdapter(adapter);

            changeStateTo(ContentStates.CONTENT);
        }
    }

    @Override
    public void onRefresh() {
        loadBusList(true);
    }

    public void setSearchActionButton(ActionButton searchActionButton) {
        this.searchActionButton = searchActionButton;
    }

    @Override
    public void changeStateTo(ContentStates newState) {
        if (state == null || !state.equals(newState)) {
            state = newState;

            switch (newState) {
                case LOADING:
                    progressWidget.showProgress(true);
                    showHideSearchActionButton(false);
                    setRefreshing(false);
                    break;
                case REFRESHING:
                    showHideSearchActionButton(false);
                    setRefreshing(true);
                    break;
                case ERROR:
                    progressWidget.showError(true);
                    showHideSearchActionButton(false);
                    setRefreshing(false);
                    break;
                case CONTENT:
                    progressWidget.showContent(true);
                    showHideSearchActionButton(true);
                    setRefreshing(false);
                    break;
                case NO_CONTENT:
                    progressWidget.showEmpty(true);
                    showHideSearchActionButton(false);
                    setRefreshing(false);
                    break;
            }
        }
    }

    public void search(String query) {
        if (adapter != null) {
            adapter.search(query);

            changeStateTo(adapter.getItemCount() <= 0 ? ContentStates.NO_CONTENT : ContentStates.CONTENT);
        }
    }

    public boolean isRefreshing() {
        return state != null && state.equals(ContentStates.REFRESHING);
    }

    private void loadBusList(boolean forceDownload) {
        if (forceDownload) {
            new LoadBusListTask(getActivity(), onBusListLoadedListener, true).execute();
            changeStateTo(ContentStates.REFRESHING);
        } else {
            new LoadBusListTask(getActivity(), onBusListLoadedListener).execute();
            changeStateTo(ContentStates.LOADING);
        }
    }

    private void showHideSearchActionButton(boolean show) {
        if (searchActionButton != null) {
            if (show) {
                searchActionButton.show();
            } else {
                searchActionButton.hide();
            }
        }
    }

    private void setRefreshing(boolean isRefreshing) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(isRefreshing);
        }
    }
}
