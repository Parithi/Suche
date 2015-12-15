package com.parithi.suche.models;

/**
 * Created by earul on 12/15/15.
 */
public class Feed {
    private long id;
    private FeedType feedType;

    public Feed(long id, FeedType feedType) {
        this.id = id;
        this.feedType = feedType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public FeedType getFeedType() {
        return feedType;
    }

    public void setFeedType(FeedType feedType) {
        this.feedType = feedType;
    }
}
