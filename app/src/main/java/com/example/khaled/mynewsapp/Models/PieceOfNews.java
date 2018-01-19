package com.example.khaled.mynewsapp.Models;

/**
 * Created by khaled on 12/28/17.
 */

public class PieceOfNews {
    private int articleId;
    private String articleTitle, imgUrl, articleDescription, articleLink,
            articleCategory, articlePubDate;

    public PieceOfNews(){}

    public PieceOfNews(int articleId, String articleTitle, String imgUrl, String articleDescription, String titleLink, String articleCategory, String articlePubDate) {
        this.articleId = articleId;
        this.articleTitle = articleTitle;
        this.imgUrl = imgUrl;
        this.articleDescription = articleDescription;
        this.articleLink = titleLink;
        this.articleCategory = articleCategory;
        this.articlePubDate = articlePubDate;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getArticleDescription() {
        return articleDescription;
    }

    public void setArticleDescription(String articleDescription) {
        this.articleDescription = articleDescription;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    public String getArticleCategory() {
        return articleCategory;
    }

    public void setArticleCategory(String articleCategory) {
        this.articleCategory = articleCategory;
    }

    public String getArticlePubDate() {
        return articlePubDate;
    }

    public void setArticlePubDate(String articlePubDate) {
        this.articlePubDate = articlePubDate;
    }
}
