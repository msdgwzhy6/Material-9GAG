package com.spark.material9gag.ui.fragment;


import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.spark.material9gag.R;
import com.spark.material9gag.api.GagApi;
import com.spark.material9gag.data.GsonRequest;
import com.spark.material9gag.data.RequestManager;
import com.spark.material9gag.model.Feed;
import com.spark.material9gag.util.TaskUtil;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends BaseFragment {

    private String page;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title Parameter 1.
     * @return A new instance of fragment FeedFragment.
     */
    public static FeedFragment newInstance(String title) {
        FeedFragment fragment = new FeedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false);

        //TODO Adapter
        //TODO Show
    }

    private void loadData(String next) {
        //request data from Internet
        RequestManager.INSTANCE.getRequestQueue().add(new GsonRequest<>(String.format(GagApi.LIST, "Hot", next),
                Feed.FeedRequestData.class, null, new Response.Listener<Feed.FeedRequestData>() {
            @Override
            public void onResponse(final Feed.FeedRequestData response) {
                TaskUtil.executeAsyncTask(new AsyncTask<Object, Void, Void>() {
                    @Override
                    protected Void doInBackground(Object... params) {
                        page = response.getPage();
                        ArrayList<Feed> feeds = response.data;
                        //write to db

                        return null;
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }));
    }

}
