package com.code972.indexing.NewsArticles;

import java.util.Date;

/**
 * Created by Egozy on 06/11/2014.
 */
public class NewsPage {

    private String title, author, text, topic_url, url, category_url, type, category_title;
    private String[] sites;
    private int postNum, likes, reportedRepliesCount;
    private Date crawlDate, datetime;
    //shares

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTopic_url() {
        return topic_url;
    }

    public void setTopic_url(String topic_url) {
        this.topic_url = topic_url;
    }

    public String[] getSites() {
        return sites;
    }

    public void setSite(String[] sites) {
        this.sites = sites;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategory_url() {
        return category_url;
    }

    public void setCategory_url(String category_url) {
        this.category_url = category_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory_title() {
        return category_title;
    }

    public void setCategory_title(String category_title) {
        this.category_title = category_title;
    }

    public int getPostNum() {
        return postNum;
    }

    public void setPostNum(int postNum) {
        this.postNum = postNum;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getReportedRepliesCount() {
        return reportedRepliesCount;
    }

    public void setReportedRepliesCount(int reportedRepliesCount) {
        this.reportedRepliesCount = reportedRepliesCount;
    }

    public Date getCrawlDate() {
        return crawlDate;
    }

    public void setCrawlDate(Date crawlDate) {
        this.crawlDate = crawlDate;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public NewsPage() {
    }
}
