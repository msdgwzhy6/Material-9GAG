package com.spark.material9gag.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import com.google.gson.Gson;
import com.spark.material9gag.model.Category;
import com.spark.material9gag.model.Feed;
import com.spark.material9gag.model.FeedsDBInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 11/19/2015.
 */
public class FeedsDataHelper extends BaseDataHelper {

    private Category category;
    public FeedsDataHelper(Context context,Category cat){
        super(context);
        category = cat;
    }
    @Override
    protected Uri getContentUri() {
        return DataProvider.FEEDS_CONTENT_URI;
    }

    public void bulkInsert(List<Feed> feeds){
        ArrayList<ContentValues> contentValuesList = new ArrayList<>();
        for(Feed feed : feeds){
            ContentValues values = new ContentValues();
            values.put(FeedsDBInfo.ID, feed.getId());
            values.put(FeedsDBInfo.CATEGORY, category.ordinal());
            values.put(FeedsDBInfo.JSON, new Gson().toJson(feed));
            contentValuesList.add(values);
        }
        ContentValues[] valuesArray = new ContentValues[contentValuesList.size()];
        bulkInsert(contentValuesList.toArray(valuesArray));
    }


    public int deleteAll() {
        synchronized (DataProvider.DBLock) {
            DBHelper mDBHelper = DataProvider.getDBHelper();
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            int row = db.delete(FeedsDBInfo.TABLE_NAME, FeedsDBInfo.CATEGORY + "=?", new String[] {
                    String.valueOf(category.ordinal())
            });
            Log.d("FeedsDataHelper",String.valueOf(row));
            return row;
        }
    }

    public CursorLoader getCursorLoader() {
        return new CursorLoader(getContext(), getContentUri(), null, FeedsDBInfo.CATEGORY + "=?",
                new String[] {
                        String.valueOf(category.ordinal())
                }, FeedsDBInfo._ID + " ASC");
    }
}
