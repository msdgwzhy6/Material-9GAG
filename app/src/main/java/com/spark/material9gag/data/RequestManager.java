package com.spark.material9gag.data;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Spark on 11/15/2015.
 */
public enum RequestManager {
    INSTANCE;

    private static RequestQueue requestQueue;

    public RequestQueue getRequestQueue(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

    public void addToRequestQueue(Request<?> req) {
        requestQueue.add(req);
    }

    public void addToRequestQueue(Request<?> req, Object tag) {
        if (tag != null) {
            req.setTag(tag);
        }
        requestQueue.add(req);
    }
}
