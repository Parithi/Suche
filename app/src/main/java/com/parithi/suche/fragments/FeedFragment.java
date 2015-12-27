package com.parithi.suche.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.parithi.suche.R;
import com.parithi.suche.SucheApplication;
import com.parithi.suche.models.Feed;
import com.parithi.suche.utils.FeedViewFactory;
import com.parithi.suche.utils.FeedsManager;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

/**
 * Created by earul on 12/15/15.
 */
public class FeedFragment extends Fragment implements FeedsManager.FeedManagerDelegate {

    private static final String LOG_TAG = FeedFragment.class.getSimpleName();

    private RecyclerView mFeedRecyclerView;
    private FeedAdapter mFeedAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TwitterLoginButton loginButton;

    public FeedFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.feedfragment, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        mFeedRecyclerView = (RecyclerView) rootView.findViewById(R.id.feed_recycler_view);

        mFeedRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mFeedRecyclerView.setLayoutManager(mLayoutManager);

        mFeedAdapter = new FeedAdapter();
        mFeedRecyclerView.setAdapter(mFeedAdapter);

        FeedsManager.getInstance().setFeedManagerDelegate(this);

        loginButton = (TwitterLoginButton) rootView.findViewById(R.id.twitter_login_button);
        if(SucheApplication.getActiveSession()==null) {
            loginButton.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    TwitterSession session = result.data;
                    TwitterAuthToken authToken = session.getAuthToken();
                    String token = authToken.token;
                    String secret = authToken.secret;

                    Log.d("FeedFragment", "Token :" + token);
                }

                @Override
                public void failure(TwitterException exception) {
                    Log.d("TwitterKit", "Login with Twitter failure", exception);
                }
            });
        } else {
            loginButton.setVisibility(View.GONE);
        }

        return rootView;
    }

    @Override
    public void notifyFeedsUpdated() {
        Log.d("FeedFragment","Updated");
        if(mFeedAdapter!=null) {
            mFeedAdapter.updateList();
        }
    }

    private class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder>{

        Object[] feedsList;
        FeedViewFactory feedViewFactory;

        public FeedAdapter(){
            feedsList = FeedsManager.getInstance().getFeedList().values().toArray();
            feedViewFactory = new FeedViewFactory();
        }

        @Override
        public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_view_placeholder, parent, false);
            FeedViewHolder feedViewHolder = new FeedViewHolder(v);
            return feedViewHolder;
        }

        @Override
        public void onBindViewHolder(FeedViewHolder holder, int position) {
            holder.mViewPlaceHolderLayout.removeAllViews();
            holder.mViewPlaceHolderLayout.addView(feedViewFactory.getViewForType(getActivity(), (Feed) feedsList[position]));
        }

        @Override
        public int getItemCount() {
            return feedsList.length;
        }

        private void updateList(){
            feedsList = FeedsManager.getInstance().getFeedList().values().toArray();
            notifyDataSetChanged();
        }
    }

    private class FeedViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout mViewPlaceHolderLayout;

        public FeedViewHolder(View itemView) {
            super(itemView);
            mViewPlaceHolderLayout = (LinearLayout) itemView.findViewById(R.id.view_placeholder);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFeedAdapter.notifyDataSetChanged();
        if(loginButton!=null) {
            loginButton.onActivityResult(requestCode, resultCode, data);
            if(SucheApplication.getActiveSession()!=null){
                loginButton.setVisibility(View.GONE);
            }
        }
    }
}