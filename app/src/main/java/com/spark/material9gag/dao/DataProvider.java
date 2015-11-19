package com.spark.material9gag.dao;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.spark.material9gag.App;
import com.spark.material9gag.model.FeedsDBInfo;

/**
 * Created by Spark on 11/19/2015.
 */
public class DataProvider extends ContentProvider {
    static final String TAG = DataProvider.class.getSimpleName();
    public static final String AUTHORITY = "com.spark.9gag.provider";
    public static final String SCHEME = "content://";

    public static final String PATH_FEEDS = "feeds";
    public static final Uri FEEDS_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + "/" + PATH_FEEDS);
    public static final Object DBLock = new Object();
    private static final int FEEDS = 1;
    public static DBHelper dbHelper = new DBHelper(App.getContext());

    /*
     * MIME type definitions
     */
    public static final String FEED_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.storm.9gag.feed";

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //add uri codes
    static {
        sUriMatcher.addURI(AUTHORITY, PATH_FEEDS, FEEDS);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        synchronized (DBLock) {
            SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
            String table = matchTable(uri);
            sqLiteQueryBuilder.setTables(table);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = sqLiteQueryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            //TODO why
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case FEEDS:
                return FEED_CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        synchronized (DBLock) {
            String table = matchTable(uri);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            long rowId = 0;
            db.beginTransaction();
            try {
                rowId = db.insert(table, null, values);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            } finally {
                db.endTransaction();
            }
            if (rowId > 0) {
                Uri returnUri = ContentUris.withAppendedId(uri, rowId);
                getContext().getContentResolver().notifyChange(uri, null);
                return returnUri;
            }
            throw new SQLException("Failed to insert row into " + uri);
        }
    }

    public int bulkInsert(ContentValues[] values){
        return getContext().getContentResolver().bulkInsert(FEEDS_CONTENT_URI,values);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        synchronized (DBLock) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            int count = 0;
            String table = matchTable(uri);
            db.beginTransaction();
            try {
                count = db.delete(table, selection, selectionArgs);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        synchronized (DBLock) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String table = matchTable(uri);
            int count;
            db.beginTransaction();
            try {
                count = db.update(table, values, selection, selectionArgs);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }
    }

    private String matchTable(Uri uri) {
        String table;
        switch (sUriMatcher.match(uri)) {
            case FEEDS:
                table = FeedsDBInfo.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return table;
    }
}
