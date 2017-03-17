package com.charles.heartfreshfood.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 产品信息
 */
public class ProductInfo implements Serializable{
    private String productID;
    private String vendorID;
    private String vendorname;
    private String imgurl;
    private ArrayList<String> picUrlList;
    private Double price;
    private String specification;
    private String productName;
    private Integer stock;
    private Double rate;


    public ProductInfo() {
    }

    public ProductInfo(String productID, String vendorID, String vendorname, String imgurl, ArrayList<String> picUrlList, Double price, String specification, String productName, Integer stock, Double rate) {
        this.productID = productID;
        this.vendorID = vendorID;
        this.vendorname = vendorname;
        this.imgurl = imgurl;
        this.picUrlList = picUrlList;
        this.price = price;
        this.specification = specification;
        this.productName = productName;
        this.stock = stock;
        this.rate = rate;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getVendorID() {
        return vendorID;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
    }

    public String getVendorname() {
        return vendorname;
    }

    public void setVendorname(String vendorname) {
        this.vendorname = vendorname;
    }

    public ArrayList<String> getPicUrlList() {
        return picUrlList;
    }

    public void setPicUrlList(ArrayList<String> picUrlList) {
        this.picUrlList = picUrlList;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
