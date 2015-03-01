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
package com.mehmetakiftutuncu.eshotroid.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.mehmetakiftutuncu.eshotroid.R;
import com.mehmetakiftutuncu.eshotroid.fragments.BusListFragment;
import com.mehmetakiftutuncu.eshotroid.interfaces.WithToolbar;
import com.mehmetakiftutuncu.eshotroid.models.ContentLayoutTypes;
import com.software.shell.fab.ActionButton;

public class BusListActivity extends ActionBarActivity implements WithToolbar {
    private Toolbar toolbar;
    private ActionButton searchActionButton;

    private ContentLayoutTypes layoutType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_list);

        initializeToolbar();
        searchActionButton = (ActionButton) findViewById(R.id.actionButton_busList_search);

        FrameLayout busListContainer = (FrameLayout) findViewById(R.id.frameLayout_busListContainer);
        FrameLayout busDetailsContainer = (FrameLayout) findViewById(R.id.frameLayout_busDetailsContainer);
        if (busDetailsContainer != null && busDetailsContainer.getVisibility() == View.VISIBLE) {
            layoutType = ContentLayoutTypes.MASTER_DETAIL;
        } else {
            layoutType = ContentLayoutTypes.NORMAL;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        final BusListFragment busListFragment = BusListFragment.with(searchActionButton);
        fragmentManager.beginTransaction().replace(R.id.frameLayout_busListContainer, busListFragment).commit();
    }

    @Override
    public void initializeToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }
}
