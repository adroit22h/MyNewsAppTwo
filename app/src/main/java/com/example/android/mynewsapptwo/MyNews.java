package com.example.android.mynewsapptwo;

public class MyNews {
    private String mArticleTitle;
    private String mArticleSectionName;
    private String mArticlePubdate;
    private String mArticleAuthorName;
    private String mArticleUrl;

    public MyNews(String articleTitle, String articleSectionName, String articlePubdate, String articleAuthorName, String articleUrl) {
        mArticleTitle = articleTitle;
        mArticleSectionName = articleSectionName;
        mArticlePubdate = articlePubdate;
        mArticleAuthorName = articleAuthorName;
        mArticleUrl = articleUrl;
    }

    public String getArticleTitle() {
        return mArticleTitle;
    }

    public String getArticleSectionName() {
        return mArticleSectionName;
    }

    public String getArticlePubdate() {
        return mArticlePubdate;
    }

    public String getArticleAuthorName() {
        return mArticleAuthorName;
    }

    public String getArticleUrl() {
        return mArticleUrl;
    }

}
