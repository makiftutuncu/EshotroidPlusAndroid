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
import android.support.annotation.Nullable;
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
    private RecyclerView recyclerView;
    private ActionButton searchActionButton;

    private ContentStates state;

    private BusListItemAdapter adapter;

    public static BusListFragment with(ActionButton searchActionButton) {
        BusListFragment fragment = new BusListFragment();
        fragment.setSearchActionButton(searchActionButton);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View layout = inflater.inflate(R.layout.fragment_bus_list, container, false);

        progressWidget = (ProgressWidget) layout.findViewById(R.id.progressWidget_busList);
        progressWidget.showProgress(true);

        swipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipeRefreshLayout_busList);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.primary), getResources().getColor(R.color.primaryDark));

        recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerView_busList);
        recyclerView.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastFirstVisibleItem = 0;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                final int currentFirstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if (currentFirstVisibleItem > this.lastFirstVisibleItem) {
                    searchActionButton.hide();
                } else if (currentFirstVisibleItem < this.lastFirstVisibleItem) {
                    searchActionButton.show();
                }

                this.lastFirstVisibleItem = currentFirstVisibleItem;
            }
        });

        searchActionButton.hide();

        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();

        new LoadBusListTask(getActivity(), new LoadBusListTask.OnBusListLoadedListener() {
            @Override
            public void onBusListLoaded(List<BusListItem> busList) {
                setBusList(busList);
            }
        }).execute();
    }

    public void setBusList(List<BusListItem> busList) {
        if (busList == null || busList.isEmpty()) {
            progressWidget.showError(true);
        } else {
            adapter = new BusListItemAdapter(busList);

            recyclerView.setAdapter(adapter);
            progressWidget.showContent(true);
            searchActionButton.show();
        }
        swipeRefreshLayout.setRefreshing(false);
        progressWidget.setEnabled(true);
    }

    @Override
    public void onRefresh() {
        new LoadBusListTask(getActivity(), new LoadBusListTask.OnBusListLoadedListener() {
            @Override
            public void onBusListLoaded(List<BusListItem> busList) {
                setBusList(busList);
            }
        }, true).execute();
    }

    public void setSearchActionButton(ActionButton searchActionButton) {
        this.searchActionButton = searchActionButton;
    }

    @Override
    public void changeStateTo(ContentStates newState) {
        switch (newState) {
            case LOADING:
            case ERROR:
            case CONTENT:
            default:
                break;
        }
    }
}
