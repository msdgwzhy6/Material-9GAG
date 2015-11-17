package com.spark.material9gag;

import android.app.Application;
import android.content.Context;

import com.spark.material9gag.data.RequestManager;

/**
 * Created by Spark on 11/17/2015.
 */
public class App extends Application{
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        RequestManager.INSTANCE.initial(sContext);
    }
    public static Context getContext() {
        return sContext;
    }
}
