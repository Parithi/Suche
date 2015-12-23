package com.parithi.suche.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.parithi.suche.R;
import com.parithi.suche.fragments.FeedFragment;
import com.parithi.suche.utils.FeedsManager;

public class FeedActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new FeedFragment())
                    .commit();
        }
    }

}
