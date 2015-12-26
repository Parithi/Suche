package com.parithi.suche.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    public View getViewForType(final Context context, Feed feed){
        View requiredView = null;
        if(feed.getFeedType() == FeedType.TWITTER){
            LinearLayout tweetLinearLayout = new LinearLayout(context);
            tweetLinearLayout.setOrientation(LinearLayout.VERTICAL);

            TweetView tweetView = new TweetView(context,((Tweet)feed).getTweet());
            tweetView.setTweetActionsEnabled(true);

            final View twitterActionsLayout = LayoutInflater.from(context).inflate(R.layout.twitter_actions_layout, null);
            ((TextView) twitterActionsLayout.findViewById(R.id.reply_button)).setText("Reply to @" + ((Tweet) feed).getTweet().user.screenName);
            ((TextView) twitterActionsLayout.findViewById(R.id.reply_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText commentEditText = (EditText) twitterActionsLayout.findViewById(R.id.comment_edittext);
                    if(commentEditText.getVisibility() == View.GONE){
                        commentEditText.setVisibility(View.VISIBLE);

                        LinearLayout.LayoutParams marginParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        marginParams.setMargins(0, 2, 0, 0);
                        twitterActionsLayout.findViewById(R.id.actions_parent_linearlayout).setLayoutParams(marginParams);
                    } else {
                        Toast.makeText(context,"This is some reply",Toast.LENGTH_SHORT).show();
                    }
                }
            });

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
