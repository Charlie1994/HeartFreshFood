package com.charles.heartfreshfood.model;

import java.io.Serializable;

/**
 * 产品简述用在自定义的cardview中
 */
public class ProductProfile implements Serializable{
    String productId;
    String productName;
    Integer purchaseNumber;
    Double productPrice;
    String productImgUrl;//产品展示图片

    public ProductProfile() {
    }

    public ProductProfile(String productId, String productName, Integer purchaseNumber, Double productPrice, String productImgUrl) {
        this.productId = productId;
        this.productName = productName;
        this.purchaseNumber = purchaseNumber;
        this.productPrice = productPrice;
        this.productImgUrl = productImgUrl;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getPurchaseNumber() {
        return purchaseNumber;
    }

    public void setPurchaseNumber(Integer purchaseNumber) {
        this.purchaseNumber = purchaseNumber;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImgUrl() {
        return productImgUrl;
    }

    public void setProductImgUrl(String productImgUrl) {
        this.productImgUrl = productImgUrl;
    }
}
