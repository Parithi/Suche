package com.parithi.suche.models;

/**
 * Created by earul on 12/16/15.
 */
public class RSSFeed extends Feed {

    private String title;
    private String link;
    private String description;

    public RSSFeed(long id) {
        super(id, FeedType.RSSFEED);
    }

    public RSSFeed(long id, String title, String link, String description) {
        super(id, FeedType.RSSFEED);
        this.title = title;
        this.link = link;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
