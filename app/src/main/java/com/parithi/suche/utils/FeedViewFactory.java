package com.parithi.suche.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.parithi.suche.models.Feed;
import com.parithi.suche.models.FeedType;
import com.parithi.suche.models.Tweet;
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

        switch (feed.getFeedType()){
            case TWITTER:
                requiredView = new TweetView(context,((Tweet)feed).getTweet());
                break;
            case FACEBOOK:
                requiredView = new TextView(context);
                ((TextView) requiredView).setText("Facebook");
                break;
            case INSTAGRAM:
                requiredView = new TextView(context);
                ((TextView) requiredView).setText("Instagram");
                break;
            case LINKEDIN:
                requiredView = new TextView(context);
                ((TextView) requiredView).setText("LinkedIn");
                break;
        }

        return requiredView;
    }
}
