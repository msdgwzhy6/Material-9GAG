package com.spark.material9gag.ui.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.spark.material9gag.App;
import com.spark.material9gag.PicDetailActivity;
import com.spark.material9gag.R;
import com.spark.material9gag.api.GagApi;
import com.spark.material9gag.dao.FeedsDataHelper;
import com.spark.material9gag.data.GsonRequest;
import com.spark.material9gag.data.RequestManager;
import com.spark.material9gag.model.Category;
import com.spark.material9gag.model.Feed;
import com.spark.material9gag.ui.adapter.FeedsAdapter;
import com.spark.material9gag.ui.view.PageListView;
import com.spark.material9gag.util.TaskUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.lv_feed)
    PageListView feedListView;
    @Bind(R.id.swipe_container)
    SwipeRefreshLayout swipeLayout;
    private String page;
    private FeedsDataHelper feedsDataHelper;
    private FeedsAdapter feedsAdapter;
    Category category;
    private ProgressBar progressBar;


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
        bundle.putString("categoryName", category.name());
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
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        ButterKnife.bind(this, view);
        swipeLayout.setOnRefreshListener(this);
        progressBar = new ProgressBar(getContext());

        //get category
        Bundle bundle = getArguments();
        category = Category.valueOf(bundle.getString("categoryName"));
        feedsDataHelper = new FeedsDataHelper(App.getContext(), category);

        //TODO Adapter
        feedsAdapter = new FeedsAdapter(getActivity(), feedListView);
        feedListView.setAdapter(feedsAdapter);
        feedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String imgUrl = feedsAdapter.getItem(position).getImages().large;
                Intent intent = new Intent(getActivity(), PicDetailActivity.class);
                intent.putExtra("IMAGE_URL",imgUrl);
                startActivity(intent);
            }
        });
        progressBar.setIndeterminate(true);
        feedListView.addFooterView(progressBar);
        feedListView.setLoadNextListener(new PageListView.OnLoadNextListener() {
            @Override
            public void onLoadNext() {
                loadData(page);
            }
        });
        //TODO Show
        getLoaderManager().initLoader(0, null, this);
        loadFirst();

        return view;
    }

    private void loadFirst() {
        page = "0";
        loadData(page);
    }

    private void loadData(String next) {
        //request data from Internet
        RequestManager.INSTANCE.getRequestQueue().add(new GsonRequest<>(String.format(GagApi.LIST, "hot", next),
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
                                                  Log.d("FeedFragment", feeds.get(0).getCaption());
                                                  //write to db
                                                  feedsDataHelper.bulkInsert(feeds);
                                                  return null;
                                              }

                                              @Override
                                              protected void onPostExecute(Void aVoid) {
                                                  super.onPostExecute(aVoid);
                                                  swipeLayout.setRefreshing(false);
                                                  PageListView.isLoading = false;
                                              }
                                          }
                );
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("FeedFragment","VolleyError" + volleyError.toString());
                swipeLayout.setRefreshing(false);
            }
        }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return feedsDataHelper.getCursorLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        feedsAdapter.changeCursor(data);
        if (data != null && data.getCount() == 0) {
            loadFirst();
        }

    }

    @Override
    public void onRefresh() {
        loadFirst();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        feedsAdapter.changeCursor(null);
    }
}
