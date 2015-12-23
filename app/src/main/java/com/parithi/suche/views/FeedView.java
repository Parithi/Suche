package com.parithi.suche.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parithi.suche.R;
import com.parithi.suche.models.FeedType;

/**
 * Created by earul on 12/17/15.
 */
public class FeedView extends LinearLayout {

    private TextView feedTitle;
    private TextView feedUsername;
    private TextView feedDescription;
    private ImageView feedUserImage;
    private ImageView feedTypeImage;
    private FeedType feedType;

    private FeedView(Context context) {
        super(context);
        init(context);
    }

    private FeedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private FeedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private FeedView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public FeedView(Context context, FeedType feedType) {
        super(context);
        this.feedType = feedType;
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.feed_layout, this);

        feedTypeImage = (ImageView) this.findViewById(R.id.feed_type_imageview);
        feedUserImage = (ImageView) this.findViewById(R.id.feed_user_imageview);
        feedTitle = (TextView) this.findViewById(R.id.feed_title_textview);
        feedDescription = (TextView) this.findViewById(R.id.feed_desc_textview);
        feedUsername = (TextView) this.findViewById(R.id.feed_username_textview);

        if(this.feedType == FeedType.RSSFEED) {
            feedUserImage.setVisibility(View.GONE);
            feedTypeImage.setImageResource(R.drawable.feed_icon);
        }

    }

    public TextView getFeedTitle() {
        return feedTitle;
    }

    public void setFeedTitle(TextView feedTitle) {
        this.feedTitle = feedTitle;
    }

    public TextView getFeedUsername() {
        return feedUsername;
    }

    public void setFeedUsername(TextView feedUsername) {
        this.feedUsername = feedUsername;
    }

    public TextView getFeedDescription() {
        return feedDescription;
    }

    public void setFeedDescription(TextView feedDescription) {
        this.feedDescription = feedDescription;
    }

    public ImageView getFeedUserImage() {
        return feedUserImage;
    }

    public void setFeedUserImage(ImageView feedUserImage) {
        this.feedUserImage = feedUserImage;
    }

    public ImageView getFeedTypeImage() {
        return feedTypeImage;
    }

    public void setFeedTypeImage() {
        this.feedTypeImage = feedTypeImage;
        switch (feedType){
            case FACEBOOK:
                break;
            case LINKEDIN:
                break;
            case RSSFEED:
                break;
            case INSTAGRAM:
                break;
        }
    }
}
