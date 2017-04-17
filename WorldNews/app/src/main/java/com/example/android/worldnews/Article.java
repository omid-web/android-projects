package com.example.android.worldnews;

/**
 * Created by Omid on 4/16/2017.
 */

class Article {
    private String mTitle;
    private String mSection;
    private String mDate;
    private String mUrl;

    public Article(String title, String section, String date, String url) {
        mTitle = title;
        mSection = section;
        mDate = date;
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSection() {
        return mSection;
    }

    public String getDate() {
        return mDate;
    }

    public String getUrl() {
        return mUrl;
    }
}
