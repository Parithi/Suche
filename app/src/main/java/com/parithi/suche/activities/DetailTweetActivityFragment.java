package com.parithi.suche.activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parithi.suche.R;
import com.parithi.suche.models.Feed;
import com.parithi.suche.models.Tweet;
import com.parithi.suche.utils.FeedViewFactory;
import com.parithi.suche.utils.FeedsManager;
import com.parithi.suche.utils.TwitterUtils;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.tweetui.TweetView;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailTweetActivityFragment extends Fragment {

    private long feedId;
    private ViewGroup detailTweetParentLayout;
    private EditText replyEditText;
    private TextView replyButton;

    public DetailTweetActivityFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_tweet, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        detailTweetParentLayout = (ViewGroup) view.findViewById(R.id.parent_detail_tweet_layout);
        replyEditText = (EditText) view.findViewById(R.id.comment_edittext);
        replyButton = (TextView) view.findViewById(R.id.reply_button);

        if(getActivity().getIntent().getExtras()!=null){
            feedId = getActivity().getIntent().getLongExtra("FEED_ID",-1);
            if(feedId != -1){
                Feed feed = FeedsManager.getInstance().getFeedList().get(feedId);
                if(feed instanceof Tweet){
                    detailTweetParentLayout.addView(FeedViewFactory.getViewForType(getActivity(),feed));
                    replyEditText.setVisibility(View.VISIBLE);
                    replyButton.setText("Reply to @" + ((Tweet) feed).getTweet().user.screenName);
                    TwitterUtils.addReplyTweetsToLayout(feedId, new Callback<List<com.twitter.sdk.android.core.models.Tweet>>() {
                        @Override
                        public void success(Result<List<com.twitter.sdk.android.core.models.Tweet>> result) {
                            List<com.twitter.sdk.android.core.models.Tweet> replyTweets = result.data;
                            for(com.twitter.sdk.android.core.models.Tweet tweet : replyTweets){
                                detailTweetParentLayout.addView(new TweetView(getActivity(),tweet));
                            }
                        }

                        @Override
                        public void failure(TwitterException e) {

                        }
                    });
                }
            }
        }
    }


}
