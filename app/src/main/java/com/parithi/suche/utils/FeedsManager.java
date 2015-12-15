package com.parithi.suche.utils;

import android.util.Log;

import com.parithi.suche.models.Feed;
import com.parithi.suche.models.FeedType;
import com.parithi.suche.models.Tweet;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterApiException;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.internal.TwitterApiConstants;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.tweetui.TweetUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by earul on 12/15/15.
 */
public class FeedsManager {

    private static final String LOG_TAG = FeedsManager.class.getSimpleName();

    private static FeedsManager feedsManager;
    private HashMap<Long, Feed> feedList;

    public static FeedsManager getInstance(){
        if(feedsManager==null){
            feedsManager = new FeedsManager();
        }
        return feedsManager;
    }

    public HashMap<Long,Feed> getFeedList() {
        if(feedList==null){
            feedList = new HashMap<>();
        }
        return feedList;
    }

    public void addFeedToList(Long uniqueId, Feed feed){
        getFeedList().put(uniqueId, feed);
    }

    public void clearFeeds(){
        if(feedList!=null) {
            feedList.clear();
        }
    }

    private void getTweets(){
        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result<AppSession> result) {
                AppSession guestAppSession = (AppSession) result.data;
                TwitterApiClient twitterApiClient =  TwitterCore.getInstance().getApiClient(guestAppSession);
                twitterApiClient.getSearchService().tweets("#greenbuild", null, null, null, null, 50, null, null, null, true, new Callback<Search>() {
                    @Override
                    public void success(Result<Search> result) {
                        List<com.twitter.sdk.android.core.models.Tweet> fabricTweets = ((Search) result.data).tweets;
                        for(com.twitter.sdk.android.core.models.Tweet fabricTweet : fabricTweets){
                            Log.d(LOG_TAG,"Loading tweet :" + fabricTweet.getId());
                            getFeedList().put(fabricTweet.getId(), new Tweet(fabricTweet.getId(), fabricTweet));
                        }
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        final TwitterApiException apiException = (TwitterApiException) exception;
                        final int errorCode = apiException.getErrorCode();
                        if (errorCode == TwitterApiConstants.Errors.APP_AUTH_ERROR_CODE || errorCode == TwitterApiConstants.Errors.GUEST_AUTH_ERROR_CODE) {
                            // request new guest AppSession (i.e. logInGuest)
                            // optionally retry
                        }
                    }
                });
            }

            @Override
            public void failure(TwitterException e) {
                Log.d("TwitterKit", "Load Tweet failure for ", e);
            }
        });
    }

    private void getLinkedInPosts(){
        getFeedList().put(5L,new Feed(5, FeedType.LINKEDIN));
        getFeedList().put(6L,new Feed(6, FeedType.LINKEDIN));
        getFeedList().put(7L,new Feed(7, FeedType.LINKEDIN));
        getFeedList().put(8L,new Feed(8, FeedType.LINKEDIN));
        getFeedList().put(9L,new Feed(9, FeedType.LINKEDIN));
    }

    private void getFacebookPosts(){
        getFeedList().put(10L,new Feed(10, FeedType.FACEBOOK));
        getFeedList().put(11L,new Feed(11, FeedType.FACEBOOK));
        getFeedList().put(12L,new Feed(12, FeedType.FACEBOOK));
        getFeedList().put(13L,new Feed(13, FeedType.FACEBOOK));
        getFeedList().put(14L,new Feed(14, FeedType.FACEBOOK));
    }

    private void getInstagramPosts(){
        getFeedList().put(15L, new Feed(15, FeedType.INSTAGRAM));
        getFeedList().put(16L, new Feed(16, FeedType.INSTAGRAM));
        getFeedList().put(17L, new Feed(17, FeedType.INSTAGRAM));
        getFeedList().put(18L, new Feed(18, FeedType.INSTAGRAM));
        getFeedList().put(19L,new Feed(19, FeedType.INSTAGRAM));
    }

    public void getData(){
        getTweets();
        getFacebookPosts();
        getInstagramPosts();
        getLinkedInPosts();
    }
}
