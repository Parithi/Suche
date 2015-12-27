package com.parithi.suche.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

/**
 * Created by earul on 12/26/15.
 */
public class TwitterUtils {

    public static void replyToTweet(final Context context, String tweetData, long replyToStatusId){
        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        StatusesService statusesService = TwitterCore.getInstance().getApiClient(session).getStatusesService();

        //void update(@Field("status") String var1, @Field("in_reply_to_status_id") Long var2, @Field("possibly_sensitive") Boolean var3, @Field("lat") Double var4, @Field("long") Double var5, @Field("place_id") String var6, @Field("display_cooridnates") Boolean var7, @Field("trim_user") Boolean var8, @Field("media_ids") String var9, Callback<Tweet> var10);

        statusesService.update(tweetData, replyToStatusId, false, null, null, null, true, false, null, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> tweetResult) {
                Log.d("Tweet", "Success");
            }

            @Override
            public void failure(TwitterException e) {
                Log.d("Tweet", "Failure");
                e.printStackTrace();
                Toast.makeText(context, "Failed : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }
}
