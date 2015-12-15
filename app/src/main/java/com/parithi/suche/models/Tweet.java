package com.parithi.suche.models;

import android.util.Log;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

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
