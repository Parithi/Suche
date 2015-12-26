package com.parithi.suche.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.parithi.suche.models.Feed;
import com.parithi.suche.models.RSSFeed;
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

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
    private FeedManagerDelegate feedManagerDelegate;

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

    public void setFeedManagerDelegate(FeedManagerDelegate feedManagerDelegate) {
        this.feedManagerDelegate = feedManagerDelegate;
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

                        if(feedManagerDelegate!=null){
                            feedManagerDelegate.notifyFeedsUpdated();
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

    private void getRSSFeeds(){
        new RssFeedFetcherTask().execute("http://petroleumshow.com/feed/");
    }

    public void getData(){
        getRSSFeeds();
        getTweets();
    }



    public class RssFeedFetcherTask extends AsyncTask<String, Void, ArrayList<Feed>> {

        private final String LOG_TAG = RssFeedFetcherTask.class.getSimpleName();

        private ArrayList<Feed> getRssFeedsFromXML(InputStream rssInputStream){
            ArrayList<Feed> rssFeedsList = new ArrayList<>();
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(rssInputStream, "UTF-8");
                boolean insideItem = false;

                int eventType = xpp.getEventType();

                int id = 0;
                RSSFeed rssFeed = null;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        Log.d("FeedParser",xpp.getName());
                        if (xpp.getName().equalsIgnoreCase("item")) {
                            insideItem = true;
                            rssFeed = new RSSFeed(id);
                            Log.d("FeedParser","Creating feed---");
                        } else if (xpp.getName().equalsIgnoreCase("title")) {
                            if (insideItem) {
                                if(rssFeed!=null) {
                                    rssFeed.setTitle(xpp.nextText().trim());
                                }
                            }
                        } else if (xpp.getName().equalsIgnoreCase("link")) {
                            if (insideItem) {
                                if (rssFeed != null) {
                                    rssFeed.setLink(xpp.nextText().trim());
                                }
                            }
                        } else if (xpp.getName().equalsIgnoreCase("description")) {
                            if (insideItem) {
                                if (rssFeed != null) {
                                    rssFeed.setDescription(xpp.nextText().trim());
                                }
                            }
                        }

                    } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                        insideItem = false;
                        if(rssFeed!=null) {
                            Log.d("FeedParser","Closing feed---");
                            rssFeedsList.add(rssFeed);
                        }
                    }

                    eventType = xpp.next();
                    id++;
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(LOG_TAG,rssFeedsList.toString());
            return rssFeedsList;
        }

        @Override
        protected void onPostExecute(ArrayList<Feed> feeds) {
            super.onPostExecute(feeds);
            for(Feed feed : feeds){
                if(feedList!=null) {
                    feedList.put(feed.getId(), feed);
                }
            }
            if(feedManagerDelegate!=null){
                feedManagerDelegate.notifyFeedsUpdated();
            }
        }

        @Override
        protected ArrayList<Feed> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            BufferedReader rssInputStream = null;
            HttpURLConnection urlConnection = null;
            String rssFeedString = null;
            try {
                URL url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                rssInputStream = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = rssInputStream.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                rssFeedString = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            try {
                return getRssFeedsFromXML(new ByteArrayInputStream(rssFeedString.getBytes("UTF-8")));
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }
    }

    public interface FeedManagerDelegate{
        void notifyFeedsUpdated();
    }
}
