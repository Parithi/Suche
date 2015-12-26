package com.parithi.suche.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.parithi.suche.R;
import com.parithi.suche.fragments.FeedFragment;
import com.parithi.suche.models.Feed;
import com.parithi.suche.utils.FeedsManager;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class FeedActivity extends FragmentActivity {

    FeedFragment feedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        feedFragment = (FeedFragment) getSupportFragmentManager().findFragmentById(R.id.feed_fragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        feedFragment.onActivityResult(requestCode, resultCode, data);
    }


}
