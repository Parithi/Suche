package com.parithi.suche.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parithi.suche.R;
import com.parithi.suche.models.Feed;
import com.parithi.suche.models.FeedType;
import com.parithi.suche.models.RSSFeed;
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
            LinearLayout tweetLinearLayout = new LinearLayout(context);
            tweetLinearLayout.setOrientation(LinearLayout.VERTICAL);

            TweetView tweetView = new TweetView(context,((Tweet)feed).getTweet());
            tweetView.setTweetActionsEnabled(true);

            View twitterActionsLayout = LayoutInflater.from(context).inflate(R.layout.twitter_actions_layout, null);
            ((TextView) twitterActionsLayout.findViewById(R.id.reply_button)).setText("Reply to @" + ((Tweet) feed).getTweet().user.screenName);

            tweetLinearLayout.addView(tweetView);
            tweetLinearLayout.addView(twitterActionsLayout);

            requiredView = tweetLinearLayout;
        } else {
            FeedView rssFeedView = new FeedView(context, FeedType.RSSFEED);
            RSSFeed rssFeed = (RSSFeed) feed;
            rssFeedView.setFeedUsername(rssFeed.getLink());
            rssFeedView.setFeedTitle(rssFeed.getTitle());
            rssFeedView.setFeedDescription(rssFeed.getDescription());
            requiredView = rssFeedView;
        }

        return requiredView;
    }
}
