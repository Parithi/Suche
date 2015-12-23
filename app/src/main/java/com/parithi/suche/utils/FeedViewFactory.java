package com.parithi.suche.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.parithi.suche.R;
import com.parithi.suche.models.Feed;
import com.parithi.suche.models.FeedType;
import com.parithi.suche.models.Tweet;
import com.parithi.suche.views.FeedView;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

/**
 * Created by earul on 12/15/15.
 */
public class FeedViewFactory {

    public View getViewForType(Context context, Feed feed){
        View requiredView = null;

        if(feed.getFeedType() == FeedType.TWITTER){
            requiredView = new TweetView(context,((Tweet)feed).getTweet());
        } else {
            requiredView = new FeedView(context,FeedType.RSSFEED);
        }

        return requiredView;
    }
}
