package com.parithi.suche.models;

/**
 * Created by earul on 12/15/15.
 */
public class Tweet extends Feed {

    private com.twitter.sdk.android.core.models.Tweet tweet;

    public Tweet(long id, com.twitter.sdk.android.core.models.Tweet tweet) {
        super(id, FeedType.TWITTER);
        this.tweet = tweet;
    }

    public com.twitter.sdk.android.core.models.Tweet getTweet() {
        return tweet;
    }
}
