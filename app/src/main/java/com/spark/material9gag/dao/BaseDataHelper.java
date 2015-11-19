package com.spark.material9gag.dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Daniel on 11/19/2015.
 */
public abstract class BaseDataHelper {

    private Context context;
    private ContentResolver contentResolver;

    public BaseDataHelper(Context ctex) {
        context = ctex;
        contentResolver = context.getContentResolver();
    }

    public Context getContext() {
        return context;
    }

    protected abstract Uri getContentUri();

    protected Cursor query(String[] projection, String selection,
                           String[] selectionArgs, String sortOrder) {
        return contentResolver.query(getContentUri(), projection, selection, selectionArgs, sortOrder);
    }

    protected Uri insert(ContentValues contentValues){
        return contentResolver.insert(getContentUri(), contentValues);
    }

    protected int bulkInsert(ContentValues[] values){
        return contentResolver.bulkInsert(getContentUri(),values);
    }
}
