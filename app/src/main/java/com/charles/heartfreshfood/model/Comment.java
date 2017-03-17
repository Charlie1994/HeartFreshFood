package com.charles.heartfreshfood.model;

/**
 * 用户论坛评论
 */
public class Comment {
    private String username;
    private String imgUrl;
    private String content;
    private String date;

    public Comment(){}
    public Comment(String username, String imgUrl, String content, String date) {
        this.username = username;
        this.imgUrl = imgUrl;
        this.content = content;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
