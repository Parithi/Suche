package com.parithi.suche.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parithi.suche.R;
import com.parithi.suche.SucheApplication;
import com.parithi.suche.activities.DetailTweetActivity;
import com.parithi.suche.models.Feed;
import com.parithi.suche.models.FeedType;
import com.parithi.suche.models.RSSFeed;
import com.parithi.suche.models.Tweet;
import com.parithi.suche.views.FeedView;
import com.twitter.sdk.android.tweetui.TweetView;

/**
 * Created by earul on 12/15/15.
 */
public class FeedViewFactory {

    public static View getViewForType(final Context context, final Feed feed){
        View requiredView = null;
        if(feed.getFeedType() == FeedType.TWITTER){
            final String screenName = ((Tweet) feed).getTweet().user.screenName;
            LinearLayout tweetLinearLayout = new LinearLayout(context);
            tweetLinearLayout.setOrientation(LinearLayout.VERTICAL);

            TweetView tweetView = new TweetView(context,((Tweet)feed).getTweet());
            tweetView.setTweetActionsEnabled(true);

            tweetLinearLayout.addView(tweetView);

            if(!(context instanceof DetailTweetActivity)) {
                tweetView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent detailTweetIntent = new Intent(context, DetailTweetActivity.class);
                        detailTweetIntent.putExtra("FEED_ID", feed.getId());
                        context.startActivity(detailTweetIntent);
                    }
                });

                final View twitterActionsLayout = LayoutInflater.from(context).inflate(R.layout.twitter_actions_layout, null);
                TextView replyButton = ((TextView) twitterActionsLayout.findViewById(R.id.reply_button));
                if(SucheApplication.getActiveSession()!=null){
                    replyButton.setText("Reply to @" + ((Tweet) feed).getTweet().user.screenName);
                    replyButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText commentEditText = (EditText) twitterActionsLayout.findViewById(R.id.comment_edittext);
                            if(commentEditText.getVisibility() == View.GONE){
                                commentEditText.setVisibility(View.VISIBLE);
                                LinearLayout.LayoutParams marginParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                marginParams.setMargins(0, 2, 0, 0);
                                twitterActionsLayout.findViewById(R.id.actions_parent_linearlayout).setLayoutParams(marginParams);
                                commentEditText.setText("@" + screenName + " ");
                                int pos = commentEditText.getText().length();
                                commentEditText.setSelection(pos);
                                commentEditText.requestFocus();
                            } else {
                                if(!TextUtils.isEmpty(commentEditText.getText())) {
                                    commentEditText.setVisibility(View.GONE);
//                            TwitterUtils.replyToTweet(context, commentEditText.getText().toString(), feed.getId());
                                } else {
                                    Toast.makeText(context,"Please enter reply text",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                } else {
                    replyButton.setText("Login to reply");
                }
                tweetLinearLayout.addView(twitterActionsLayout);
            }

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
