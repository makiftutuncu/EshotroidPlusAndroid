package com.mehmetakiftutuncu.eshotroid.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mehmetakiftutuncu.eshotroid.R;
import com.mehmetakiftutuncu.eshotroid.utilities.Log;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    @Override protected void onStart() {
        Log.debug(TAG, "onStart");

        super.onStart();
    }

    @Override protected void onResume() {
        Log.debug(TAG, "onResume");

        super.onResume();
    }

    @Override protected void onPause() {
        Log.debug(TAG, "onPause");

        super.onPause();
    }

    @Override protected void onStop() {
        Log.debug(TAG, "onStop");

        super.onStop();
    }

    @Override protected void onDestroy() {
        Log.debug(TAG, "onDestroy");

        super.onDestroy();
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        Log.debug(TAG, "onCreate");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_search);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
