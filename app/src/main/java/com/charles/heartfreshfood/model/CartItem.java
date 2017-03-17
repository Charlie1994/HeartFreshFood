package com.charles.heartfreshfood.model;

import java.io.Serializable;

/**
 *  购物车单项信息
 */
public class CartItem implements Serializable{
    private String productid;
    private String vendorid;
    private String vendorName;
    private String productName;
    private String specification;
    private Double price;
    private Integer number;
    private String url;
    private Double subtotal;
    public CartItem() {
    }

    public CartItem(String productid, String vendorid, String vendorName, String productName, String specification, Double price, Integer number, String url, Double subtotal) {
        this.productid = productid;
        this.vendorid = vendorid;
        this.vendorName = vendorName;
        this.productName = productName;
        this.specification = specification;
        this.price = price;
        this.number = number;
        this.url = url;
        this.subtotal = subtotal;
    }

    public String getVendorid() {
        return vendorid;
    }

    public void setVendorid(String vendorid) {
        this.vendorid = vendorid;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public Double getSubtotal() {

        return price*number;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
