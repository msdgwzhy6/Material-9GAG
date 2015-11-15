package com.spark.material9gag.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.spark.material9gag.data.RequestManager;

/**
 * Created by Spark on 11/15/2015.
 */
public class BaseActivity extends AppCompatActivity {
    protected ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RequestManager.INSTANCE.getRequestQueue(getApplicationContext()).cancelAll(this);
    }

}
