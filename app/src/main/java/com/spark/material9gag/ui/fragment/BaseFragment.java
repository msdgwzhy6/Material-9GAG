package com.spark.material9gag.ui.fragment;


import android.support.v4.app.Fragment;

import com.spark.material9gag.data.RequestManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {

    @Override
    public void onDestroy() {
        super.onDestroy();
        RequestManager.INSTANCE.getRequestQueue().cancelAll(this);
    }
}
