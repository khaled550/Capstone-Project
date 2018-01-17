package com.example.khaled.mynewsapp.Models;

/**
 * Created by khaled on 12/28/17.
 */

public class PieceOfNews {
    private int articleId, clusterId;
    private String articleTitle, thumb_url, imgUrl, articleDescription, titleLink,
            articleAuthor, articlePubDate;

    public PieceOfNews(int articleId, int clusterId, String articleTitle, String thumb_url, String imgUrl, String articleDescription, String titleLink, String articleAuthor, String articlePubDate) {
        this.articleId = articleId;
        this.clusterId = clusterId;
        this.articleTitle = articleTitle;
        this.thumb_url = thumb_url;
        this.imgUrl = imgUrl;
        this.articleDescription = articleDescription;
        this.titleLink = titleLink;
        this.articleAuthor = articleAuthor;
        this.articlePubDate = articlePubDate;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getClusterId() {
        return clusterId;
    }

    public void setClusterId(int clusterId) {
        this.clusterId = clusterId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getThumb_url() {
        return thumb_url;
    }

    public void setThumb_url(String thumb_url) {
        this.thumb_url = thumb_url;
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

    public String getTitleLink() {
        return titleLink;
    }

    public void setTitleLink(String titleLink) {
        this.titleLink = titleLink;
    }

    public String getArticleAuthor() {
        return articleAuthor;
    }

    public void setArticleAuthor(String articleAuthor) {
        this.articleAuthor = articleAuthor;
    }

    public String getArticlePubDate() {
        return articlePubDate;
    }

    public void setArticlePubDate(String atriclePubDate) {
        this.articlePubDate = atriclePubDate;
    }
}
