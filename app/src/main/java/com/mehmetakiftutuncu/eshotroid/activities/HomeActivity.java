package com.mehmetakiftutuncu.eshotroid.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mehmetakiftutuncu.eshotroid.R;
import com.mehmetakiftutuncu.eshotroid.fragments.BusListFragment;
import com.mehmetakiftutuncu.eshotroid.utilities.Log;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    @Override protected void onSaveInstanceState(Bundle outState) {
        Log.debug(TAG, "onSaveInstanceState");

        super.onSaveInstanceState(outState);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        Log.debug(TAG, "onCreate");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        BusListFragment busListFragment;

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
}
