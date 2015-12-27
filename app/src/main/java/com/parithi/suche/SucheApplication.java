package com.parithi.suche;

import android.app.Application;

import com.parithi.suche.utils.FeedsManager;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterSession;

import io.fabric.sdk.android.Fabric;

/**
 * Created by earul on 12/15/15.
 */
public class SucheApplication extends Application {

    private static final String TWITTER_KEY = "xTP6iyKG5v3bh04ajWuUWMcEc";
    private static final String TWITTER_SECRET = "hzTKILBuj5VxX3dXRdlSdMwMKy4Y0KSKQEzUdVSlju78n0saaH";
    static TwitterSession activeSession;
    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        FeedsManager.getInstance().getData();
        activeSession = Twitter.getSessionManager().getActiveSession();

    }

    public static TwitterSession getActiveSession() {
        return activeSession;
    }
}
