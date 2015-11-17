package com.spark.material9gag.util;

import android.os.AsyncTask;
import android.os.Build;

/**
 *
 * Created by Spark on 11/17/2015.
 */
public class TaskUtil {
    public static void executeAsyncTask(AsyncTask<Object, Void, Void> task, Object... params) {
        if (Build.VERSION.SDK_INT >= 11) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }
}
