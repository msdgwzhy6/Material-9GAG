package com.spark.material9gag.model;

/**
 * Created by Spark on 11/19/2015.
 */
public enum Category {
    hot("Hot"), trending("Trending"), fresh("Fresh");
    private String mDisplayName;

    Category(String displayName) {
        mDisplayName = displayName;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

}
