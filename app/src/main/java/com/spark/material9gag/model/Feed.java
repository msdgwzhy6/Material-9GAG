package com.spark.material9gag.model;

import java.util.ArrayList;

/**
 *
 * Created by Spark on 11/17/2015.
 */
public class Feed extends BaseModel {
    private String id;
    private String caption;
    private String link;
    private Image images;
    private Vote votes;

    private class Image {
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
