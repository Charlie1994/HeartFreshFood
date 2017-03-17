package com.charles.heartfreshfood.model;

/**
 *  产品评价显示
 */
public class Review extends Comment{
    private Integer rate;

    public Review() {
    }

    public Review(String username, String imgUrl, String content, String date, Integer rate) {
        super(username, imgUrl, content, date);
        this.rate = rate;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }
}
