package com.spark.material9gag.ui.fragment;


import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.spark.material9gag.App;
import com.spark.material9gag.R;
import com.spark.material9gag.api.GagApi;
import com.spark.material9gag.dao.FeedsDataHelper;
import com.spark.material9gag.data.GsonRequest;
import com.spark.material9gag.data.RequestManager;
import com.spark.material9gag.model.Category;
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
    private FeedsDataHelper feedsDataHelper;
    Category category;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param category Parameter 1.
     * @return A new instance of fragment FeedFragment.
     */
    public static FeedFragment newInstance(Category category) {
        FeedFragment fragment = new FeedFragment();
        Bundle bundle = new Bundle();
        bundle.putString("categoryName",category.name());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //get category
        Bundle bundle = getArguments();
        category = Category.valueOf(bundle.getString("categoryName"));
        feedsDataHelper = new FeedsDataHelper(App.getContext(),category);

        //TODO Adapter
        //TODO Show
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    private void loadData(String next) {
        //request data from Internet
        RequestManager.INSTANCE.getRequestQueue().add(new GsonRequest<>(String.format(GagApi.LIST, "Hot", next),
                Feed.FeedRequestData.class, null, new Response.Listener<Feed.FeedRequestData>() {
            final boolean isRefreshFromTop = ("0".equals(page));

            @Override
            public void onResponse(final Feed.FeedRequestData response) {
                TaskUtil.executeAsyncTask(new AsyncTask<Object, Void, Void>() {
                                              @Override
                                              protected Void doInBackground(Object... params) {
                                                  if (isRefreshFromTop) {
                                                      feedsDataHelper.deleteAll();
                                                  }
                                                  page = response.getPage();
                                                  ArrayList<Feed> feeds = response.data;
                                                  //write to db
                                                  feedsDataHelper.bulkInsert(feeds);
                                                  return null;
                                              }

                                              @Override
                                              protected void onPostExecute(Void aVoid) {
                                                  super.onPostExecute(aVoid);
                                                  //TODO LoadingFooter
                                              }
                                          }
                );
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }));
    }

}
