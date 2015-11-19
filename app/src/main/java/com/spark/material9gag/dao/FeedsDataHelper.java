package com.spark.material9gag.dao;

import android.content.Context;
import android.net.Uri;

/**
 * Created by Daniel on 11/19/2015.
 */
public class FeedsDataHelper extends BaseDataHelper {

    public FeedsDataHelper(Context context){
        super(context);
    }
    @Override
    protected Uri getContentUri() {
        return DataProvider.FEEDS_CONTENT_URI;
    }
}
