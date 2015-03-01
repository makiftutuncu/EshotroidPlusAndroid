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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.mehmetakiftutuncu.eshotroid.R;
import com.mehmetakiftutuncu.eshotroid.fragments.BusListFragment;
import com.mehmetakiftutuncu.eshotroid.interfaces.WithToolbar;
import com.mehmetakiftutuncu.eshotroid.models.ContentLayoutTypes;
import com.software.shell.fab.ActionButton;

public class BusListActivity extends ActionBarActivity implements WithToolbar, View.OnClickListener {
    private Toolbar toolbar;
    private RelativeLayout toolbarSearchLayout;
    private ImageButton toolbarBackButton;
    private ImageButton toolbarClearButton;
    private EditText toolbarSearchEditText;
    private ActionButton searchActionButton;

    private TextWatcher searchTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (busListFragment != null) {
                busListFragment.search(s.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private FrameLayout busListContainer;
    private FrameLayout busDetailsContainer;

    private BusListFragment busListFragment;

    private ContentLayoutTypes layoutType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_list);

        initializeToolbar();
        searchActionButton = (ActionButton) findViewById(R.id.actionButton_busList_search);
        searchActionButton.setOnClickListener(this);

        busListContainer = (FrameLayout) findViewById(R.id.frameLayout_busListContainer);
        busDetailsContainer = (FrameLayout) findViewById(R.id.frameLayout_busDetailsContainer);

        if (busDetailsContainer != null && busDetailsContainer.getVisibility() == View.VISIBLE) {
            layoutType = ContentLayoutTypes.MASTER_DETAIL;
        } else {
            layoutType = ContentLayoutTypes.NORMAL;
        }

        busListFragment = BusListFragment.with(searchActionButton);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameLayout_busListContainer, busListFragment).commit();
    }

    @Override
    public void initializeToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbarSearchLayout   = (RelativeLayout) findViewById(R.id.relativeLayout_toolbar_searchLayout);
        toolbarBackButton     = (ImageButton) findViewById(R.id.imageButton_toolbar_searchBack);
        toolbarClearButton    = (ImageButton) findViewById(R.id.imageButton_toolbar_searchClear);
        toolbarSearchEditText = (EditText) findViewById(R.id.editText_toolbar_search);
        toolbarSearchEditText.addTextChangedListener(searchTextWatcher);

        toolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchToolbar(false);
            }
        });

        toolbarClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarSearchEditText.setText("");
            }
        });
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void showSearchToolbar(boolean show) {
        if (toolbarSearchLayout != null) {
            if (show) {
                toolbarSearchLayout.setVisibility(View.VISIBLE);
                toolbarSearchEditText.requestFocus();

                if (searchActionButton != null) {
                    searchActionButton.hide();
                }
            } else {
                toolbarSearchLayout.setVisibility(View.GONE);
                toolbarSearchEditText.clearFocus();

                if (searchActionButton != null) {
                    searchActionButton.show();
                }
            }
        }
    }

    public boolean isTabletMode() {
        return layoutType != null && layoutType.equals(ContentLayoutTypes.MASTER_DETAIL);
    }

    @Override
    public void onClick(View v) {
        if (v != null && v.getId() == (searchActionButton != null ? searchActionButton.getId() : -1)) {
            // Search action button is clicked
            showSearchToolbar(true);
        }
    }
}
