package com.spark.material9gag.model;

import android.database.Cursor;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * Created by Spark on 11/17/2015.
 */
public class Feed extends BaseModel {
    private static final HashMap<String, Feed> CACHE = new HashMap<String, Feed>();

    private String id;
    private String caption;
    private String link;
    private Image images;
    private Vote votes;

    public class Image {
        public String small;
        public String normal;
        public String large;
    }

    private class Vote {
        public int count;
    }

    public static class FeedRequestData {
        public ArrayList<Feed> data;
        public Paging paging;

        public String getPage() {
            return paging.next;
        }
    }

    private class Paging {
        public String next;
    }

    private static void addToCache(Feed feed) {
        CACHE.put(feed.id, feed);
    }

    private static Feed getFromCache(String id) {
        return CACHE.get(id);
    }

    public static Feed fromCursor(Cursor cursor) {
        String id = cursor.getString(cursor.getColumnIndex(FeedsDBInfo.ID));
        Feed feed = getFromCache(id);
        if (feed != null) {
            return feed;
        }
        feed = new Gson().fromJson(
                cursor.getString(cursor.getColumnIndex(FeedsDBInfo.JSON)),
                Feed.class);
        addToCache(feed);
        return feed;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Image getImages() {
        return images;
    }

    public void setImages(Image images) {
        this.images = images;
    }

    public Vote getVotes() {
        return votes;
    }

    public void setVotes(Vote votes) {
        this.votes = votes;
    }
}
