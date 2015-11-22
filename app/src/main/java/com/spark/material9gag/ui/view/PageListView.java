package com.spark.material9gag.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by Spark on 11/21/2015.
 */
public class PageListView extends ListView implements AbsListView.OnScrollListener {

    private OnLoadNextListener mLoadNextListener;
    public static boolean isLoading = false;
    public PageListView(Context context) {
        super(context);
        setOnScrollListener(this);
    }

    public PageListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnScrollListener(this);
    }

    public PageListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount >= totalItemCount
                && totalItemCount != 0
                && mLoadNextListener != null && !isLoading) {
            isLoading = true;
            mLoadNextListener.onLoadNext();
        }
    }
    public void setLoadNextListener(OnLoadNextListener listener) {
        mLoadNextListener = listener;
    }
    public interface OnLoadNextListener {
        void onLoadNext();
    }

}
