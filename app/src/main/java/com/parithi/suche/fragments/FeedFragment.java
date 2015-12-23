package com.parithi.suche.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.parithi.suche.R;
import com.parithi.suche.models.Feed;
import com.parithi.suche.utils.FeedViewFactory;
import com.parithi.suche.utils.FeedsManager;

/**
 * Created by earul on 12/15/15.
 */
public class FeedFragment extends Fragment {

    private static final String LOG_TAG = FeedFragment.class.getSimpleName();

    private RecyclerView mFeedRecyclerView;
    private FeedAdapter mFeedAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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
        return rootView;
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

            holder.mActionsPlaceHolderLayout.removeAllViews();
            holder.mViewPlaceHolderLayout.addView(LayoutInflater.from(getActivity()).inflate(R.layout.twitter_actions_layout,null));
        }

        @Override
        public int getItemCount() {
            return feedsList.length;
        }
    }

    private class FeedViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout mViewPlaceHolderLayout;
        public LinearLayout mActionsPlaceHolderLayout;

        public FeedViewHolder(View itemView) {
            super(itemView);
            mViewPlaceHolderLayout = (LinearLayout) itemView.findViewById(R.id.view_placeholder);
            mActionsPlaceHolderLayout = (LinearLayout) itemView.findViewById(R.id.actions_placeholder);
        }

    }

}