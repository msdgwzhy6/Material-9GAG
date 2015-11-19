package com.spark.material9gag.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Daniel on 11/19/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "9gag.db";
    private static final int VERSION = 1;

    public DBHelper(Context context){
        super(context,DB_NAME,null,VERSION);
    }

    //数据库第一次被创建时onCreate会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS feeds" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, category INTEGER,json TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
