package com.mehmetakiftutuncu.eshotroid.activities;

import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitch;
import com.mehmetakiftutuncu.eshotroid.R;
import com.mehmetakiftutuncu.eshotroid.fragments.BusListFragment;
import com.mehmetakiftutuncu.eshotroid.utilities.Keyboard;
import com.mehmetakiftutuncu.eshotroid.utilities.Log;

public class HomeActivity extends AppCompatActivity implements WithToolbar, Searchable {
    private static final String TAG = "HomeActivity";

    private BusListFragment busListFragment;

    private Toolbar toolbar;
    private MaterialAnimatedSwitch materialAnimatedSwitch;
    private RelativeLayout relativeLayoutSearch;
    private EditText editTextSearch;
    private FloatingActionButton floatingActionButton;

    private boolean isSearching;

    @Override protected void onSaveInstanceState(Bundle outState) {
        Log.debug(TAG, "onSaveInstanceState");

        outState.putBoolean("isSearching", isSearching);

        super.onSaveInstanceState(outState);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        Log.debug(TAG, "onCreate");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        initializeContent(savedInstanceState);
        initializeToolbar();
        initializeSearch();
        initializeFloatingSearchButton();

        if (savedInstanceState != null) {
            toggleSearch(savedInstanceState.getBoolean("isSearching"));
        }
    }

    @Override public void onBackPressed() {
        if (isSearching()) {
            toggleSearch(false);

            return;
        }

        super.onBackPressed();
    }

    @Override public void initializeToolbar() {
        toolbar                = (Toolbar) findViewById(R.id.toolbar);
        materialAnimatedSwitch = (MaterialAnimatedSwitch) findViewById(R.id.materialAnimatedSwitch_filter);

        setSupportActionBar(toolbar);
    }

    @Override public void initializeSearch() {
        relativeLayoutSearch = (RelativeLayout) toolbar.findViewById(R.id.relativeLayout_search);
        editTextSearch       = (EditText) relativeLayoutSearch.findViewById(R.id.editText_search);

        ImageButton imageButtonClearSearch = (ImageButton) relativeLayoutSearch.findViewById(R.id.imageButton_clearSearch);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override public void afterTextChanged(Editable s) {
                onSearchQueryChanged(s.toString());
            }
        });

        editTextSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Keyboard.toggleKeyboard(HomeActivity.this, v, true);
                } else {
                    Keyboard.toggleKeyboard(HomeActivity.this, v, false);
                }
            }
        });

        imageButtonClearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextSearch.setText("");
                editTextSearch.requestFocus();
            }
        });

        relativeLayoutSearch.setVisibility(View.GONE);
    }

    @Override public boolean isSearching() {
        return isSearching;
    }

    @Override public void toggleSearch(boolean isSearching) {
        if (this.isSearching ^ isSearching) {
            this.isSearching = isSearching;

            busListFragment.setSearching(isSearching);

            final TransitionDrawable toolbarBackground = (TransitionDrawable) toolbar.getBackground();
            toolbarBackground.setCrossFadeEnabled(true);

            if (isSearching) {
                Log.debug(TAG, "Toggling search on");

                toolbarBackground.startTransition(300);
                toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggleSearch(false);
                    }
                });
                relativeLayoutSearch.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.statusBarColorWhite));
                }
                materialAnimatedSwitch.setVisibility(View.GONE);
                floatingActionButton.hide();
                editTextSearch.requestFocus();
            } else {
                Log.debug(TAG, "Toggling search off");

                editTextSearch.setText("");
                toolbarBackground.reverseTransition(300);
                toolbar.setNavigationIcon(null);
                toolbar.setNavigationOnClickListener(null);
                relativeLayoutSearch.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                materialAnimatedSwitch.setVisibility(View.VISIBLE);
                floatingActionButton.show();
            }
        }
    }

    @Override public void onSearchQueryChanged(String query) {
        Log.debug(TAG, "Searching '%s'", query);

        busListFragment.search(query);
    }

    public FloatingActionButton getFloatingActionButton() {
        return floatingActionButton;
    }

    private void initializeContent(Bundle savedInstanceState) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) {
            busListFragment = (BusListFragment) supportFragmentManager.findFragmentByTag(BusListFragment.TAG);
        } else {
            busListFragment = BusListFragment.instance();
        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout_busList, busListFragment, BusListFragment.TAG)
            .commit();
    }

    private void initializeFloatingSearchButton() {
        floatingActionButton = (FloatingActionButton) findViewById(R.id.search);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                toggleSearch(true);
            }
        });
    }
}
